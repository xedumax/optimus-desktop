package com.yobel.optimus.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigationUtil {
    public static void cambiarVentana(Button botonReferencia, String fxmlPath, String titulo) {
        try {
            // Carga el recurso
            URL url = NavigationUtil.class.getResource(fxmlPath);
            if (url == null) {
                throw new IOException("No se encontró el archivo FXML en: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) botonReferencia.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Aquí centralizamos el mensaje de error para el usuario
            AlertUtil.mostrarError("Error de Navegación",
                    "No se pudo cargar la pantalla: " + fxmlPath + "\nDetalle: " + e.getMessage());
        }
    }

    /**
     * Carga una sub-vista dentro de un contenedor específico (Dashboard style)
     */
    public static <T> T cargarDentroDe(Pane contenedor, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Node view = loader.load();

            contenedor.getChildren().setAll(view);

            if (contenedor instanceof AnchorPane) {
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }

            // Retornamos el controlador para poder usarlo fuera
            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.mostrarError("Error", "No se pudo cargar: " + fxmlPath);
            return null;
        }
    }


}
