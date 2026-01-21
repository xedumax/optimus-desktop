package com.yobel.optimus.service;

import com.google.gson.Gson;
import com.yobel.optimus.model.request.LecturaRequest;
import com.yobel.optimus.model.response.GenericResponse;
import com.yobel.optimus.util.AppContext;
import okhttp3.*;

import java.io.IOException;

public class LecturaEmpaqueService {

    private final OkHttpClient client;
    private final Gson gson = new Gson();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public LecturaEmpaqueService(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Registra el bulto/pedido en el API
     */
    public GenericResponse registrarBulto(String url, LecturaRequest lecturaRequest) throws IOException {
        String jsonBody = gson.toJson(lecturaRequest);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + AppContext.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Obtenemos el cuerpo usando tu método auxiliar
            String bodyString = getResponseBody(response);

            // Mapeamos a la respuesta genérica
            GenericResponse res = gson.fromJson(bodyString, GenericResponse.class);

            // Si el HTTP es error (400, 500), pero hay un body, lo devolvemos para ver el message.
            // Si el body es nulo, lanzamos la excepción habitual.
            if (res == null) {
                throw new IOException("Error HTTP " + response.code() + " sin respuesta del servidor.");
            }

            return res;
        }
    }

    // --- Métodos Privados de Soporte para evitar repetir código ---

    private Request buildGetRequest(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + AppContext.getToken())
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Optimus-JavaFX")
                .build();
    }

    private String getResponseBody(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Fallo en el servicio: " + response.code());
        }
        return (response.body() != null) ? response.body().string() : "";
    }
}