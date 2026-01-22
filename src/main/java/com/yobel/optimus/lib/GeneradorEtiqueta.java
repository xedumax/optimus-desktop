package com.yobel.optimus.lib;

import com.yobel.optimus.model.entity.InfoEtiqueta;

import java.io.IOException;
import java.util.List;

public interface GeneradorEtiqueta {
    void generar(List<InfoEtiqueta> data, String directorio, String nombreArchivo) throws IOException;
}