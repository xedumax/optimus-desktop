package com.yobel.optimus.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yobel.optimus.service.AuthService;
import com.yobel.optimus.util.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LoginController {
    @FXML private Pane rootContainer; // O VBox, según tu raíz
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private TextField txtClaveVisible;
    @FXML private ToggleButton btnVerClave;
    @FXML private Label lblIconoOjo;
    @FXML private Button btnIniciar;
    @FXML private ComboBox<String> cbAmbiente;

    // Definimos la pseudo-clase como una constante al inicio de la clase
    private static final PseudoClass MOSTRANDO_CLAVE = PseudoClass.getPseudoClass("mostrando");

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        Platform.runLater(() -> rootContainer.requestFocus());

        //Inicializando valores para pruebas - PENDIENTE
        txtUsuario.setText("pejefeprod");
        txtClave.setText("yobelscm2025");

        // Configurar opciones del combo de entornos
        cbAmbiente.setItems(FXCollections.observableArrayList("DEV", "QAS", "PROD"));
        // Vincula los textos de ambos campos para que siempre sean iguales
        txtClaveVisible.textProperty().bindBidirectional(txtClave.textProperty());
    }

    @FXML
    private void togglePassword() {
        boolean ver = btnVerClave.isSelected();
        txtClaveVisible.setVisible(ver);
        txtClave.setVisible(!ver);
        // 2. Lógica visual: Solo le decimos al Label si el estado "mostrando" es verdadero o falso
        lblIconoOjo.pseudoClassStateChanged(MOSTRANDO_CLAVE, ver);
    }

    @FXML
    protected void onLoginClick() {
        String user = txtUsuario.getText().trim();
        String pass = txtClave.getText().trim();
        String session = AppContext.getSession();
        String ambienteSeleccionado = cbAmbiente.getValue();

        // 1. Validar campos básicos antes de intentar el login
        if (user.isEmpty() || pass.isEmpty()) {
            AlertUtil.mostrarAdvertencia("Campos Requeridos", "Por favor, ingrese usuario y contraseña.");
            return;
        }

        if (ambienteSeleccionado == null || ambienteSeleccionado.isEmpty()) {
            AlertUtil.mostrarAdvertencia("Entorno Requerido", "Debe seleccionar un entorno (DEV, QAS o PROD) para continuar.");
            return;
        }

        // 2. Establecer el ambiente ANTES de obtener la URL del endpoint
        AppContext.setAmbiente(ambienteSeleccionado);

        // Ahora AppConfig.Auth.login() sabrá exactamente a qué servidor apuntar
        String loginUrl = AppConfig.Auth.login();

        new Thread(() -> {
            try {
                // 3. Ejecutar Login a través del servicio
                String resultado = authService.login(loginUrl, user, pass, session);

                // 4. Parsear y Limpiar Token
                JsonObject jsonObject = JsonParser.parseString(resultado).getAsJsonObject();

                if (jsonObject.has("token") && !jsonObject.get("token").isJsonNull()) {
                    String tokenRaw = jsonObject.get("token").getAsString();

                    // Limpieza de seguridad: remueve comillas y caracteres ocultos
                    String cleanToken = tokenRaw.replaceAll("[^\\x20-\\x7E]", "").replace("\"", "").trim();

                    // 5. Guardar sesión en el contexto global
                    AppContext.setToken(cleanToken);
                    AppContext.setUsuario(user);

                    System.out.println("Login exitoso en entorno: " + AppContext.getAmbiente());

                    // 6. Navegar al menú principal
                    Platform.runLater(this::irAlMenu);
                } else {
                    Platform.runLater(() -> AlertUtil.mostrarError("Error de Autenticación", "Respuesta del servidor inválida."));
                }

            } catch (IOException e) {
                // Maneja errores de red o credenciales incorrectas (401/403)
                Platform.runLater(() -> {
                    String errorMsg = e.getMessage().contains("401")
                            ? "Usuario o clave incorrectos."
                            : "Error de conexión: " + e.getMessage();
                    AlertUtil.mostrarError("Acceso Fallido", errorMsg);
                });
            }
        }).start();
    }

    private void irAlMenu() {
        Platform.runLater(() ->
                NavigationUtil.cambiarVentana(btnIniciar, ViewConfig.MENU, "Optimus - Menú")
        );
    }
}
