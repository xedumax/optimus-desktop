package com.yobel.optimus.model.request;

public class LecturaRequest {
    private String cuenta;
    private String codAgp;
    private String pedido;
    private String fecModif;
    private String userModif;
    private String fechaAutomatico;
    private String origen;

    // Constructor, Getters y Setters
    public LecturaRequest(String cuenta, String codAgp, String pedido, String fecModif,
                          String userModif, String fechaAutomatico, String origen) {
        this.cuenta = cuenta;
        this.codAgp = codAgp;
        this.pedido = pedido;
        this.fecModif = fecModif;
        this.userModif = userModif;
        this.fechaAutomatico = fechaAutomatico;
        this.origen = origen;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCodAgp() {
        return codAgp;
    }

    public void setCodAgp(String codAgp) {
        this.codAgp = codAgp;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getFecModif() {
        return fecModif;
    }

    public void setFecModif(String fecModif) {
        this.fecModif = fecModif;
    }

    public String getUserModif() {
        return userModif;
    }

    public void setUserModif(String userModif) {
        this.userModif = userModif;
    }

    public String getFechaAutomatico() {
        return fechaAutomatico;
    }

    public void setFechaAutomatico(String fechaAutomatico) {
        this.fechaAutomatico = fechaAutomatico;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}