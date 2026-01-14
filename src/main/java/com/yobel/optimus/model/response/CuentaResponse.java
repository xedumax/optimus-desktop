package com.yobel.optimus.model.response;

import com.yobel.optimus.model.entity.Cuenta;

import java.util.List;

public class CuentaResponse {
    private List<Cuenta> data;
    private boolean success;
    private String message;

    // Getter para la lista de cuentas
    public List<Cuenta> getData() {
        return data;
    }

    // Setters y otros Getters (opcionales para depuración)
    public void setData(List<Cuenta> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}