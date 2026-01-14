package com.yobel.optimus.service;

import com.google.gson.Gson;
import com.yobel.optimus.model.entity.Agrupador;
import com.yobel.optimus.model.entity.Cuenta;
import com.yobel.optimus.model.entity.Ventana;
import com.yobel.optimus.model.request.LecturaRequest;
import com.yobel.optimus.model.response.AgrupadorResponse;
import com.yobel.optimus.model.response.CuentaResponse;
import com.yobel.optimus.model.response.VentanaResponse;
import com.yobel.optimus.util.AppContext;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LecturaEmpaqueService {

    private final OkHttpClient client;
    private final Gson gson = new Gson();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public LecturaEmpaqueService(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Obtiene la lista de agrupadores usando la URL proporcionada
     */
    public List<Agrupador> getAgrupadores(String url) throws IOException {
        Request request = buildGetRequest(url);

        try (Response response = client.newCall(request).execute()) {
            String body = getResponseBody(response);
            AgrupadorResponse res = gson.fromJson(body, AgrupadorResponse.class);
            return (res != null && res.getData() != null) ? res.getData() : Collections.emptyList();
        }
    }

    /**
     * Obtiene cuentas filtrando por tipo '1'
     */
    public List<Cuenta> getCuentas(String url) throws IOException {
        Request request = buildGetRequest(url);

        try (Response response = client.newCall(request).execute()) {
            String body = getResponseBody(response);
            CuentaResponse res = gson.fromJson(body, CuentaResponse.class);

            if (res != null && res.getData() != null) {
                return res.getData().stream()
                        .filter(c -> "1".equals(c.getCtaTipo()))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene ventanas para una cuenta específica
     */
    public List<Ventana> getVentanas(String url) throws IOException {
        Request request = buildGetRequest(url);

        try (Response response = client.newCall(request).execute()) {
            String body = getResponseBody(response);
            VentanaResponse res = gson.fromJson(body, VentanaResponse.class);
            return (res != null && res.getData() != null) ? res.getData() : Collections.emptyList();
        }
    }

    /**
     * Registra el bulto/pedido en el API
     */
    public void registrarBulto(String url, LecturaRequest lecturaRequest) throws IOException {
        String jsonBody = gson.toJson(lecturaRequest);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + AppContext.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error HTTP " + response.code() + " al registrar bulto.");
            }
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