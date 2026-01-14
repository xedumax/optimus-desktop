package com.yobel.optimus.model.entity;

public class Ventana {
    private String codCuenta;
    private String agpCuenta;
    private String nroVentana;
    private String desVentana;
    private String estado;

    // Getters
    public String getCodCuenta() { return codCuenta; }
    public String getAgpCuenta() { return agpCuenta; }
    public String getNroVentana() { return nroVentana; }
    public String getDesVentana() { return desVentana; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return nroVentana + " - " + desVentana;
    }
}