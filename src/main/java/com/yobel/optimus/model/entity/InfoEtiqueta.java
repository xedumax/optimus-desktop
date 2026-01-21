package com.yobel.optimus.model.entity;

import com.google.gson.annotations.SerializedName;

public class InfoEtiqueta {
    @SerializedName("etqCuenta") private String cuenta;
    @SerializedName("etqFchProceso") private String fechaProceso;
    @SerializedName("etqLoteProceso") private int loteProceso;
    @SerializedName("etqCodAgp") private String codAgp;
    @SerializedName("etqZonaCliente") private String zonaCliente;
    @SerializedName("etqSubCtaCliente") private String subCtaCliente;
    @SerializedName("etqCtrPedido") private String ctrPedido;
    @SerializedName("etqNroPedido") private String nroPedido;
    @SerializedName("etqSecPedido") private int secPedido;
    @SerializedName("etqCodCliente") private String codCliente;
    @SerializedName("etqNombreCliente") private String nombreCliente;
    @SerializedName("etqDireccCliente") private String direccCliente;
    @SerializedName("etqCodLpr") private String codLpr;
    @SerializedName("etqDescLpr") private String descLpr;
    @SerializedName("etqCorrEmpaq") private int corrEmpaq;
    @SerializedName("etqTotBultos") private String totBultos;
    @SerializedName("etqTipoEmpaque") private String tipoEmpaque;
    @SerializedName("etqCategoria") private String categoria;
    @SerializedName("etqSiCliNueva") private String siCliNueva;
    @SerializedName("etqTotUnidades") private int totUnidades;
    @SerializedName("etqTotItems") private int totItems;
    @SerializedName("etqPeso") private double peso;
    @SerializedName("etqVolumen") private double volumen;
    @SerializedName("etqCampana") private String campana;
    @SerializedName("etqDistrito") private String distrito;
    @SerializedName("etqSecRuta") private int secRuta;
    @SerializedName("etqSecZona") private int secZona;
    @SerializedName("etqNroColumna") private int nroColumna;
    @SerializedName("etqRuta") private String ruta;
    @SerializedName("etqTipLpr") private String tipLpr;
    @SerializedName("etqFlgSecuencia") private String flgSecuencia;
    @SerializedName("etqDescClasifCli") private String descClasifCli;
    @SerializedName("etqDescProducto") private String descProducto;
    @SerializedName("etqFchaProduc") private String fchaProduc;
    @SerializedName("etqFchaDespacho") private String fchaDespacho;
    @SerializedName("etqTlfnoCliente") private String tlfnoCliente;
    @SerializedName("etqVentanaDes") private String ventanaDes;
    @SerializedName("etqDigitosPed") private String digitosPed;
    @SerializedName("etqSecEntrega") private int secEntrega;
    @SerializedName("etqCorrBltoAgp") private int corrBltoAgp;
    @SerializedName("etqRemesaAsig") private String remesaAsig;
    @SerializedName("etqRangosAnaqpp") private String rangosAnaqpp;
    @SerializedName("etqRuteoEsquema") private String ruteoEsquema;
    @SerializedName("etqRuteoSecProd") private String ruteoSecProd;
    @SerializedName("etqRuteoVehiculo") private String ruteoVehiculo;
    @SerializedName("etqRuteoZonaRep") private String ruteoZonaRep;
    @SerializedName("etqFlgOrden") private String flgOrden;
    @SerializedName("etqFlgOrienta") private String flgOrienta;
    @SerializedName("etqStatus") private String status;
    @SerializedName("etqRegFcha") private String regFcha;
    @SerializedName("etqRegUser") private String regUser;

    // --- GETTERS ---
    public String getCuenta() { return cuenta; }
    public String getFechaProceso() { return fechaProceso; }
    public int getLoteProceso() { return loteProceso; }
    public String getCodAgp() { return codAgp; }
    public String getZonaCliente() { return zonaCliente; }
    public String getSubCtaCliente() { return subCtaCliente; }
    public String getCtrPedido() { return ctrPedido; }
    public String getNroPedido() { return nroPedido; }
    public int getSecPedido() { return secPedido; }
    public String getCodCliente() { return codCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public String getDireccCliente() { return direccCliente; }
    public String getCodLpr() { return codLpr; }
    public String getDescLpr() { return descLpr; }
    public int getCorrEmpaq() { return corrEmpaq; }
    public String getTotBultos() { return totBultos; }
    public String getTipoEmpaque() { return tipoEmpaque; }
    public String getCategoria() { return categoria; }
    public String getSiCliNueva() { return siCliNueva; }
    public int getTotUnidades() { return totUnidades; }
    public int getTotItems() { return totItems; }
    public double getPeso() { return peso; }
    public double getVolumen() { return volumen; }
    public String getCampana() { return campana; }
    public String getDistrito() { return distrito; }
    public int getSecRuta() { return secRuta; }
    public int getSecZona() { return secZona; }
    public int getNroColumna() { return nroColumna; }
    public String getRuta() { return ruta; }
    public String getTipLpr() { return tipLpr; }
    public String getFlgSecuencia() { return flgSecuencia; }
    public String getDescClasifCli() { return descClasifCli; }
    public String getDescProducto() { return descProducto; }
    public String getFchaProduc() { return fchaProduc; }
    public String getFchaDespacho() { return fchaDespacho; }
    public String getTlfnoCliente() { return tlfnoCliente; }
    public String getVentanaDes() { return ventanaDes; }
    public String getDigitosPed() { return digitosPed; }
    public int getSecEntrega() { return secEntrega; }
    public int getCorrBltoAgp() { return corrBltoAgp; }
    public String getRemesaAsig() { return remesaAsig; }
    public String getRangosAnaqpp() { return rangosAnaqpp; }
    public String getRuteoEsquema() { return ruteoEsquema; }
    public String getRuteoSecProd() { return ruteoSecProd; }
    public String getRuteoVehiculo() { return ruteoVehiculo; }
    public String getRuteoZonaRep() { return ruteoZonaRep; }
    public String getFlgOrden() { return flgOrden; }
    public String getFlgOrienta() { return flgOrienta; }
    public String getStatus() { return status; }
    public String getRegFcha() { return regFcha; }
    public String getRegUser() { return regUser; }

    // --- SETTERS ---
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }
    public void setFechaProceso(String fechaProceso) { this.fechaProceso = fechaProceso; }
    public void setLoteProceso(int loteProceso) { this.loteProceso = loteProceso; }
    public void setCodAgp(String codAgp) { this.codAgp = codAgp; }
    public void setZonaCliente(String zonaCliente) { this.zonaCliente = zonaCliente; }
    public void setSubCtaCliente(String subCtaCliente) { this.subCtaCliente = subCtaCliente; }
    public void setCtrPedido(String ctrPedido) { this.ctrPedido = ctrPedido; }
    public void setNroPedido(String nroPedido) { this.nroPedido = nroPedido; }
    public void setSecPedido(int secPedido) { this.secPedido = secPedido; }
    public void setCodCliente(String codCliente) { this.codCliente = codCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setDireccCliente(String direccCliente) { this.direccCliente = direccCliente; }
    public void setCodLpr(String codLpr) { this.codLpr = codLpr; }
    public void setDescLpr(String descLpr) { this.descLpr = descLpr; }
    public void setCorrEmpaq(int corrEmpaq) { this.corrEmpaq = corrEmpaq; }
    public void setTotBultos(String totBultos) { this.totBultos = totBultos; }
    public void setTipoEmpaque(String tipoEmpaque) { this.tipoEmpaque = tipoEmpaque; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setSiCliNueva(String siCliNueva) { this.siCliNueva = siCliNueva; }
    public void setTotUnidades(int totUnidades) { this.totUnidades = totUnidades; }
    public void setTotItems(int totItems) { this.totItems = totItems; }
    public void setPeso(double peso) { this.peso = peso; }
    public void setVolumen(double volumen) { this.volumen = volumen; }
    public void setCampana(String campana) { this.campana = campana; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public void setSecRuta(int secRuta) { this.secRuta = secRuta; }
    public void setSecZona(int secZona) { this.secZona = secZona; }
    public void setNroColumna(int nroColumna) { this.nroColumna = nroColumna; }
    public void setRuta(String ruta) { this.ruta = ruta; }
    public void setTipLpr(String tipLpr) { this.tipLpr = tipLpr; }
    public void setFlgSecuencia(String flgSecuencia) { this.flgSecuencia = flgSecuencia; }
    public void setDescClasifCli(String descClasifCli) { this.descClasifCli = descClasifCli; }
    public void setDescProducto(String descProducto) { this.descProducto = descProducto; }
    public void setFchaProduc(String fchaProduc) { this.fchaProduc = fchaProduc; }
    public void setFchaDespacho(String fchaDespacho) { this.fchaDespacho = fchaDespacho; }
    public void setTlfnoCliente(String tlfnoCliente) { this.tlfnoCliente = tlfnoCliente; }
    public void setVentanaDes(String ventanaDes) { this.ventanaDes = ventanaDes; }
    public void setDigitosPed(String digitosPed) { this.digitosPed = digitosPed; }
    public void setSecEntrega(int secEntrega) { this.secEntrega = secEntrega; }
    public void setCorrBltoAgp(int corrBltoAgp) { this.corrBltoAgp = corrBltoAgp; }
    public void setRemesaAsig(String remesaAsig) { this.remesaAsig = remesaAsig; }
    public void setRangosAnaqpp(String rangosAnaqpp) { this.rangosAnaqpp = rangosAnaqpp; }
    public void setRuteoEsquema(String ruteoEsquema) { this.ruteoEsquema = ruteoEsquema; }
    public void setRuteoSecProd(String ruteoSecProd) { this.ruteoSecProd = ruteoSecProd; }
    public void setRuteoVehiculo(String ruteoVehiculo) { this.ruteoVehiculo = ruteoVehiculo; }
    public void setRuteoZonaRep(String ruteoZonaRep) { this.ruteoZonaRep = ruteoZonaRep; }
    public void setFlgOrden(String flgOrden) { this.flgOrden = flgOrden; }
    public void setFlgOrienta(String flgOrienta) { this.flgOrienta = flgOrienta; }
    public void setStatus(String status) { this.status = status; }
    public void setRegFcha(String regFcha) { this.regFcha = regFcha; }
    public void setRegUser(String regUser) { this.regUser = regUser; }
}