package com.yobel.optimus.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {

    public static void mostrarInfo(String titulo, String mensaje) {
        crearAlerta(titulo, mensaje, AlertType.INFORMATION);
    }

    public static void mostrarError(String titulo, String mensaje) {
        crearAlerta(titulo, mensaje, AlertType.ERROR);
    }

    public static void mostrarAdvertencia(String titulo, String mensaje) {
        crearAlerta(titulo, mensaje, AlertType.WARNING);
    }

    private static void crearAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}