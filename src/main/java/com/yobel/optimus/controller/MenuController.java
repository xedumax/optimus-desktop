package com.yobel.optimus.controller;

import com.yobel.optimus.model.entity.SystemItem;
import com.yobel.optimus.service.AuthService;
import com.yobel.optimus.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class MenuController {

    @FXML private AnchorPane contentArea;
    @FXML private Label lblUsuarioSesion;
    @FXML private Label lblEntornoSesion;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        String usuario = AppContext.getUsuario();
        String entorno = AppContext.getAmbiente();

        lblUsuarioSesion.setText(usuario != null ? usuario.toUpperCase() : AppConstants.USUARIO_SESION);
        lblEntornoSesion.setText(entorno != null ? entorno : AppConstants.USUARIO_ENTORNO_DEV);
    }

    @FXML
    private void abrirLecturaEmpaques(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                // Validación de permisos
                List<SystemItem> listaSistemas = authService.getSystems(AppConfig.Auth.sistemas());
                boolean tienePermiso = listaSistemas.stream()
                        .anyMatch(s -> AppConstants.SYSTEM_NAME_OHS.equalsIgnoreCase(s.getSystemName()));

                if (tienePermiso) {
                    //Éxito: Usamos NavigationUtil para inyectar la vista
                    Platform.runLater(() -> {
                        // 1. Cargamos la vista y obtenemos su controlador
                        LecturaEmpaqueController controller = NavigationUtil.cargarDentroDe(contentArea, ViewConfig.LECTURA_EMPAQUE);

                        // 2. Pasamos la referencia del contenedor al hijo
                        if (controller != null) {
                            controller.setMainContentArea(contentArea);
                        }
                    });
                } else {
                    // 3. Fallo de permisos: Usamos AlertUtil
                    Platform.runLater(() -> AlertUtil.mostrarAdvertencia("Acceso Denegado", "No tiene permisos para el módulo OHS."));
                }
            } catch (IOException e) {
                // 4. Error de red: Usamos AlertUtil
                    Platform.runLater(() -> AlertUtil.mostrarError("Error de Conexión", "No se pudo validar el acceso: " + e.getMessage()) );
            }
        }).start();
    }

}