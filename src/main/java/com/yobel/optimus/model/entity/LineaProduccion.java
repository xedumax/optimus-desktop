package com.yobel.optimus.model.entity;

public class LineaProduccion {
    private String cuenta;
    private String agp;
    private String lpr;
    private String descripcion;

    // Getters y Setters
    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }
    public String getAgp() { return agp; }
    public void setAgp(String agp) { this.agp = agp; }
    public String getLpr() { return lpr; }
    public void setLpr(String lpr) { this.lpr = lpr; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return descripcion; // Muestra "LPR 1(VTN4)" en el combo
    }
}
