package com.yobel.optimus.service;

import com.yobel.optimus.util.ConfigManager;

public class PrintService {
    private ConfigManager config = ConfigManager.getInstance();

    public void imprimirEtiqueta() {
        String printerName = config.getPrinterName();
        String outputPath = config.getPrinterOutputPath();
        int dpi = config.getPrinterDPI();

        System.out.println("Impresora: " + printerName);
        System.out.println("Path: " + outputPath);
        System.out.println("DPI: " + dpi);

        // Lógica de impresión
    }

}
