package com.yobel.optimus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController {


    @FXML
    private AnchorPane contentArea;

    @FXML
    private void abrirLecturaEmpaques(ActionEvent actionEvent) {
        try {
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lectura-empaques.fxml"));
            Node view = loader.load();

            // Limpiar y cargar en el área central
            contentArea.getChildren().setAll(view);

            // Ajustar al tamaño del contenedor
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
