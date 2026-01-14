package com.yobel.optimus.util;

/**
 * Clase para gestionar el estado global de la aplicación (Singleton-like).
 * Almacena información necesaria para las peticiones HTTP entre diferentes módulos.
 */
public class AppContext {
    private static String token;
    private static String usuario;  // Para el campo userModif
    private static String ambiente; // DEV, QAS, PROD
    private static String baseUrl;
    private static String session = "3332323232";

    // Métodos para el Token
    public static String getToken() { return token; }
    public static void setToken(String token) { AppContext.token = token; }

    // Métodos para el Usuario (Nuevo)
    public static String getUsuario() { return usuario; }
    public static void setUsuario(String usuario) { AppContext.usuario = usuario; }

    // Métodos para el Ambiente (Nuevo)
    public static String getAmbiente() { return ambiente; }
    public static void setAmbiente(String ambiente) { AppContext.ambiente = ambiente; }

    // Métodos para la URL Base
    public static String getBaseUrl() { return baseUrl; }
    public static void setBaseUrl(String baseUrl) { AppContext.baseUrl = baseUrl; }

    // Metodos para Session
    public static String getSession() { return session; }
    public static void setSession(String session) { AppContext.session = session; }

    // Limpiar contexto al cerrar sesión
    public static void clear() {
        token = null;
        usuario = null;
        ambiente = null;
        baseUrl = null;
    }
}