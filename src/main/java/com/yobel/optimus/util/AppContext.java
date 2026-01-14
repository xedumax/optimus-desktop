package com.yobel.optimus.util;

/**
 * Clase para gestionar el estado global de la aplicación (Singleton-like).
 * Almacena información necesaria para las peticiones HTTP entre diferentes módulos.
 */
public class AppContext {
    private static String token;
    private static String baseUrl;

    public static String getToken() {
        return token;
    }
    public static void setToken(String newToken) {
        if (newToken != null) {
            // Limpia comillas accidentales y espacios
            token = newToken.replace("\"", "").trim();
        } else {
            token = null;
        }
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String newBaseUrl) {
        // Aseguramos que la URL no termine en "/" para evitar doble diagonal en las rutas
        if (newBaseUrl != null && newBaseUrl.endsWith("/")) {
            baseUrl = newBaseUrl.substring(0, newBaseUrl.length() - 1);
        } else {
            baseUrl = newBaseUrl;
        }
    }

    /**
     * Limpia todos los datos de la sesión actual (útil para Logout).
     */
    public static void clearSession() {
        token = null;
        baseUrl = null;
    }
}