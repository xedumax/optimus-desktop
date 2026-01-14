package com.yobel.optimus.service;

import com.google.gson.Gson;
import com.yobel.optimus.model.entity.Agrupador;
import com.yobel.optimus.model.entity.Cuenta;
import com.yobel.optimus.model.entity.Ventana;
import com.yobel.optimus.model.request.LecturaRequest;
import com.yobel.optimus.model.response.AgrupadorResponse;
import com.yobel.optimus.model.response.CuentaResponse;
import com.yobel.optimus.model.response.VentanaResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class BarcodeService {

    private final OkHttpClient client;
    private final Gson gson = new Gson();

    public BarcodeService(OkHttpClient client) {
        this.client = client;
    }

    public List<Agrupador> getAgrupadores(String token) throws IOException {
        String url = "https://optimus-dev.yobel.biz/api/prd/matriz/agrupadorProduccion/listSel/001";

        // Limpieza profunda para asegurar que la firma del token no se rompa
        String cleanToken = (token != null)
                ? token.replaceAll("[^\\x20-\\x7E]", "").replace("\"", "").trim()
                : "";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + cleanToken)
                .addHeader("User-Agent", "PostmanRuntime/7.40.0")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = (response.body() != null) ? response.body().string() : "";

            if (!response.isSuccessful()) {
                System.err.println("--- Error en Servicio Agrupadores ---");
                System.err.println("Código: " + response.code());
                System.err.println("Cuerpo: " + body);
                throw new IOException("HTTP " + response.code());
            }

            AgrupadorResponse res = gson.fromJson(body, AgrupadorResponse.class);

            // Verificación de nulidad para evitar NullPointerException en el Controller
            if (res != null && res.getData() != null) {
                return res.getData();
            } else {
                return Collections.emptyList();
            }
        }
    }

    public List<Cuenta> getCuentas(String token) throws IOException {
        String url = "https://optimus-dev.yobel.biz/api/dyt/gestion-cuentas/listAll";

        String cleanToken = (token != null)
                ? token.replaceAll("[^\\x20-\\x7E]", "").replace("\"", "").trim()
                : "";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + cleanToken)
                .addHeader("User-Agent", "PostmanRuntime/7.40.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = (response.body() != null) ? response.body().string() : "";

            if (!response.isSuccessful()) throw new IOException("Error HTTP " + response.code());

            CuentaResponse res = gson.fromJson(body, CuentaResponse.class);

            if (res != null && res.getData() != null) {
                // APLICAR FILTRO: Solo Tipo Cuenta = '1'
                return res.getData().stream()
                        .filter(c -> "1".equals(c.getCtaTipo()))
                        .collect(java.util.stream.Collectors.toList());
            }

            return java.util.Collections.emptyList();
        }
    }

    public List<Ventana> getVentanas(String token, String codigoCuenta) throws IOException {
        String url = "https://optimus-dev.yobel.biz/api/dyt/gestion-maestros/ventanaDsp/list/" + codigoCuenta;

        String cleanToken = (token != null) ? token.replace("\"", "").trim() : "";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + cleanToken)
                .addHeader("User-Agent", "PostmanRuntime/7.40.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            if (!response.isSuccessful()) throw new IOException("Error Ventanas: " + response.code());

            VentanaResponse res = gson.fromJson(body, VentanaResponse.class);
            return (res != null && res.getData() != null) ? res.getData() : new java.util.ArrayList<>();
        }
    }

    public void registrarBulto(String token, LecturaRequest request) throws IOException, InterruptedException {
        String url = "https://optimus-dev.yobel.biz/api/prd/procesoregistroinfo/procesoPedidos/capturaBultos";

        String jsonBody = new Gson().toJson(request);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new IOException("Error del servidor: " + response.statusCode() + " - " + response.body());
        }
    }

}