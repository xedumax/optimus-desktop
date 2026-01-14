package com.yobel.optimus.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yobel.optimus.service.AuthService;
import com.yobel.optimus.util.AppContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
    @FXML private ComboBox<String> cbAmbiente;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Configurar opciones del combo
        cbAmbiente.setItems(FXCollections.observableArrayList("DEV", "QAS", "PROD"));
        cbAmbiente.setValue("DEV"); // Por defecto
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
        String ambiente = cbAmbiente.getValue();


        String baseUrl = switch (ambiente) {
            case "QAS" -> "https://optimus-qas.yobel.biz";
            case "PROD" -> "https://optimus.yobel.biz";
            default -> "https://optimus-dev.yobel.biz";
        };

        String loginUrl = baseUrl + "/api/msi/autorizacion/auth/login";

        new Thread(() -> {
            try {
                // 1. Ejecutar Login
                String resultado = authService.login(loginUrl, user, pass);
                String token = null;

                // 2. Extraer Token
                JsonObject jsonObject = JsonParser.parseString(resultado).getAsJsonObject();
                if (jsonObject.has("token")) {
                    token = jsonObject.get("token").getAsString();
                    AppContext.setToken(token);
                }

                if (token != null) {
                    // 3. Guardar token en el contexto global (AppContext)
                    AppContext.setToken(token);
                    AppContext.setBaseUrl(baseUrl);

                    // 4. Ir al menú principal de inmediato
                    Platform.runLater(this::irAlMenu);
                }

            } catch (IOException e) {
                // Maneja errores 401 (Credenciales mal) o problemas de red
                Platform.runLater(() -> mostrarAlerta("Acceso Fallido", e.getMessage()));
            }
        }).start();
    }

    private void irAlMenu() {
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
