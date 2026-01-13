package com.yobel.optimus.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LecturaController {
    @FXML private ComboBox<String> cbCuenta;
    @FXML private ComboBox<String> cbAgp;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtVentana;
    @FXML private TextField txtPedido;
    private AnchorPane mainContentArea; // Esta variable guardará la referencia al área central del menú

    @FXML
    public void initialize() {
        // Aquí puedes cargar datos iniciales en los ComboBox
        configurarDatePicker();

        dpFecha.getEditor().setEditable(false);// Bloquea la edición manual del texto, pero permite abrir el calendario
        dpFecha.getEditor().setFocusTraversable(false);// Opcional: Evitar que el campo reciba el foco del teclado
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