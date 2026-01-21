package com.yobel.optimus.service;

import com.google.gson.Gson;
import com.yobel.optimus.model.entity.*;
import com.yobel.optimus.model.response.*;
import com.yobel.optimus.util.AppContext;
import okhttp3.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MaestroService {

    private final OkHttpClient client;
    private final Gson gson = new Gson();

    public MaestroService(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Obtiene cuentas filtrando por tipo '1' usando la URL pasada por AppConfig
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
    * Obtiene Lineas de Produccion | Parámetro: Código de Cuenta y AGP seleccionado previamente
    */
    public List<LineaProduccion> getLineasProduccion(String url) throws IOException {
        Request request = buildGetRequest(url);
        try (Response response = client.newCall(request).execute()) {
            String body = getResponseBody(response);
            LineaProduccionResponse res = gson.fromJson(body, LineaProduccionResponse.class);
            return (res != null && res.getData() != null) ? res.getData() : Collections.emptyList();
        }
    }

    public List<InfoEtiqueta> getInfoEtiquetas(String url) throws IOException {
        Request request = buildGetRequest(url);
        try (Response response = client.newCall(request).execute()) {
            String body = getResponseBody(response);
            InfoEtiquetaResponse res = gson.fromJson(body, InfoEtiquetaResponse.class);
            return (res != null && res.getData() != null) ? res.getData() : Collections.emptyList();
        }
    }

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
        if (!response.isSuccessful()) throw new IOException("Error HTTP " + response.code());
        return (response.body() != null) ? response.body().string() : "";
    }

}