package com.yobel.optimus.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private TextField txtClaveVisible;
    @FXML private ToggleButton btnVerClave;
    @FXML private Label lblIconoOjo;
    @FXML private Button btnIniciar;

    @FXML
    public void initialize() {
        // Vincula los textos de ambos campos para que siempre sean iguales
        txtClaveVisible.textProperty().bindBidirectional(txtClave.textProperty());
    }

    @FXML
    private void togglePassword() {
        if (btnVerClave.isSelected()) {
            txtClaveVisible.setVisible(true);
            txtClave.setVisible(false);
            lblIconoOjo.setText("🔒"); // O cambia por el icono de ojo tachado
        } else {
            txtClaveVisible.setVisible(false);
            txtClave.setVisible(true);
            lblIconoOjo.setText("👁");
        }
    }

    @FXML
    protected void onLoginClick() {
        String user = txtUsuario.getText();
        String pass = txtClave.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Campos Requeridos", "Por favor, complete todos los campos marcados con *.");
        } else {
            // Aquí va la lógica de autenticación (DB o API)
            if (user.equals("admin") && pass.equals("1234")) {
                System.out.println("Acceso Correcto");

                try {
                    // 1. Cargar el FXML del menú
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
                    Parent root = loader.load();

                    // 2. Obtener el Stage actual usando el botón de login
                    Stage stage = (Stage) btnIniciar.getScene().getWindow();

                    // 3. Configurar scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Optimus - Herramientas");
                    stage.setResizable(true);
                    stage.centerOnScreen();
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    mostrarAlerta("Error", "No se pudo cargar el menú principal.");
                }

            } else {
                mostrarAlerta("Error", "Usuario o clave incorrectos.");
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private String getPassword() {
        return txtClave.getText();
    }
}
