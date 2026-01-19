package com.yobel.optimus.model.dto;

public class EtiquetaDTO {
    // Datos de identificación y ubicación
    public String etqfpr, etqagp, etqzon, etqsct, etqter, etqctr, etqnro;
    public String etqnbr, etqdir, etqdsc, etqepq;

    // Datos de control y boleteo
    public String etqctd, etqblt, etqcor, etqA02;

    // Campos calculados que el API debe devolver listos para el ZPL
    public String nroEpq;    // Ej: "1 / 5"
    public String codBar;    // Código de barras completo
    public String corPedido; // Correlativo formateado

    public String getEtqfpr() {
        return etqfpr;
    }

    public void setEtqfpr(String etqfpr) {
        this.etqfpr = etqfpr;
    }

    public String getEtqagp() {
        return etqagp;
    }

    public void setEtqagp(String etqagp) {
        this.etqagp = etqagp;
    }

    public String getEtqzon() {
        return etqzon;
    }

    public void setEtqzon(String etqzon) {
        this.etqzon = etqzon;
    }

    public String getEtqsct() {
        return etqsct;
    }

    public void setEtqsct(String etqsct) {
        this.etqsct = etqsct;
    }

    public String getEtqter() {
        return etqter;
    }

    public void setEtqter(String etqter) {
        this.etqter = etqter;
    }

    public String getEtqctr() {
        return etqctr;
    }

    public void setEtqctr(String etqctr) {
        this.etqctr = etqctr;
    }

    public String getEtqnro() {
        return etqnro;
    }

    public void setEtqnro(String etqnro) {
        this.etqnro = etqnro;
    }

    public String getEtqnbr() {
        return etqnbr;
    }

    public void setEtqnbr(String etqnbr) {
        this.etqnbr = etqnbr;
    }

    public String getEtqdir() {
        return etqdir;
    }

    public void setEtqdir(String etqdir) {
        this.etqdir = etqdir;
    }

    public String getEtqdsc() {
        return etqdsc;
    }

    public void setEtqdsc(String etqdsc) {
        this.etqdsc = etqdsc;
    }

    public String getEtqepq() {
        return etqepq;
    }

    public void setEtqepq(String etqepq) {
        this.etqepq = etqepq;
    }

    public String getEtqctd() {
        return etqctd;
    }

    public void setEtqctd(String etqctd) {
        this.etqctd = etqctd;
    }

    public String getEtqblt() {
        return etqblt;
    }

    public void setEtqblt(String etqblt) {
        this.etqblt = etqblt;
    }

    public String getEtqcor() {
        return etqcor;
    }

    public void setEtqcor(String etqcor) {
        this.etqcor = etqcor;
    }

    public String getEtqA02() {
        return etqA02;
    }

    public void setEtqA02(String etqA02) {
        this.etqA02 = etqA02;
    }

    public String getNroEpq() {
        return nroEpq;
    }

    public void setNroEpq(String nroEpq) {
        this.nroEpq = nroEpq;
    }

    public String getCodBar() {
        return codBar;
    }

    public void setCodBar(String codBar) {
        this.codBar = codBar;
    }

    public String getCorPedido() {
        return corPedido;
    }

    public void setCorPedido(String corPedido) {
        this.corPedido = corPedido;
    }
}