package com.yobel.optimus.model.entity;

public class Cuenta {
    private String ctaCodigo;
    private String ctaCodDesc;
    private String ctaTipo; // '1' para Filtro
    private String ctaAgpDflt; // AGP por defecto

    // Getters
    public String getCtaCodigo() { return ctaCodigo; }
    public String getCtaCodDesc() { return ctaCodDesc; }
    public String getCtaTipo() { return ctaTipo; }
    public String getCtaAgpDflt() { return ctaAgpDflt; }

    // El método toString es lo que el usuario verá en el ComboBox
    @Override
    public String toString() {
        // Formato para el combo: Codigo - Descripcion
        return ctaCodigo + " - " + ctaCodDesc;
    }
}