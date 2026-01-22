package com.yobel.optimus.lib;

import com.yobel.optimus.model.entity.InfoEtiqueta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CargaFileEtq1Vert implements GeneradorEtiqueta {

    @Override
    public void generar(List<InfoEtiqueta> data, String directorio, String nombreArchivo) throws IOException {
        File file = new File(directorio, nombreArchivo);

        try (BufferedWriter f = new BufferedWriter(new FileWriter(file))) {
            for (InfoEtiqueta etq : data) {
                // 1. ASIGNACIÓN DE VARIABLES SEGÚN DOCUMENTACIÓN
                String vFpr = etq.getEtqFchProceso();
                String vAGP = etq.getEtqCodAgp();
                String vZON = etq.getEtqZonaCliente();
                String vSCT = etq.getEtqSubCtaCliente();
                String vTER = " "; // A revisar uso según doc
                String vCTR = etq.getEtqCtrPedido();
                String vNRO = etq.getEtqNroPedido();
                String vSEC = String.valueOf(etq.getEtqSecPedido());
                String vCCL = etq.getEtqCodCliente();
                String vNBR = etq.getEtqNombreCliente();
                String vDIR = etq.getEtqDireccCliente();
                String vDSC = etq.getEtqDescLpr();
                String vEPQ = etq.getEtqTipoEmpaque();
                String vCTD = String.valueOf(etq.getEtqTotUnidades());
                String vCAMP = etq.getEtqCampana();
                String vDIS = etq.getEtqDistrito();
                String vTLP = etq.getEtqTipLpr();
                String vNCCE = etq.getEtqDescClasifCli(); // vDESCRI en doc
                String vFechaProc = etq.getEtqFchProceso();
                String vBLT = etq.getEtqTotBultos();
                int vCOR = etq.getEtqCorrEmpaq();
                int vCorBltAgp = etq.getEtqCorrBltoAgp();

                // Variables adicionales
                int vTotBltPed = 0; // Falta adicionar al API según doc
                String vA02 = ""; // REVISAR según doc
                String vTipProc = "1"; // Valor por defecto mencionado en doc previa

                // 2. LÓGICA DE FORMATEO Y TRUNCAMIENTO
                String vNroEpq = vCOR + " / " + vBLT;
                String vCorPedido = vCorBltAgp + " / " + vTotBltPed;

                String vREC = "   ";
                if ("02".equals(vCTR)) {
                    vREC = "RECLAMOS";
                }

                String vCTA = (vZON != null && vZON.length() >= 4) ? vZON.substring(0, 4) : vZON;

                // Truncamiento Nombre (18 caracteres)
                String vNBR1 = (vNBR.length() > 18) ? vNBR.substring(0, 18) : vNBR;
                String vNBR2 = (vNBR.length() > 36) ? vNBR.substring(18, 36) : (vNBR.length() > 18 ? vNBR.substring(18) : "");

                // Truncamiento Dirección (25 caracteres)
                String vDIR1 = (vDIR.length() > 25) ? vDIR.substring(0, 25) : vDIR;
                String vDIR2 = (vDIR.length() > 50) ? vDIR.substring(25, 50) : (vDIR.length() > 25 ? vDIR.substring(25) : "");

                // Formateo CORRAUX (Ceros a la izquierda, 3 dígitos)
                String vCORAUX = String.format("%03d", vCOR);

                // Lógica Código de Barras
                String vCODBAR = "";
                if ("3".equals(vTLP)) {
                    vCODBAR = vCTR + vNRO + vAGP + vCORAUX;
                } else {
                    vCODBAR = vCTR + vNRO + vCORAUX;
                }

                // 3. GENERACIÓN DE CÓDIGO ZPL
                f.write("^XA"); f.newLine();
                f.write("^MMR"); f.newLine();
                f.write("^PR6,6"); f.newLine();
                f.write("^fo240,30^a0n,30,30^fd" + vDSC + "^FS"); f.newLine();
                f.write("^fo410,35^a0n,61,61^fd" + vNroEpq + "^FS"); f.newLine();
                f.write("^fo240,80^FWN^AD^fdEmpaque:^FS"); f.newLine();
                f.write("^fo350,70^a0n,41,41^fd" + vEPQ + "^FS"); f.newLine();
                f.write("^fo280,140^a0n,140,140^fd" + vSEC + "^FS"); f.newLine();

                if ("3".equals(vTLP)) {
                    f.write("^fo240,265^FWN^AD^fdDescripcion:^FS"); f.newLine();
                } else {
                    f.write("^fo240,265^FWN^AD^fdUnidades:^FS"); f.newLine();
                    f.write("^fo400,260^a0n,38,38^fd" + vCTD + "^FS"); f.newLine();
                }

                f.write("^fo250,290^a0n,35,35^fd" + vNCCE + "^FS"); f.newLine();
                f.write("^fo350,290^a0n,35,35^fd" + vREC + "^FS"); f.newLine();
                f.write("^fo240,330^AD^fdCliente: " + vCCL + "^FS"); f.newLine();
                f.write("^fo240,350^a0n,30,30^fd" + vNBR1 + "^FS"); f.newLine();
                f.write("^fo240,375^a0n,30,30^fd" + vNBR2 + "^FS"); f.newLine();
                f.write("^fo240,410^AD^fdDireccion:^FS"); f.newLine();
                f.write("^fo240,430^AD^fd" + vDIR1 + "^FS"); f.newLine();
                f.write("^fo240,455^AD^fd" + vDIR2 + "^FS"); f.newLine();
                f.write("^fo240,480^AD^fd" + vDIS + "^FS"); f.newLine();
                f.write("^fo240,505^a0n,15,15^fdPEDIDO^FS"); f.newLine();
                f.write("^fo250,530^a0n,50,50^fd" + vCTR + vNRO + "^FS"); f.newLine();

                if ("3".equals(vTipProc)) {
                    f.write("^fo270,580^a0n,140,140^fd" + vA02.trim() + "^FS"); f.newLine();
                } else {
                    f.write("^fo270,580^a0n,140,140^fd" + vCTA.trim() + "^FS"); f.newLine();
                }

                f.write("^fo250,700^a0n,50,50^fb160,,,C^fd" + vTER + "^FS"); f.newLine();

                if ("3".equals(vTipProc)) {
                    f.write("^fo450,700^a0n,60,60^fd" + vCTA.trim() + "^FS"); f.newLine();
                } else {
                    f.write("^fo450,700^a0n,60,60^fd" + vSCT + "^FS"); f.newLine();
                }

                // Condicional de Correlativo Pedido (vFlgCorr se asume 'S' según flujo)
                f.write("^fo40,680^a0n,130,130^fd" + vCorPedido.trim() + "^FS"); f.newLine();

                f.write("^fo240,750^AD^fdCamp:^FS"); f.newLine();
                f.write("^fo310,750^a0n,25,25^fd" + vCAMP + "^FS"); f.newLine();
                f.write("^fo400,750^AD^fdF.Prc:^FS"); f.newLine();
                f.write("^fo480,750^a0n,25,20^fd" + vFechaProc.trim() + "^FS"); f.newLine();

                // Código de Barras
                f.write("^fo30,30^BY3,3:1,10"); f.newLine();
                f.write("^fo30,30^BCB,190,N,N,N^FD" + vCODBAR + "^FS"); f.newLine();

                f.write("^PQ1"); f.newLine();
                f.write("^PON"); f.newLine();
                f.write("^xz"); f.newLine();
                f.newLine(); // Línea en blanco entre etiquetas
            }
        }
    }
}