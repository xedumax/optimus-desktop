package com.yobel.optimus.model.entity;

public class InfoEtiqueta {

    private String etqCuenta;
    private String etqFchProceso;
    private int etqLoteProceso;
    private String etqCodAgp;
    private String etqZonaCliente;
    private String etqSubCtaCliente;
    private String etqCtrPedido;
    private String etqNroPedido;
    private int etqSecPedido;
    private String etqCodCliente;
    private String etqNombreCliente;
    private String etqDireccCliente;
    private String etqCodLpr;
    private String etqDescLpr;
    private int etqCorrEmpaq;
    private String etqTotBultos;
    private String etqTipoEmpaque;
    private String etqCategoria;
    private String etqSiCliNueva;
    private int etqTotUnidades;
    private int etqTotItems;

    // --- CAMPOS DECIMALES ---
    private double etqPeso;    // Cambiado a double para soportar decimales
    private double etqVolumen; // Mantenido como double
    // ------------------------

    private String etqCampana;
    private String etqDistrito;
    private int etqSecRuta;
    private int etqSecZona;
    private int etqNroColumna;
    private String etqRuta;
    private String etqTipLpr;
    private String etqFlgSecuencia;
    private String etqDescClasifCli;
    private String etqDescProducto;
    private String etqFchaProduc;
    private String etqFchaDespacho;
    private String etqTlfnoCliente;
    private String etqVentanaDes;
    private String etqDigitosPed;
    private int etqSecEntrega;
    private int etqCorrBltoAgp;
    private String etqRemesaAsig;
    private String etqRangosAnaqpp;
    private String etqRuteoEsquema;
    private String etqRuteoSecProd;
    private String etqRuteoVehiculo;
    private String etqRuteoZonaRep;
    private String etqFlgOrden;
    private String etqFlgOrienta;
    private String etqStatus;
    private String etqRegFcha;
    private String etqRegUser;

    public InfoEtiqueta() {}

    public String getEtqCuenta() {
        return etqCuenta;
    }

    public void setEtqCuenta(String etqCuenta) {
        this.etqCuenta = etqCuenta;
    }

    public String getEtqFchProceso() {
        return etqFchProceso;
    }

    public void setEtqFchProceso(String etqFchProceso) {
        this.etqFchProceso = etqFchProceso;
    }

    public int getEtqLoteProceso() {
        return etqLoteProceso;
    }

    public void setEtqLoteProceso(int etqLoteProceso) {
        this.etqLoteProceso = etqLoteProceso;
    }

    public String getEtqCodAgp() {
        return etqCodAgp;
    }

    public void setEtqCodAgp(String etqCodAgp) {
        this.etqCodAgp = etqCodAgp;
    }

    public String getEtqZonaCliente() {
        return etqZonaCliente;
    }

    public void setEtqZonaCliente(String etqZonaCliente) {
        this.etqZonaCliente = etqZonaCliente;
    }

    public String getEtqSubCtaCliente() {
        return etqSubCtaCliente;
    }

    public void setEtqSubCtaCliente(String etqSubCtaCliente) {
        this.etqSubCtaCliente = etqSubCtaCliente;
    }

    public String getEtqCtrPedido() {
        return etqCtrPedido;
    }

    public void setEtqCtrPedido(String etqCtrPedido) {
        this.etqCtrPedido = etqCtrPedido;
    }

    public String getEtqNroPedido() {
        return etqNroPedido;
    }

    public void setEtqNroPedido(String etqNroPedido) {
        this.etqNroPedido = etqNroPedido;
    }

    public int getEtqSecPedido() {
        return etqSecPedido;
    }

    public void setEtqSecPedido(int etqSecPedido) {
        this.etqSecPedido = etqSecPedido;
    }

    public String getEtqCodCliente() {
        return etqCodCliente;
    }

    public void setEtqCodCliente(String etqCodCliente) {
        this.etqCodCliente = etqCodCliente;
    }

    public String getEtqNombreCliente() {
        return etqNombreCliente;
    }

    public void setEtqNombreCliente(String etqNombreCliente) {
        this.etqNombreCliente = etqNombreCliente;
    }

    public String getEtqDireccCliente() {
        return etqDireccCliente;
    }

    public void setEtqDireccCliente(String etqDireccCliente) {
        this.etqDireccCliente = etqDireccCliente;
    }

    public String getEtqCodLpr() {
        return etqCodLpr;
    }

    public void setEtqCodLpr(String etqCodLpr) {
        this.etqCodLpr = etqCodLpr;
    }

    public String getEtqDescLpr() {
        return etqDescLpr;
    }

    public void setEtqDescLpr(String etqDescLpr) {
        this.etqDescLpr = etqDescLpr;
    }

    public int getEtqCorrEmpaq() {
        return etqCorrEmpaq;
    }

    public void setEtqCorrEmpaq(int etqCorrEmpaq) {
        this.etqCorrEmpaq = etqCorrEmpaq;
    }

    public String getEtqTotBultos() {
        return etqTotBultos;
    }

    public void setEtqTotBultos(String etqTotBultos) {
        this.etqTotBultos = etqTotBultos;
    }

    public String getEtqTipoEmpaque() {
        return etqTipoEmpaque;
    }

    public void setEtqTipoEmpaque(String etqTipoEmpaque) {
        this.etqTipoEmpaque = etqTipoEmpaque;
    }

    public String getEtqCategoria() {
        return etqCategoria;
    }

    public void setEtqCategoria(String etqCategoria) {
        this.etqCategoria = etqCategoria;
    }

    public String getEtqSiCliNueva() {
        return etqSiCliNueva;
    }

    public void setEtqSiCliNueva(String etqSiCliNueva) {
        this.etqSiCliNueva = etqSiCliNueva;
    }

    public int getEtqTotUnidades() {
        return etqTotUnidades;
    }

    public void setEtqTotUnidades(int etqTotUnidades) {
        this.etqTotUnidades = etqTotUnidades;
    }

    public int getEtqTotItems() {
        return etqTotItems;
    }

    public void setEtqTotItems(int etqTotItems) {
        this.etqTotItems = etqTotItems;
    }

    public double getEtqPeso() {
        return etqPeso;
    }

    public void setEtqPeso(double etqPeso) {
        this.etqPeso = etqPeso;
    }

    public double getEtqVolumen() {
        return etqVolumen;
    }

    public void setEtqVolumen(double etqVolumen) {
        this.etqVolumen = etqVolumen;
    }

    public String getEtqCampana() {
        return etqCampana;
    }

    public void setEtqCampana(String etqCampana) {
        this.etqCampana = etqCampana;
    }

    public String getEtqDistrito() {
        return etqDistrito;
    }

    public void setEtqDistrito(String etqDistrito) {
        this.etqDistrito = etqDistrito;
    }

    public int getEtqSecRuta() {
        return etqSecRuta;
    }

    public void setEtqSecRuta(int etqSecRuta) {
        this.etqSecRuta = etqSecRuta;
    }

    public int getEtqSecZona() {
        return etqSecZona;
    }

    public void setEtqSecZona(int etqSecZona) {
        this.etqSecZona = etqSecZona;
    }

    public int getEtqNroColumna() {
        return etqNroColumna;
    }

    public void setEtqNroColumna(int etqNroColumna) {
        this.etqNroColumna = etqNroColumna;
    }

    public String getEtqRuta() {
        return etqRuta;
    }

    public void setEtqRuta(String etqRuta) {
        this.etqRuta = etqRuta;
    }

    public String getEtqTipLpr() {
        return etqTipLpr;
    }

    public void setEtqTipLpr(String etqTipLpr) {
        this.etqTipLpr = etqTipLpr;
    }

    public String getEtqFlgSecuencia() {
        return etqFlgSecuencia;
    }

    public void setEtqFlgSecuencia(String etqFlgSecuencia) {
        this.etqFlgSecuencia = etqFlgSecuencia;
    }

    public String getEtqDescClasifCli() {
        return etqDescClasifCli;
    }

    public void setEtqDescClasifCli(String etqDescClasifCli) {
        this.etqDescClasifCli = etqDescClasifCli;
    }

    public String getEtqDescProducto() {
        return etqDescProducto;
    }

    public void setEtqDescProducto(String etqDescProducto) {
        this.etqDescProducto = etqDescProducto;
    }

    public String getEtqFchaProduc() {
        return etqFchaProduc;
    }

    public void setEtqFchaProduc(String etqFchaProduc) {
        this.etqFchaProduc = etqFchaProduc;
    }

    public String getEtqFchaDespacho() {
        return etqFchaDespacho;
    }

    public void setEtqFchaDespacho(String etqFchaDespacho) {
        this.etqFchaDespacho = etqFchaDespacho;
    }

    public String getEtqTlfnoCliente() {
        return etqTlfnoCliente;
    }

    public void setEtqTlfnoCliente(String etqTlfnoCliente) {
        this.etqTlfnoCliente = etqTlfnoCliente;
    }

    public String getEtqVentanaDes() {
        return etqVentanaDes;
    }

    public void setEtqVentanaDes(String etqVentanaDes) {
        this.etqVentanaDes = etqVentanaDes;
    }

    public String getEtqDigitosPed() {
        return etqDigitosPed;
    }

    public void setEtqDigitosPed(String etqDigitosPed) {
        this.etqDigitosPed = etqDigitosPed;
    }

    public int getEtqSecEntrega() {
        return etqSecEntrega;
    }

    public void setEtqSecEntrega(int etqSecEntrega) {
        this.etqSecEntrega = etqSecEntrega;
    }

    public int getEtqCorrBltoAgp() {
        return etqCorrBltoAgp;
    }

    public void setEtqCorrBltoAgp(int etqCorrBltoAgp) {
        this.etqCorrBltoAgp = etqCorrBltoAgp;
    }

    public String getEtqRemesaAsig() {
        return etqRemesaAsig;
    }

    public void setEtqRemesaAsig(String etqRemesaAsig) {
        this.etqRemesaAsig = etqRemesaAsig;
    }

    public String getEtqRangosAnaqpp() {
        return etqRangosAnaqpp;
    }

    public void setEtqRangosAnaqpp(String etqRangosAnaqpp) {
        this.etqRangosAnaqpp = etqRangosAnaqpp;
    }

    public String getEtqRuteoEsquema() {
        return etqRuteoEsquema;
    }

    public void setEtqRuteoEsquema(String etqRuteoEsquema) {
        this.etqRuteoEsquema = etqRuteoEsquema;
    }

    public String getEtqRuteoSecProd() {
        return etqRuteoSecProd;
    }

    public void setEtqRuteoSecProd(String etqRuteoSecProd) {
        this.etqRuteoSecProd = etqRuteoSecProd;
    }

    public String getEtqRuteoVehiculo() {
        return etqRuteoVehiculo;
    }

    public void setEtqRuteoVehiculo(String etqRuteoVehiculo) {
        this.etqRuteoVehiculo = etqRuteoVehiculo;
    }

    public String getEtqRuteoZonaRep() {
        return etqRuteoZonaRep;
    }

    public void setEtqRuteoZonaRep(String etqRuteoZonaRep) {
        this.etqRuteoZonaRep = etqRuteoZonaRep;
    }

    public String getEtqFlgOrden() {
        return etqFlgOrden;
    }

    public void setEtqFlgOrden(String etqFlgOrden) {
        this.etqFlgOrden = etqFlgOrden;
    }

    public String getEtqFlgOrienta() {
        return etqFlgOrienta;
    }

    public void setEtqFlgOrienta(String etqFlgOrienta) {
        this.etqFlgOrienta = etqFlgOrienta;
    }

    public String getEtqStatus() {
        return etqStatus;
    }

    public void setEtqStatus(String etqStatus) {
        this.etqStatus = etqStatus;
    }

    public String getEtqRegFcha() {
        return etqRegFcha;
    }

    public void setEtqRegFcha(String etqRegFcha) {
        this.etqRegFcha = etqRegFcha;
    }

    public String getEtqRegUser() {
        return etqRegUser;
    }

    public void setEtqRegUser(String etqRegUser) {
        this.etqRegUser = etqRegUser;
    }
}