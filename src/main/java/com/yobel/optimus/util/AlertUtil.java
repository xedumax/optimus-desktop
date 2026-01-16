package com.yobel.optimus.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

    /**
     * Muestra un cuadro de diálogo para confirmar una acción.
     * @return true si el usuario presiona Aceptar, false en caso contrario.
     */
    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Para un diseño más limpio y centrado
        alert.setContentText(mensaje);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void crearAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}