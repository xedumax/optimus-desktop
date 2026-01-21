package com.yobel.optimus.model.response;

import com.yobel.optimus.model.entity.InfoEtiqueta;
import java.util.List;

public class InfoEtiquetaResponse {
    private List<InfoEtiqueta> data;
    private boolean success;

    // Getters
    public List<InfoEtiqueta> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    // Setters
    public void setData(List<InfoEtiqueta> data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}