package com.yobel.optimus.lib;

import com.yobel.optimus.model.entity.InfoEtiqueta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CargaFileEtq1Horiz implements GeneradorEtiqueta {

    @Override
    public void generar(List<InfoEtiqueta> data, String directorio, String nombreArchivo) throws IOException {
        File file = new File(directorio, nombreArchivo);

        try (BufferedWriter f = new BufferedWriter(new FileWriter(file))) {
            for (InfoEtiqueta etq : data) {
                // 1. ASIGNACIÓN DE VARIABLES
                String vFpr = etq.getEtqFchProceso();
                String vLOE = String.valueOf(etq.getEtqLoteProceso());
                String vAGP = etq.getEtqCodAgp();
                String vZON = etq.getEtqZonaCliente();
                String vSCT = etq.getEtqSubCtaCliente();
                String vTER = " ";
                String vCTR = etq.getEtqCtrPedido();
                String vNRO = etq.getEtqNroPedido();
                String vSEC = String.valueOf(etq.getEtqSecPedido());
                String vCCL = etq.getEtqCodCliente();
                String vNBR = etq.getEtqNombreCliente();
                String vDIR = etq.getEtqDireccCliente();
                String vLPR = etq.getEtqCodLpr();
                String vDSC = etq.getEtqDescLpr();
                String vCOR = String.valueOf(etq.getEtqCorrEmpaq());
                String vBLT = etq.getEtqTotBultos();
                String vEPQ = etq.getEtqTipoEmpaque();
                String vCTD = String.valueOf(etq.getEtqTotUnidades());
                String vCAMP = etq.getEtqCampana();
                String vDIS = etq.getEtqDistrito();
                String vTLP = etq.getEtqTipLpr();
                String vDESCRI = etq.getEtqDescClasifCli();
                String vA02 = " "; // Valor adicional si aplica
                String vRemesa = etq.getEtqRemesaAsig();

                // Valores de configuración (Hardcoded según lógica de ejemplo)
                String vTipProc = "1";
                String vFlgCorr = "S";

                // 2. LÓGICA DE FORMATEO
                // Correlativo auxiliar con ceros
                String vCORAUX = String.format("%03d", Integer.parseInt(vCOR));
                String vNroEpq = vCOR + " / " + vBLT;

                String vCorBltAgp = String.valueOf(etq.getEtqCorrBltoAgp());
                String vTotBltPed = "0"; // Ajustar si el API lo provee
                String vCorPedido = vCorBltAgp.trim() + "/" + vTotBltPed.trim();

                String vREC = ("02".equals(vCTR) || "12".equals(vCTR)) ? "RECLAMOS" : "   ";
                String vCTA = (vZON != null && vZON.length() >= 4) ? vZON.substring(0, 4) : vZON;

                // Truncamiento Horizontal (22 caracteres por línea según ejemplo)
                String vNBR1 = (vNBR.length() > 22) ? vNBR.substring(0, 22) : vNBR;
                String vNBR2 = (vNBR.length() > 44) ? vNBR.substring(22, Math.min(vNBR.length(), 44)) : "";

                String vDIR1 = (vDIR.length() > 25) ? vDIR.substring(0, 25) : vDIR;
                String vDIR2 = (vDIR.length() > 50) ? vDIR.substring(25, Math.min(vDIR.length(), 50)) : "";

                // Lógica Código de Barras
                String vCODBAR = "3".equals(vTLP) ? vCTR + vNRO.trim() + vAGP.trim() + vCORAUX
                        : vCTR + vNRO.trim() + vCORAUX;

                // Ventana para Código 2D (DataMatrix)
                String vVent = ""; // Lógica simplificada

                // 3. GENERACIÓN DE CÓDIGO ZPL (DISEÑO HORIZONTAL)
                f.write("^XA"); f.newLine();
                f.write("^MMR"); f.newLine();
                f.write("^PR6,6"); f.newLine();

                // Descripción LPR (Rotado)
                f.write("^fo750,170^a0n,30,30^FWR^fd" + vDSC.trim() + "^FS"); f.newLine();

                if (!"3".equals(vTLP)) {
                    f.write("^fo540,420^a0n,80,65^FWR^fd" + vRemesa.trim() + "^FS"); f.newLine();
                }

                f.write("^fo530,180^a0n,130,130^FWR^fd" + vSEC + "^FS"); f.newLine();
                f.write("^fo700,180^AD^fdEmpaque:^FS"); f.newLine();
                f.write("^fo735,320^a0n,50,50^FWR^fd" + vNroEpq + "^FS"); f.newLine();
                f.write("^fo700,280^a0n,45,45^FWR^fd" + vEPQ + "^FS"); f.newLine();

                if ("3".equals(vTLP)) {
                    f.write("^fo520,180^AD^fdDescripcion:^FS"); f.newLine();
                } else {
                    f.write("^fo520,180^AD^fdUnidades:^FS"); f.newLine();
                    f.write("^fo510,330^a0n,38,38^FWR^fd" + vCTD + "^FS"); f.newLine();
                }

                f.write("^fo480,180^a0n,35,35^FWR^fd" + vDESCRI + "^FS"); f.newLine();
                f.write("^fo450,180^a0n,35,35^FWR^fd" + vREC + "^FS"); f.newLine();

                f.write("^fo440,180^AD^fdCliente:^FS"); f.newLine();
                f.write("^fo440,300^a0n,30,30^FWR^fd" + vCCL + "^FS"); f.newLine();
                f.write("^fo400,180^a0n,25,25^FWR^fd" + vNBR1 + "^FS"); f.newLine();
                f.write("^fo370,180^a0n,25,25^FWR^fd" + vNBR2 + "^FS"); f.newLine();

                f.write("^fo345,180^AD^fdDireccion^FS"); f.newLine();
                f.write("^fo325,180^AD^fd" + vDIR1 + "^FS"); f.newLine();
                f.write("^fo305,180^AD^fd" + vDIR2 + "^FS"); f.newLine();
                f.write("^fo280,180^AD^fd" + vDIS + "^FS"); f.newLine();

                f.write("^fo248,180^AD^fdPedido^FS"); f.newLine();
                f.write("^fo218,180^a0n,30,30^FWR^fd" + vCTR + vNRO + "^FS"); f.newLine();

                if ("3".equals(vTipProc)) {
                    f.write("^fo120,180^a0n,100,100^FWR^fd" + vA02.trim() + "^FS"); f.newLine();
                    f.write("^fo90,350^a0n,50,50^FWR^fd" + vCTA.trim() + "^FS"); f.newLine();
                } else {
                    f.write("^fo120,180^a0n,100,100^FWR^fd" + vCTA.trim() + "^FS"); f.newLine();
                    f.write("^fo90,350^a0n,50,50^FWR^fd" + vSCT + "^FS"); f.newLine();
                }

                f.write("^fo90,180^a0n,50,50^FWR^fd" + vTER + "^FS"); f.newLine();
                f.write("^fo75,180^AD^fdCamp:^FS"); f.newLine();
                f.write("^fo75,250^a0n,25,25^FWR^fd" + vCAMP + "^FS"); f.newLine();
                f.write("^fo75,350^AD^fdLte:^FS"); f.newLine();
                f.write("^fo75,410^a0n,25,25^FWR^fd" + vLOE + "^FS"); f.newLine();
                f.write("^fo50,180^AD^fdF.Prc:^FS"); f.newLine();
                f.write("^fo50,270^a0n,25,25^FWR^fd" + vFpr + "^FS"); f.newLine();

                if ("S".equals(vFlgCorr)) {
                    f.write("^fo20,25^a0n,110,110^FWR^fd" + vCorPedido.trim() + "^FS"); f.newLine();
                }

                // Código de Barras 1D
                f.write("^fo60,25^BY3,3:1,10"); f.newLine();
                int xPos = (vNRO.trim().length() > 12) ? 60 : 130;
                f.write("^fo" + xPos + ",25^BCN,130,N,N,N^FD" + vCODBAR + "^FS"); f.newLine();

                // Código 2D (DataMatrix)
                f.write("^fo640,400^BXR,6,200,16^fd" + vCODBAR + vVent.trim() + "^FS"); f.newLine();

                // Caso especial LPR LP4 (Inverso)
                if ("LP4".equals(vLPR)) {
                    f.write("^LRY^fo50,180^GB200,260,180^FS"); f.newLine();
                }

                f.write("^PQ1"); f.newLine();
                f.write("^PON"); f.newLine();
                f.write("^xz"); f.newLine();
                f.newLine();
            }
        }
    }
}