package com.yobel.optimus.file;

import com.yobel.optimus.util.AlertUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class EtiquetaPrintService {

    /**
     * Envía un archivo a la impresora mediante comandos de consola de Windows.
     * @param rutaArchivo Ruta absoluta del archivo (.txt o .zpl)
     */
    public void enviarAImpresora(String rutaArchivo) {
        // Ejecutamos la confirmación en el hilo de la UI
        Platform.runLater(() -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Impresión");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("Se procederá a imprimir las etiquetas.\n¿Desea continuar?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

                // Ejecución del comando en un hilo separado para no bloquear la app
                new Thread(() -> {
                    try {
                        String[] cmd = {
                                "cmd.exe",
                                "/C",
                                "type \"" + rutaArchivo + "\" > PRN"
                        };

                        Process proc = Runtime.getRuntime().exec(cmd);
                        int exitVal = proc.waitFor();

                        Platform.runLater(() -> {
                            if (exitVal == 0) {
                                AlertUtil.mostrarInfo("Impresión", "Archivo enviado a la impresora con éxito.");
                            } else {
                                AlertUtil.mostrarError("Error", "No se pudo completar la impresión (Código: " + exitVal + ")");
                            }
                        });

                    } catch (Exception e) {
                        Platform.runLater(() ->
                                AlertUtil.mostrarError("Error Crítico", "Fallo al ejecutar comando de impresión: " + e.getMessage()));
                    }
                }).start();
            }
        });
    }
}