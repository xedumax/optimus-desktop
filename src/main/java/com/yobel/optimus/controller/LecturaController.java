package com.yobel.optimus.controller;

import com.yobel.optimus.model.entity.Agrupador;
import com.yobel.optimus.model.entity.Cuenta;
import com.yobel.optimus.model.entity.Ventana;
import com.yobel.optimus.model.request.LecturaRequest;
import com.yobel.optimus.service.BarcodeService;
import com.yobel.optimus.util.AppContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class LecturaController {
    @FXML private ComboBox<Cuenta> cbCuenta;
    @FXML private ComboBox<Agrupador> cbAgp;
    @FXML private TextField txtVentana;
    @FXML private TextField txtPedido; // El campo para el lector de barras
    @FXML private Label lblInfoEmpaque;
    @FXML private DatePicker dpFecha;
    @FXML private Button btnQr;
    private AnchorPane mainContentArea; // Esta variable guardará la referencia al área central del menú
    private BarcodeService barcodeService = new BarcodeService(new OkHttpClient());

    @FXML
    public void initialize() {
        cargarCuentas();
        cargarAgrupadores();

        configurarDatePicker();
        dpFecha.getEditor().setEditable(false);// Bloquea la edición manual del texto, pero permite abrir el calendario
        dpFecha.getEditor().setFocusTraversable(false);// Opcional: Evitar que el campo reciba el foco del teclado

        // 1. Al presionar Enter en el TextField
        txtPedido.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                ejecutarProcesamiento();
            }
        });
        // 2. Al hacer clic en el botón QR
        btnQr.setOnAction(event -> ejecutarProcesamiento());
    }

    private void cargarCuentas() {
        String token = AppContext.getToken();

        new Thread(() -> {
            try {
                // 1. Llamada al servicio (que ya tiene el filtro de Tipo '1')
                List<Cuenta> listaFiltrada = barcodeService.getCuentas(token);

                Platform.runLater(() -> {
                    if (listaFiltrada != null && !listaFiltrada.isEmpty()) {
                        // 2. Llenar el combo
                        cbCuenta.getItems().setAll(listaFiltrada);
                        System.out.println("Cuentas cargadas: " + listaFiltrada.size());
                    } else {
                        cbCuenta.setPromptText("Sin cuentas disponibles");
                        System.out.println("No se encontraron cuentas de tipo '1'.");
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    System.err.println("Error al invocar servicio de cuentas: " + e.getMessage());
                });
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
                // 1. Obtenemos la lista de ventanas de la cuenta seleccionada
                List<Ventana> todas = barcodeService.getVentanas(token, codCuenta);

                // 2. Filtramos por AGP y Estado 'A' (Activo)
                Optional<Ventana> ventanaMatch = todas.stream()
                        .filter(v -> filtroAgp.equals(v.getAgpCuenta()))
                        .filter(v -> "A".equals(v.getEstado()))
                        .findFirst();

                Platform.runLater(() -> {
                    if (ventanaMatch.isPresent()) {
                        Ventana v = ventanaMatch.get();
                        txtVentana.setText(v.getNroVentana() + " - " + v.getDesVentana());

                        // 3. ENVIAR FOCO AL CAMPO DE PEDIDO
                        // Esto permite que el usuario pase la pistola de códigos inmediatamente
                        txtPedido.requestFocus();
                        System.out.println("Ventana asignada. Cursor listo en txtPedido.");
                    } else {
                        txtVentana.setText("SIN VENTANA ACTIVA");
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> txtVentana.setText("Error de conexión"));
                e.printStackTrace();
            }
        }).start();
    }

    private void ejecutarProcesamiento() {
        String codigo = txtPedido.getText().trim();

        // SIMULACIÓN DE CAMPO VACÍO
        if (codigo.isEmpty()) {
            // 1. Mostrar alerta visual en el label verde (pero en rojo)
            lblInfoEmpaque.setText("Error: El campo de pedido está vacío. Escanee una etiqueta.");
            lblInfoEmpaque.setTextFill(javafx.scene.paint.Color.RED);

            // 2. Opcional: Cambiar el borde del TextField para llamar la atención
            txtPedido.setStyle("-fx-border-color: red; -fx-border-width: 2;");

            // 3. Regresar el foco al campo para que el usuario intente de nuevo
            txtPedido.requestFocus();
            return; // Detiene la ejecución aquí
        }

        // Si no está vacío, restauramos el estilo y procesamos
        txtPedido.setStyle(null);
        procesarLectura(codigo);
        txtPedido.clear();
        txtPedido.requestFocus();
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
                    "JRUIZ_TEST", // userModif (Usuario del login)
                    fechaFormateada,                                               // fechaAutomatico
                    "A"                                                            // origen
            );

            enviarAlApi(request);

        } catch (Exception e) {
            System.err.println("Error al invocar procesarLectura: " + e.getMessage());
            lblInfoEmpaque.setTextFill(Color.RED);
        }
    }

    private void enviarAlApi(LecturaRequest request) {
        String token = AppContext.getToken();

        // Ejecutamos en un hilo separado para no congelar la pantalla
        new Thread(() -> {
            try {
                // Llamada al servicio
                barcodeService.registrarBulto(token, request);

                // Éxito: Actualizamos la UI en el hilo principal
                Platform.runLater(() -> {
                    lblInfoEmpaque.setText("✓ Registro exitoso: " + request.getPedido());
                    lblInfoEmpaque.setTextFill(Color.web("#28a745"));
                });

            } catch (Exception e) {
                // Error o pérdida de conexión
                Platform.runLater(() -> {
                    System.err.println("Error al invocar enviarAlApi : " + e.getMessage());
                    lblInfoEmpaque.setTextFill(Color.RED);

                    // Aquí llamar al método de persistencia local (SQL o File)
                    //guardarEnLogLocal(request);
                });
            }
        }).start();
    }

    @FXML
    private void volverAlMenu() {
        limpiarCampos();
        // Obtenemos el contenedor principal (contentArea) y removemos esta vista
        if (mainContentArea != null) {
            mainContentArea.getChildren().clear();
        }
    }

    private void limpiarCampos() {
        txtPedido.clear();
        dpFecha.setValue(null);
        // Limpiar ComboBoxes si es necesario
    }

    private void configurarDatePicker() {
        // Definimos el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dpFecha.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                }
                return null;
            }
        });

        // Opcional: Establecer la fecha de hoy por defecto
        dpFecha.setValue(LocalDate.now());
    }

    public void setMainContentArea(AnchorPane area) {
        this.mainContentArea = area;
    }


}