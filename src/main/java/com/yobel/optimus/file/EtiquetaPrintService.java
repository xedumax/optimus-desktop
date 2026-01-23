package com.yobel.optimus.file;

import com.yobel.optimus.util.AlertUtil;
import javafx.application.Platform;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EtiquetaPrintService {
    public void enviarAImpresora(String rutaArchivo) {
        String nombreZebra = "ZebraZPL"; // El nombre de la impresora en Windows
        if (!existeImpresora(nombreZebra)) {
            //AlertUtil.mostrarAdvertencia("Configuración Faltante",
            System.out.println("La impresora '" + nombreZebra + "' no está instalada en este equipo.\n" +
                            "Por favor, verifique el nombre en Panel de Control.");
           // return;
        }

        Platform.runLater(() -> {
            new Thread(() -> {
                StringBuilder errorDetallado = new StringBuilder();
                try {
                    String[] cmd = {"cmd.exe", "/C", "type \"" + rutaArchivo.replace("/", "\\") + "\" >prn"};
                    Process proc = Runtime.getRuntime().exec(cmd);

                    // CAPTURAR EL ERROR REAL DEL CMD
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            errorDetallado.append(line).append("\n");
                        }
                    }

                    int exitVal = proc.waitFor();

                    Platform.runLater(() -> {
                        if (exitVal == 0) {
                            AlertUtil.mostrarInfo("Éxito", "Impresión iniciada correctamente.");
                        } else {
                            String mensajeExtra = interpretarCodigoError(exitVal, errorDetallado.toString());
                            AlertUtil.mostrarError("Error de Impresión", mensajeExtra);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private String interpretarCodigoError(int code, String errorCMD) {
        switch (code) {
            case 1:
                return "Error de acceso: El puerto 'prn' no está disponible.\n" +
                        "Asegúrese de haber compartido la impresora y mapeado el puerto LPT1.";
            case 2:
                return "Archivo no encontrado: Verifique la ruta del archivo generado.";
            case 5:
                return "Acceso denegado: Intente ejecutar el programa como Administrador.";
            default:
                return "Código " + code + ": " + (errorCMD.isEmpty() ? "Error desconocido del sistema." : errorCMD);
        }
    }

    private boolean existeImpresora(String nombreBuscado) {
        PrintService[] servicios = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService servicio : servicios) {
            if (servicio.getName().equalsIgnoreCase(nombreBuscado)) {
                return true;
            }
        }
        return false;
    }
}