package com.yobel.optimus.service;

import com.google.gson.JsonSyntaxException;
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
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + AppContext.getToken())
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) throw new IOException("Respuesta del servidor vacía");

            String responseBodyText = response.body().string();

            if (!response.isSuccessful()) {
                // LOG CRÍTICO: Aquí verás si el API dice "Token Expired" o "Invalid URL"
                System.err.println("DEBUG SYSTEMS - URL: " + url);
                System.err.println("DEBUG SYSTEMS - CODE: " + response.code());
                System.err.println("DEBUG SYSTEMS - RESPONSE: " + responseBodyText);
                throw new IOException("Error " + response.code() + ": " + responseBodyText);
            }

            // 2. Mapeo flexible
            SystemResponse res = gson.fromJson(responseBodyText, SystemResponse.class);

            if (res == null || res.getSystems() == null) {
                System.err.println("ERROR: El JSON no coincide con la clase SystemResponse. JSON: " + responseBodyText);
                return java.util.Collections.emptyList();
            }

            return res.getSystems();
        } catch (JsonSyntaxException e) {
            throw new IOException("Error al parsear el JSON de sistemas: " + e.getMessage());
        }
    }


}