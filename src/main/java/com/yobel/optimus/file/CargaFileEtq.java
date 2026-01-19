package com.yobel.optimus.file;

import com.yobel.optimus.util.AppConfig;

public abstract class CargaFileEtq {
    protected String vCia;
    protected String vDIRFil;
    protected String vNomFil;

    public CargaFileEtq() {
        // Tomamos la ruta definida en tu AppConfig actualizado
        this.vDIRFil = AppConfig.PATH_ETIQUETAS;
    }

    public void setvCia(String vCia) { this.vCia = vCia; }
    public void setvNomFil(String vNomFil) { this.vNomFil = vNomFil; }

    public abstract void salvaFile(String apiUrl) throws Exception;
}