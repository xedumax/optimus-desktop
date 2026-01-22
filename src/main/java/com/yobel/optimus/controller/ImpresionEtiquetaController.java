package com.yobel.optimus.controller;

import com.yobel.optimus.file.EtiquetaPrintService;
import com.yobel.optimus.lib.CargaFileEtq1Vert;
import com.yobel.optimus.model.entity.*;
import com.yobel.optimus.lib.GeneradorEtiqueta;
import com.yobel.optimus.service.MaestroService;
import com.yobel.optimus.util.AlertUtil;
import com.yobel.optimus.util.AppConfig;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImpresionEtiquetaController {

    @FXML private ComboBox<Cuenta> cbCuenta;
    @FXML private ComboBox<String> cbEtiqueta;
    @FXML private ComboBox<Agrupador> cbAgp;
    @FXML private ComboBox<LineaProduccion> cbLpr; // Lista de impresoras o similar
    @FXML private TextField txtZonaCliente, txtSubCtaCliente;
    @FXML private VBox vbOpcional;

    private AnchorPane mainContentArea;
    private MaestroService maestroService = new MaestroService(new OkHttpClient());
    private EtiquetaPrintService printService = new EtiquetaPrintService();

    @FXML
    public void initialize() {

        //Llenado de Combos
        cargarCuentas();
        cargarAgrupadores();
        cargarEtiquetas();

        // Control de visibilidad para el bloque Opcional (Solo ET3)
        cbEtiqueta.valueProperty().addListener((obs, oldVal, newVal) -> {
            cargarAgrupadores();
            boolean esET3 = newVal != null && newVal.startsWith("ET3");
            vbOpcional.setVisible(esET3);
            vbOpcional.setManaged(esET3);
        });

        // Listener para Cuenta
        cbCuenta.valueProperty().addListener((obs, oldVal, newVal) -> {
            validarYRefrescarLpr(); //Limpiar y volver a llamar servicio
        });

        // Listener para AGP
        cbAgp.valueProperty().addListener((obs, oldVal, newVal) -> {
            validarYRefrescarLpr(); //Limpiar y volver a llamar servicio
        });

        // Los Datos de Zona Cliente y Sub-Cta Cliente | Configurar restricciones para ingresos manuales
        configurarTextField(txtZonaCliente, 8);
        configurarTextField(txtSubCtaCliente, 2);
    }

    @FXML
    private void handleProcesar() {
        // 1. Validaciones previas
        Cuenta cuentaSeleccionada = cbCuenta.getValue();
        if (cuentaSeleccionada == null || cbEtiqueta.getValue() == null) {
            AlertUtil.mostrarAdvertencia("Campos incompletos", "Debe seleccionar Cuenta y Etiqueta.");
            return;
        }

        String codCuenta = cuentaSeleccionada.getCtaCodigo();
        // Obtenemos valores de los combos opcionales (si no hay selección, enviamos "null" al API)
        String codAgp = (cbAgp.getValue() != null) ? cbAgp.getValue().getAgp() : "null";
        String codLpr = (cbLpr.getValue() != null) ? cbLpr.getValue().getLpr() : "null";

        // 2. Iniciar proceso en hilo secundario
        new Thread(() -> {
            try {
                // Construimos la URL usando AppConfig como solicitaste
                String url = AppConfig.Operaciones.infoEtiquetas(codCuenta, codAgp, codLpr);
                List<InfoEtiqueta> listaEtiquetas = maestroService.getInfoEtiquetas(url);

                if (listaEtiquetas != null && !listaEtiquetas.isEmpty()) {
                    // OBTENER DEL PRIMER REGISTRO
                    InfoEtiqueta primerRegistro = listaEtiquetas.get(0);

                    String pOrientacion = primerRegistro.getEtqFlgOrienta(); // etqFlgOrienta
                    String pOrden = primerRegistro.getEtqFlgOrden();       // etqFlgOrden
                    String claseEjecutar;

                    // 3. Lógica de decisión de Clase
                    if ("H".equals(pOrientacion)) {
                        if ("001".equals(codCuenta)) {
                            claseEjecutar = "CargaFileEtq1Horiz";
                        } else {
                            claseEjecutar = "CargaFileEtq2Horiz";
                        }
                    } else if ("V".equals(pOrientacion)) {
                        claseEjecutar = "CargaFileEtq1Vert";
                    } else {
                        claseEjecutar = "";
                    }

                    // 4. Ejecutar generación de TXT
                    if (!claseEjecutar.isEmpty()) {
                        claseEjecutar = "CargaFileEtq1Vert";//******prueba
                        System.out.println("Ejecutando proceso: " + claseEjecutar);

                        //0. Datos
                        String rutaBase = AppConfig.Directorios.rutaEtiquetas();
                        File carpeta = new File(rutaBase);

                        // Crear ruta si no existe
                        if (!carpeta.exists()) {
                            carpeta.mkdirs();
                        }

                        // 1. Primero construimos el TXT
                        generarArchivoTxt(codCuenta + "_" + claseEjecutar + ".txt",
                                                        claseEjecutar, listaEtiquetas);

                        printService.enviarAImpresora(AppConfig.Directorios.rutaEtiquetas());

                        String finalClaseEjecutar = claseEjecutar; //****** prueba
                        Platform.runLater(() ->
                                AlertUtil.mostrarInfo("Éxito", "Proceso " + finalClaseEjecutar + " finalizado correctamente."));
                    } else {
                        System.err.println("No se pudo determinar la clase para la orientación: " + pOrientacion);
                    }

                } else {
                    Platform.runLater(() ->
                            AlertUtil.mostrarAdvertencia("Sin datos", "El API no devolvió información para los filtros seleccionados."));
                }

            } catch (IOException e) {
                Platform.runLater(() ->
                        AlertUtil.mostrarError("Error de Conexión", "No se pudo obtener la información de etiquetas."));
            }
        }).start();
    }

    private void generarArchivoTxt(String nombreArchivo, String nombreClase, List<InfoEtiqueta> listaEtiquetas) {
        try {
            GeneradorEtiqueta generador;
            if (nombreClase.equals("CargaFileEtq1Vert")) {
                generador = new CargaFileEtq1Vert();
            } else {
                return;
            }

            // Generar el TXT usando tu librería existente
            generador.generar(listaEtiquetas, AppConfig.Directorios.rutaEtiquetas(), nombreArchivo);
            System.out.println("Archivo TXT creado con éxito.");

        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> AlertUtil.mostrarError("Error", "No se pudo crear el archivo TXT."));
        }
    }

    private void cargarCuentas() {
        new Thread(() -> {
            try {
                List<Cuenta> listaFiltrada = maestroService.getCuentas(AppConfig.Maestros.cuentas());
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

    private void cargarEtiquetas(){
        cbEtiqueta.getItems().addAll(
                "ET1: ETIQ. PED. VD 10 x 6",
                "ET2: ETIQ. PED. VD 9 x 6",
                "ET3: ETIQ. PED. VD 9 x 6 300 Dpi",
                "ET4: ETIQ. PED. VD 10 x 6",
                "ET5: ETIQ. PED. VD 10 x 6"
        );
    }

    private void cargarAgrupadores() {
        new Thread(() -> {
            try {
                // 0. Limpiamos el combo asociado
                cbLpr.getItems().clear();
                cbLpr.setPromptText("Seleccione Linea de Prod");

                // 1. Obtenemos la lista desde el servicio
                List<Agrupador> lista = maestroService.getAgrupadores(AppConfig.Maestros.agrupadores());

                // 2. Actualizamos la UI en el hilo principal
                Platform.runLater(() -> {
                    if (lista != null && !lista.isEmpty()) {
                        // 1. Limpiar datos y selección
                        cbAgp.getSelectionModel().clearSelection();
                        cbAgp.setValue(null);

                        // 2. Cargar nuevos items
                        cbAgp.getItems().setAll(lista);

                        // 3.Resetear la celda visual y el prompt
                        cbAgp.setButtonCell(new ListCell<Agrupador>() {
                            @Override
                            protected void updateItem(Agrupador item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(cbAgp.getPromptText());
                                } else {
                                    setText(item.toString());
                                }
                            }
                        });
                        cbAgp.setPromptText("Seleccione Agrupador");
                        System.out.println("La lista de agrupadores.");
                    } else {
                        cbAgp.getItems().clear();
                        cbAgp.setPromptText("Sin datos disponibles");
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

    private void cargarLpr() {
        // Protección adicional: solo procede si ambos tienen valor
        if (cbCuenta.getValue() == null || cbAgp.getValue() == null) return;

        String codCuenta = cbCuenta.getValue().getCtaCodigo();
        String codAgp = cbAgp.getValue().getAgp();

        new Thread(() -> {
            try {
                // Se construye la URL dinámica desde AppConfig pasando los 2 parámetros
                String url = AppConfig.Maestros.lineasProduccion(codCuenta, codAgp);
                List<LineaProduccion> lista = maestroService.getLineasProduccion(url);

                Platform.runLater(() -> {
                    if (lista != null && !lista.isEmpty()) {
                        cbLpr.getItems().setAll(lista);


                        // 1. Limpiar datos y selección
                        cbLpr.getSelectionModel().clearSelection();
                        cbLpr.setValue(null);

                        // 2. Cargar nuevos items
                        cbLpr.getItems().setAll(lista);

                        // 3.Resetear la celda visual y el prompt
                        cbLpr.setButtonCell(new ListCell<LineaProduccion>() {
                            @Override
                            protected void updateItem(LineaProduccion item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(cbLpr.getPromptText());
                                } else {
                                    setText(item.toString());
                                }
                            }
                        });
                        cbLpr.setPromptText("Seleccione Linea de Prod.");
                        System.out.println("La lista de agrupadores.");
                    } else {
                        cbLpr.setPromptText("Sin líneas disponibles");
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> AlertUtil.mostrarError("Error", "No se pudo cargar la información de LPR."));
            }
        }).start();
    }

    @FXML
    private void onCuentaSelected(ActionEvent event) {
        Cuenta seleccionada = cbCuenta.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            System.out.println("Cuenta seleccionada: " + seleccionada.toString());
        }
    }

    @FXML
    private void onEtiquetaSelected(ActionEvent event) {
        // 0. Listar Linea de Producción
        Cuenta cuentaSeleccionada = cbCuenta.getValue();
        Agrupador agpSeleccionado = cbAgp.getValue();
        // Obtener CIA con valor default si la selección es null
        String cia = (cuentaSeleccionada != null) ? cuentaSeleccionada.getCtaCodigo() : "001";

        // Solo si cuenta y agp son diferentes de null, cargar líneas
        if (cuentaSeleccionada != null && agpSeleccionado != null) {
            cbLpr.getItems().clear();
            cargarLpr(); // método ya usa AppConfig.Maestros.lineasProduccion(...)
        }

    }

    /**
     * Método intermedio para validar la condición antes de llamar a la carga
     */
    private void validarYRefrescarLpr() {
        if (cbCuenta.getValue() != null && cbAgp.getValue() != null) {
            cbLpr.getItems().clear();
            cbLpr.setPromptText("Seleccione Linea de Prod");
            cargarLpr();
        }
    }

    /**
     * Aplica filtro de longitud máxima y fuerza mayúsculas
     */
    private void configurarTextField(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            // 1. Convertir a mayúsculas automáticamente
            change.setText(change.getText().toUpperCase());

            // 2. Validar longitud máxima
            int nuevaLongitud = change.getControlNewText().length();
            if (nuevaLongitud <= maxLength) {
                return change;
            }
            return null; // Rechaza el cambio si excede el límite
        }));
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
        txtSubCtaCliente.clear();
        txtZonaCliente.clear();
    }

    public void setMainContentArea(AnchorPane contentArea) {
        this.mainContentArea = contentArea;
    }
}