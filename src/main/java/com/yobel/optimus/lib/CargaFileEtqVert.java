package com.yobel.optimus.lib;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;

import com.yobel.optimus.file.CargaFileEtq;
import com.yobel.optimus.file.FileEscribe;
import com.yobel.optimus.model.dto.EtiquetaDTO;
import com.yobel.optimus.model.entity.ConfigEtiqueta;
import okhttp3.*; // Usando OkHttp según tu ejemplo

public class CargaFileEtqVert extends CargaFileEtq {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    public void salvaFile(String apiUrl) throws Exception {
        // 1. Reemplazo de rs1, rs2, rs3 (Consultas de configuración/vistas)
        // Se consumen servicios específicos pasando el URL de AppConfig

        // 2. Reemplazo del ResultSet principal (Datos de impresión)
        List<EtiquetaDTO> datosImpresion;
    }


    private void generarZPL(List<EtiquetaDTO> lista, ConfigEtiqueta config) throws IOException {
        FileEscribe f = new FileEscribe(vDIRFil, vNomFil);
        for (EtiquetaDTO item : lista) {
            // Lógica ZPL idéntica a la original pero usando el DTO
            f.escribirTexto("^XA");
            f.nuevaLinea();

            // Ejemplo: Usar datos del DTO en lugar del ResultSet
            f.escribirTexto("^fo240,30^a0n,30,30^fd" + item.etqdsc + "^FS");
            f.nuevaLinea();

            // Si el flagi de correlativo viene del servicio de configuracón
            if ("S".equals(config.getvFlgCorr())) {
                f.escribirTexto("^fo40,680^a0n,130,130^fd" + item.getCorPedido() + "^FS");
                f.nuevaLinea();
            }

            f.escribirTexto("^xz");
            f.nuevaLinea();
        }
        f.cerrarStream();
    }
}