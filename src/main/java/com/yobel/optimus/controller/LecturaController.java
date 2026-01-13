package com.yobel.optimus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

public class LecturaController {
    @FXML private ComboBox<String> cbCuenta;
    @FXML private ComboBox<String> cbAgp;

    @FXML private TextField txtFecha;
    @FXML private DatePicker dpFecha;

    @FXML private TextField txtVentana;
    @FXML private TextField txtPedido;

    @FXML
    public void initialize() {
        // Aquí puedes cargar datos iniciales en los ComboBox
        //configurarFormatoFecha(); <TextField fx:id="txtFecha" prefWidth="120" />
        configurarDatePicker();
        // Bloquea la edición manual del texto, pero permite abrir el calendario
        dpFecha.getEditor().setEditable(false);

        // Opcional: Evitar que el campo reciba el foco del teclado
        dpFecha.getEditor().setFocusTraversable(false);
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

    @FXML
    private void cerrarVentana(ActionEvent event) {
        // 1. Limpiar los campos (por seguridad)
        cbCuenta.getSelectionModel().clearSelection();
        cbAgp.getSelectionModel().clearSelection();
        txtVentana.clear();
        txtPedido.clear();
        dpFecha.setValue(null);

        // 2. Cerrar la ventana actual
        Stage stage = (Stage) txtPedido.getScene().getWindow();
        stage.close();
    }
}