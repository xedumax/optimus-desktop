package com.yobel.optimus.controller;

import com.yobel.optimus.file.CargaFileEtq;
import com.yobel.optimus.lib.CargaFileEtqVert;
import com.yobel.optimus.model.entity.*;
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

    @FXML
    public void initialize() {
        //Llenado de Combos
        cargarCuentas();
        cargarAgrupadores();
        cargarEtiquetas();

        // Control de visibilidad para el bloque Opcional (Solo ET3)
        cbEtiqueta.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean esET3 = newVal != null && newVal.startsWith("ET3");
            vbOpcional.setVisible(esET3);
            vbOpcional.setManaged(esET3);
        });

        // Listener para Cuenta
        cbCuenta.valueProperty().addListener((obs, oldVal, newVal) -> {
            validarYRefrescarLpr();
        });

        // Listener para AGP
        cbAgp.valueProperty().addListener((obs, oldVal, newVal) -> {
            validarYRefrescarLpr();
        });

        // Los Datos de Zona Cliente y Sub-Cta Cliente | Configurar restricciones para ingresos manuales
        configurarTextField(txtZonaCliente, 8);
        configurarTextField(txtSubCtaCliente, 2);
    }

    @FXML
    private void handleProcesar() {
        // Validamos selección mínima
        if (cbCuenta.getValue() == null || cbEtiqueta.getValue() == null) {
            AlertUtil.mostrarAdvertencia("Campos incompletos", "Debe seleccionar Cuenta y Etiqueta.");
            return;
        }

        // Captura de datos manuales
        String zona = txtZonaCliente.getText().trim();
        String subCta = txtSubCtaCliente.getText().trim();

        System.out.println("Procesando Etiqueta con Zona: " + zona + " y Sub-Cta: " + subCta);

        // Aquí invocarías al servicio pasando la URL de AppConfig
        // ej: impresionService.generar(AppConfig.Operaciones.impresion(), datos);
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

    private void cargarAgrupadores() {
        new Thread(() -> {
            try {
                // 1. Obtenemos la lista desde el servicio
                List<Agrupador> lista = maestroService.getAgrupadores(AppConfig.Maestros.agrupadores());

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

    private void cargarEtiquetas(){
        cbEtiqueta.getItems().addAll(
                "ET1: ETIQ. PED. VD 10 x 6",
                "ET2: ETIQ. PED. VD 9 x 6",
                "ET3: ETIQ. PED. VD 9 x 6 300 Dpi",
                "ET4: ETIQ. PED. VD 10 x 6",
                "ET5: ETIQ. PED. VD 10 x 6"
        );
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
                    } else {
                        cbLpr.getItems().clear();
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
    private void onAgpSelected(ActionEvent event) {
        // 0. Listar Linea de Producción
        Cuenta cuentaSeleccionada = cbCuenta.getValue();
        Agrupador agpSeleccionado = cbAgp.getValue();
        // Obtener CIA con valor default si la selección es null
        String cia = (cuentaSeleccionada != null) ? cuentaSeleccionada.getCtaCodigo() : "001";

        // Solo si cuenta y agp son diferentes de null, cargar líneas
        if (cuentaSeleccionada != null && agpSeleccionado != null) {
            cbLpr.getItems().clear();
            cbLpr.setPromptText("Seleccione Linea de Prod");
            cargarLpr(); // Supongo que este método ya usa AppConfig.Maestros.lineasProduccion(...)
        }

        // 1. Obtener la selección de la etiqueta
        // Nota: Asegúrate que el ComboBox de etiquetas tenga un valor seleccionado
        String seleccion = cbEtiqueta.getSelectionModel().getSelectedItem();
        if (seleccion == null) return;

        // 2. Determinar variables (Basado en tu lógica: ET1 = 001/Vertical)
        // Aquí puedes poner un switch si tienes más etiquetas (ET1, ET2, etc.)
        String codigoReporte = "001";
        String vFlg = "V";

        if (seleccion.equals("ET2")) { // Ejemplo para horizontal
            vFlg = "H";
            codigoReporte = "002";
        }

        // 3. Obtener URL desde AppConfig (Inyección directa)
        // Usamos la nueva sección de Etiquetas que definimos para tu AppConfig
        String urlApi = AppConfig.Etiquetas.datosImpresion(cia);

        // 4. Lógica de instanciación
        CargaFileEtq servicio = null;

        if (codigoReporte.equals("001") || codigoReporte.equals("016")) {
            if (vFlg.equals("H")) {
                if (codigoReporte.equals("001")){
                    //servicio = new CargaFileEtq1Horiz();
                    System.out.println("Carga Etiqueta #1 Horizontal instanciada");
                }else {
                    //servicio = new CargaFileEtq2Horiz();
                    System.out.println("Carga Etiqueta #2 Horizontal instanciada");
                }
            } else if (vFlg.equals("V")) {
                // servicio = new CargaFileEtq1Vert();
                System.out.println("Carga Etiqueta Vertical instanciada");
            }
        }

        // 5. Invocar al servicio pasando el URL directamente como argumento
        if (servicio != null) {
            try {
                // Configuramos los datos básicos en la clase base
                servicio.setvCia(cia);
                servicio.setvNomFil("ETQ_" + vFlg + "_" + cia + ".zpl");

                // LLAMADA AL API: Pasamos la URL directamente del AppConfig al método
                // Este método ejecutará internamente el OkHttpClient
                servicio.salvaFile(urlApi);

                System.out.println("Archivo ZPL generado en: " + AppConfig.PATH_ETIQUETAS);
                System.out.println("Proceso completado para: " + seleccion);

            } catch (Exception e) {
                System.err.println("Error al procesar etiquetas: " + e.getMessage());
                e.printStackTrace();
                // Mostrar alerta al usuario (opcional)
                // AlertaUtil.showError("Error", "No se pudo generar el archivo de etiquetas.");
            }
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
        } else {
            // Opcional: Limpiar el combo LPR si uno de los padres queda nulo
            cbLpr.getItems().clear();
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