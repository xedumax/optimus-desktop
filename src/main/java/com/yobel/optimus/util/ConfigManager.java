package com.yobel.optimus.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                System.err.println("No se encontró application.properties");
                return;
            }

            properties.load(input);
            System.out.println("✓ Configuración cargada correctamente");

        } catch (IOException ex) {
            System.err.println("Error al cargar configuración: " + ex.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }


    public String getAppName() {
        return getProperty("app.name", "Optimus");
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url", "http://localhost:8080/api");
    }

    public String getPrinterName() {
        return getProperty("printer.zebra.name");
    }

    public String getPrinterOutputPath() {
        return getProperty("printer.zebra.output.path", "C:/zebra/output");
    }

    public int getPrinterDPI() {
        return getIntProperty("printer.zebra.dpi", 203);
    }

    public String getBarcodeFormat() {
        return getProperty("barcode.format", "CODE128");
    }

    public int getSessionTimeout() {
        return getIntProperty("session.timeout", 3600);
    }
}