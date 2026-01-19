package com.yobel.optimus.model.entity;

public class ConfigEtiqueta {
    private String vFlgCorr; // Reemplaza rs2.getString(6) de AIPDXP
    private String vTipProc;  // Reemplaza rs3.getString(1) de AIPCIA
    private String vFlgOrd;   // Reemplaza rs1.getString(45) de AIPETQ
    private String vArch;     // El nombre de la vista (AIPETQLx) calculado por el API

    // Getters y Setters
    public String getvFlgCorr() { return vFlgCorr; }
    public void setvFlgCorr(String vFlgCorr) { this.vFlgCorr = vFlgCorr; }

    public String getvTipProc() { return vTipProc; }
    public void setvTipProc(String vTipProc) { this.vTipProc = vTipProc; }

    public String getvFlgOrd() { return vFlgOrd; }
    public void setvFlgOrd(String vFlgOrd) { this.vFlgOrd = vFlgOrd; }

    public String getvArch() { return vArch; }
    public void setvArch(String vArch) { this.vArch = vArch; }
}