package com.yobel.optimus.controller;

import com.yobel.optimus.model.entity.Agrupador;
import com.yobel.optimus.model.entity.Cuenta;
import com.yobel.optimus.model.entity.Ventana;
import com.yobel.optimus.model.request.LecturaRequest;
import com.yobel.optimus.service.LecturaEmpaqueService;
import com.yobel.optimus.service.MaestroService;
import com.yobel.optimus.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class LecturaEmpaqueController {
    @FXML private ComboBox<Cuenta> cbCuenta;
    @FXML private ComboBox<Agrupador> cbAgp;
    @FXML private TextField txtVentana, txtPedido;
    @FXML private Label lblInfoEmpaque;
    @FXML private Button btnBarcode;

    private AnchorPane mainContentArea;
    private LecturaEmpaqueService lecturaEmpaqueService = new LecturaEmpaqueService(new OkHttpClient());
    private MaestroService maestrodeService = new MaestroService(new OkHttpClient());

    @FXML
    public void initialize() {
        cargarCuentas();
        cargarAgrupadores();

        // Foco inicial sutil
        Platform.runLater(() -> cbCuenta.requestFocus());

        // Listener para Combo AGP - Donde envia el focus al pedido
        cbAgp.valueProperty().addListener((obs, oldVal, newVal) -> {
            txtPedido.requestFocus(); // Foco listo para scanear
        });

        // Al presionar Enter en el TextField, ejecuta el procesamiento
        txtPedido.setOnAction(event -> ejecutarProcesamiento());

    }

    private void cargarCuentas() {
        new Thread(() -> {
            try {
                List<Cuenta> listaFiltrada = maestrodeService.getCuentas(AppConfig.Maestros.cuentas());
                Platform.runLater(() -> {
                    if (listaFiltrada != null && !listaFiltrada.isEmpty()) {
                        cbCuenta.getItems().setAll(listaFiltrada);
                    } else {
                        cbCuenta.setPromptText("Sin cuentas disponibles");
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> AlertUtil.mostrarError("Error", "No se pudieron cargar las cuentas."));
            }
        }).start();
    }

    private void cargarAgrupadores() {
        new Thread(() -> {
            try {
                // 1. Obtenemos la lista desde el servicio
                List<Agrupador> lista = maestrodeService.getAgrupadores(AppConfig.Maestros.agrupadores());

                // 2. Actualizamos la UI en el hilo principal
                Platform.runLater(() -> {
                    if (lista != null && !lista.isEmpty()) {
                        cbAgp.getItems().setAll(lista);
                    } else {
                        System.out.println("La lista de agrupadores llegó vacía.");
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    System.err.println("Error al cargar combos: " + e.getMessage());
                });
            }
        }).start();
    }

    private void cargarVentanas(String codCuenta, String filtroAgp) {
        new Thread(() -> {
            try {
                List<Ventana> todas = maestrodeService.getVentanas(AppConfig.Maestros.ventanas()+codCuenta);
                Optional<Ventana> ventanaMatch = todas.stream()
                        .filter(v -> filtroAgp.equals(v.getAgpCuenta()) && "A".equals(v.getEstado()))
                        .findFirst();

                Platform.runLater(() -> {
                    if (ventanaMatch.isPresent()) {
                        Ventana v = ventanaMatch.get();
                        txtVentana.setText(v.getNroVentana() + " - " + v.getDesVentana());
                        txtPedido.requestFocus(); // Foco listo para scanear
                    } else {
                        txtVentana.setText(AppConstants.AGRUPADOR_SIN_VENTANA);
                        //AlertUtil.mostrarAdvertencia("Atención", "No hay ventanas activas para este agrupador.");
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> txtVentana.setText("Error de conexión"));
            }
        }).start();
    }

    @FXML
    private void ejecutarProcesamiento() {
        if (!validarCampos()) return;

        //Lectura por codigo de barras e implementación - Se ingresará manualmente mientras tanto
        String codigo = txtPedido.getText().trim();

        if (codigo.isEmpty()) {
            lblInfoEmpaque.setVisible(true);
            lblInfoEmpaque.setText("Error: Escanee una etiqueta.");
            lblInfoEmpaque.setTextFill(Color.RED);
            txtPedido.setStyle("-fx-border-color: red;");
            return;
        }

        txtPedido.setStyle(null);
        procesarLectura(codigo);
        txtPedido.clear();
        txtPedido.requestFocus();
    }

    private void procesarLectura(String codigoBarras) {
        try {
            int longitud = codigoBarras.length();
            if (longitud < 15) throw new Exception("Código no válido (muy corto)");

            lblInfoEmpaque.setText("Error: Escanee una etiqueta.");

            // 1. Descomposición (Lógica de negocio estable)
            String ctr = codigoBarras.substring(0, 2);
            String correlativo = codigoBarras.substring(longitud - 3);
            String numPedido = codigoBarras.substring(2, longitud - 3);

            String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // 2. Actualizar UI directamente (Estamos en el UI Thread aquí)
            lblInfoEmpaque.setVisible(false);
            lblInfoEmpaque.setTextFill(Color.GREEN);
            lblInfoEmpaque.setText(String.format
                                    ("Último Empaque Leído:\nCTR: %s | Pedido: %s | Corr: %s",
                                     ctr, numPedido, correlativo));

            // 3. Captura de datos para el Request
            String codCta = cbCuenta.getValue().getCtaCodigo();
            String codAgp = cbAgp.getValue().getAgp();
            String usuario = AppContext.getUsuario();

            LecturaRequest request = new LecturaRequest(
                    codCta, codAgp, codigoBarras, fechaFormateada,
                    usuario, fechaFormateada, AppConstants.ORIGEN_AUTOMATICO
            );

            // 4. Enviar al hilo secundario
            enviarAlApi(request);

        } catch (Exception e) {
            // 3. CAPTURA DE EXCEPCIÓN: Aquí manejamos el "Código no válido"
            lblInfoEmpaque.setVisible(true);   // Aseguramos que se vea
            lblInfoEmpaque.setManaged(true);   // Aseguramos que ocupe espacio
            lblInfoEmpaque.setTextFill(Color.RED); // Color de error

            // Mostramos exactamente el mensaje que pusimos en el throw
            lblInfoEmpaque.setText("Error: " + e.getMessage());

            // Opcional: Marcar el cuadro de texto en rojo para llamar la atención
            txtPedido.setStyle("-fx-border-color: red;");
        }
    }

    private void enviarAlApi(LecturaRequest request) {
        new Thread(() -> {
            try {
                // Llamada al servicio usando AppConfig directamente como instruiste
                lecturaEmpaqueService.registrarBulto(AppConfig.Operaciones.capturaBultos(), request);
                lblInfoEmpaque.setVisible(true);

                Platform.runLater(() -> {
                    lblInfoEmpaque.setTextFill(Color.GREEN);
                    //limpiarCampos();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblInfoEmpaque.setText("Error al registrar en servidor");
                    lblInfoEmpaque.setTextFill(Color.RED);
                    AlertUtil.mostrarError("Error de Registro", "No se pudo sincronizar el bulto.");
                });
            }
        }).start();
    }

    private boolean validarCampos() {
        if (cbCuenta.getValue() == null) {
            AlertUtil.mostrarAdvertencia("Falta Información", "Seleccione una Cuenta.");
            return false;
        }
        if (cbAgp.getValue() == null) {
            AlertUtil.mostrarAdvertencia("Falta Información", "Seleccione un Modelo de Etiqueta.");
            return false;
        }

        lblInfoEmpaque.setVisible(true);
        lblInfoEmpaque.setManaged(true);
        lblInfoEmpaque.setTextFill(Color.GRAY);
        lblInfoEmpaque.setText("Esperando lectura... ");
        return true;
    }

    @FXML
    private void onCuentaSelected(ActionEvent event) {
        Cuenta seleccionada = cbCuenta.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            System.out.println("Cuenta seleccionada: " + seleccionada.getCtaCodigo());

            // Lógica opcional: Si la cuenta tiene un AGP por defecto, seleccionarlo en cbAgp
            if (seleccionada.getCtaAgpDflt() != null) {
                String agpDefecto = seleccionada.getCtaAgpDflt();
                cbAgp.getItems().stream()
                        .filter(a -> a.getAgp().equals(agpDefecto))
                        .findFirst()
                        .ifPresent(a -> cbAgp.getSelectionModel().select(a));
            }
        }
    }

    @FXML
    private void onAgpSelected(ActionEvent event) {
        Agrupador agpSeleccionado = cbAgp.getSelectionModel().getSelectedItem();
        Cuenta cuentaSeleccionada = cbCuenta.getSelectionModel().getSelectedItem();

        if (agpSeleccionado != null && cuentaSeleccionada != null) {
            cargarVentanas(cuentaSeleccionada.getCtaCodigo(), agpSeleccionado.getAgp());
        }
    }

    @FXML
    private void volverAlMenu() {
        // 1. Limpiamos los datos del formulario por seguridad
        limpiarCampos();

        // 2. Verificamos que la referencia al contenedor no sea nula
        if (mainContentArea != null) {
            // Removemos la vista actual (Lectura) del contenedor del Menú
            mainContentArea.getChildren().clear();
            System.out.println("Vista de lectura removida. El área central queda limpia.");
        } else {
            // Si llega aquí, es porque olvidaste el setMainContentArea en el MenuController
            System.err.println("Error: No se tiene referencia al contentArea.");
        }
    }

    private void limpiarCampos() {
        // Limpiar Selecciones
        cbCuenta.getSelectionModel().clearSelection();
        cbAgp.getSelectionModel().clearSelection();
        txtVentana.clear();
        txtPedido.clear();
    }

    public void setMainContentArea(AnchorPane contentArea) {
        this.mainContentArea = contentArea;
    }
}