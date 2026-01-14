package com.yobel.optimus.service;

import com.yobel.optimus.model.entity.SystemItem;
import com.yobel.optimus.model.response.SystemResponse;
import com.yobel.optimus.util.AppContext;
import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class AuthService {
    private final OkHttpClient client;
    private final Gson gson = new Gson();

    public AuthService() {
        this.client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String token = AppContext.getToken();

                    if (token != null && !token.isEmpty()) {
                        // Limpieza profunda: quitamos cualquier rastro de comillas o espacios
                        String cleanToken = token.replace("\"", "").trim();

                        Request requestWithToken = originalRequest.newBuilder()
                                .header("Authorization", "Bearer " + cleanToken)
                                .header("Accept", "application/json")
                                .header("Connection", "keep-alive") // Algunos servidores lo requieren
                                .build();

                        return chain.proceed(requestWithToken);
                    }
                    return chain.proceed(originalRequest);
                })
                .build();
    }

    public String login(String url, String username, String password, String session) throws IOException {
        // 1. Crear el cuerpo de la petición en formato JSON
        Map<String, String> data = Map.of(
                "username", username,
                "password", password,
                "session" , session
        );
        String json = gson.toJson(data);

        RequestBody body = RequestBody.create(
                json, MediaType.get("application/json; charset=utf-8")
        );

        // 2. Construir la petición POST
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 3. Ejecutar y obtener respuesta
        try (Response response = client.newCall(request).execute()) {
            // Manejo de errores específicos (ej. 401 Unauthorized)
            if (!response.isSuccessful()) {
                throw new IOException("Usuario no valido. Revisar credenciales.");
            }

            if (response.body() == null) throw new IOException("Respuesta vacía del servidor");

            byte[] bytes = response.body().bytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    //SERVICIO A REVISAR SE SALTEA EL PASO - PENDIENTE

    public List<SystemItem> getSystems(String url) throws IOException {
        java.net.URL urlObj = new java.net.URL(url);

        // OBTENEMOS Y LIMPIAMOS EL TOKEN IGUAL QUE EN EL MÉTODO QUE SÍ FUNCIONA
        String tokenRaw = AppContext.getToken();
        String cleanToken = (tokenRaw != null)
                ? tokenRaw.replaceAll("[^\\x20-\\x7E]", "").replace("\"", "").trim()
                : "";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + cleanToken) // <--- Usar el token limpio
                .addHeader("User-Agent", "PostmanRuntime/7.40.0")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBodyText = (response.body() != null) ? response.body().string() : "";

            if (!response.isSuccessful()) {
                // Imprime el cuerpo para saber por qué falló (ej. Token expirado o mal formado)
                System.err.println("Error en getSystems: " + response.code() + " Body: " + responseBodyText);
                throw new IOException("Error del servidor (" + response.code() + ")");
            }

            SystemResponse res = gson.fromJson(responseBodyText, SystemResponse.class);

            return (res != null && res.getSystems() != null)
                    ? res.getSystems()
                    : java.util.Collections.emptyList();
        }
    }


}