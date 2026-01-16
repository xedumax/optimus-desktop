package com.yobel.optimus.model.response;

import com.yobel.optimus.model.entity.Cuenta;
import com.yobel.optimus.model.entity.LineaProduccion;

import java.util.List;

public class LineaProduccionResponse {
    private List<LineaProduccion> data;
    private boolean success;
    private String message;

    // Getter para la lista de cuentas
    public List<LineaProduccion> getData() {
        return data;
    }

    // Setters y otros Getters (opcionales para depuración)
    public void setData(List<LineaProduccion> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}