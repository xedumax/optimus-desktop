package com.yobel.optimus.controller;

import com.yobel.optimus.model.entity.Agrupador;
import com.yobel.optimus.model.entity.Cuenta;
import com.yobel.optimus.model.entity.Ventana;
import com.yobel.optimus.model.request.LecturaRequest;
import com.yobel.optimus.service.BarcodeService;
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

public class LecturaController {
    @FXML private ComboBox<Cuenta> cbCuenta;
    @FXML private ComboBox<Agrupador> cbAgp;
    @FXML private TextField txtVentana, txtPedido;
    @FXML private Label lblInfoEmpaque;
    @FXML private Button btnQr;

    private AnchorPane mainContentArea;
    private BarcodeService barcodeService = new BarcodeService(new OkHttpClient());

    @FXML
    public void initialize() {
        cargarCuentas();
        cargarAgrupadores();

        txtPedido.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) ejecutarProcesamiento();
        });
        btnQr.setOnAction(event -> ejecutarProcesamiento());

        // Foco inicial sutil
        Platform.runLater(() -> cbCuenta.requestFocus());
    }

    private void cargarCuentas() {
        String token = AppContext.getToken();
        new Thread(() -> {
            try {
                List<Cuenta> listaFiltrada = barcodeService.getCuentas(token);
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
        String token = AppContext.getToken();

        new Thread(() -> {
            try {
                // 1. Obtenemos la lista desde el servicio
                List<Agrupador> lista = barcodeService.getAgrupadores(token);

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

    private void cargarVentanas(String codCuenta, String filtroAgp) {
        String token = AppContext.getToken();
        new Thread(() -> {
            try {
                List<Ventana> todas = barcodeService.getVentanas(token, codCuenta);
                Optional<Ventana> ventanaMatch = todas.stream()
                        .filter(v -> filtroAgp.equals(v.getAgpCuenta()) && "A".equals(v.getEstado()))
                        .findFirst();

                Platform.runLater(() -> {
                    if (ventanaMatch.isPresent()) {
                        Ventana v = ventanaMatch.get();
                        txtVentana.setText(v.getNroVentana() + " - " + v.getDesVentana());
                        txtPedido.requestFocus(); // Foco listo para la pistola
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

    private void ejecutarProcesamiento() {
        txtPedido.setText("PE0125067454001"); //PENDIENTE
        String codigo = txtPedido.getText().trim();

        if (codigo.isEmpty()) {
            lblInfoEmpaque.setText("Error: Escanee una etiqueta.");
            lblInfoEmpaque.setTextFill(Color.RED);
            txtPedido.setStyle("-fx-border-color: red;");
            return;
        }

        txtPedido.setStyle(null);
        procesarLectura(codigo);
        txtPedido.clear();
    }

    private void procesarLectura(String codigoBarras) {
        try {
            int longitud = codigoBarras.length();
            if (longitud < 6) throw new Exception("Código no válido");

            // 1. Descomposición de la información
            String ctr = codigoBarras.substring(0, 2); // CTR: primeros 2 caracteres
            String correlativo = codigoBarras.substring(longitud - 3); // Correlativo: últimos 3
            // Pedido: desde pos 3 hasta longitud - 3
            String numPedido = codigoBarras.substring(2, longitud - 3);

            // 2. Obtención de fecha y hora local
            LocalDateTime localDateTime = LocalDateTime.now();
            String fechaFormateada = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // 3. Actualizar UI (Texto verde con ajuste de línea)
            Platform.runLater(() -> {
                lblInfoEmpaque.setText(String.format(
                        "Último Empaque Leído:\nCTR: %s | Pedido: %s | Corr: %s\nHora Local: %s",
                        ctr, numPedido, correlativo, fechaFormateada
                ));
                lblInfoEmpaque.setTextFill(Color.web("#28a745"));
            });

            // 4. Preparar Request para el API
            LecturaRequest request = new LecturaRequest(
                    cbCuenta.getSelectionModel().getSelectedItem().getCtaCodigo(), // cuenta
                    cbAgp.getSelectionModel().getSelectedItem().getAgp(),          // codAgp
                    codigoBarras,                                                  // pedido (código leído)
                    fechaFormateada,                                               // fecModif
                    AppContext.getUsuario(), // userModif (Usuario del login)
                    fechaFormateada,                                               // fechaAutomatico
                    AppConstants.ORIGEN_AUTOMATICO                                 // origen
            );

            enviarAlApi(request);

        } catch (Exception e) {
            System.err.println("Error al invocar procesarLectura: " + e.getMessage());
            lblInfoEmpaque.setTextFill(Color.RED);
        }
    }

    private void enviarAlApi(LecturaRequest request) {
        String token = AppContext.getToken();
        new Thread(() -> {
            try {
                barcodeService.registrarBulto(token, request);
                Platform.runLater(() -> {
                    lblInfoEmpaque.setText("Registrado: " + request.getPedido());
                    lblInfoEmpaque.setTextFill(Color.web("#28a745"));
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblInfoEmpaque.setText("Error al registrar en servidor");
                    lblInfoEmpaque.setTextFill(Color.RED);
                    AlertUtil.mostrarError("Error de Registro", "No se pudo sincronizar el bulto. Se recomienda registro manual.");
                });
            }
        }).start();
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
        txtPedido.clear();
        txtVentana.clear();
        lblInfoEmpaque.setText("Esperando lectura...");
        lblInfoEmpaque.setTextFill(Color.GRAY);
    }

    public void setMainContentArea(AnchorPane contentArea) {
        this.mainContentArea = contentArea;
    }
}