package com.yobel.optimus.model.entity;

public class Agrupador {
    private String cuenta;
    private String agp;
    private String descripcion;

    // Getters para acceder a los datos
    public String getCuenta() { return cuenta; }
    public String getAgp() { return agp; }
    public String getDescripcion() { return descripcion; }

    // El método toString es lo que el usuario verá en el ComboBox
    @Override
    public String toString() {
        // Ejemplo: "AGC - LINEA C"
        return agp + " - " + descripcion;
    }
}