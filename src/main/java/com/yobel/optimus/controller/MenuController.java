package com.yobel.optimus.controller;

import com.yobel.optimus.model.entity.SystemItem;
import com.yobel.optimus.service.AuthService;
import com.yobel.optimus.util.AppContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class MenuController {

    @FXML
    private AnchorPane contentArea;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        System.out.println("Menú inicializado. Ambiente: " + AppContext.getBaseUrl());
    }

    @FXML
    private void abrirLecturaEmpaques(ActionEvent actionEvent) {
        Platform.runLater(this::cargarVistaLectura);    ////-----------SE ACCEDE SIN VALIDAR PERMISOS - SYSTEMNAME = “OHS” - PENDIENTE

        String systemUrl = AppContext.getBaseUrl() + "/api/msi/autorizacion/system";

        // 1. Validar permisos en un hilo separado
        new Thread(() -> {
            try {
                List<SystemItem> listaSistemas = authService.getSystems(systemUrl);

                // Verificar si el usuario tiene permiso para el sistema (ejemplo: "OHS")
                boolean tienePermiso = listaSistemas.stream()
                        .anyMatch(s -> "OHS".equalsIgnoreCase(s.getSystemName()));

                if (tienePermiso) {
                    Platform.runLater(this::cargarVistaLectura);
                } else {
                    Platform.runLater(() -> mostrarAlerta("Acceso Denegado", "No tienes permisos para 'Lectura Empaques'."));
                }
            } catch (IOException e) {
                //Platform.runLater(() -> mostrarAlerta("Error", "No se pudieron validar los permisos: " + e.getMessage()));
            }
        }).start();
    }

    private void cargarVistaLectura() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lectura-empaques.fxml"));
            Node view = loader.load();

            // Configurar controlador de la nueva vista
            LecturaController controller = loader.getController();
            controller.setMainContentArea(contentArea);

            // Inyectar en el área central
            contentArea.getChildren().setAll(view);

            // Ajustar al tamaño del contenedor
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar la vista de empaques.");
        }
    }



    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}