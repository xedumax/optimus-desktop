package com.yobel.optimus.lib;

import com.yobel.optimus.model.entity.InfoEtiqueta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CargaFileEtq2Horiz implements GeneradorEtiqueta {

    @Override
    public void generar(List<InfoEtiqueta> data, String directorio, String nombreArchivo) throws IOException {
        // 1. VALIDACIÓN DE RUTA (Requerido: C:\OptimusDesk\Prd\ETQ\)
        File folder = new File(directorio);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // El nombre del archivo ahora viene como XXX_ETIQUETA.TXT (donde XXX es etqCuenta)
        File file = new File(folder, nombreArchivo);

        try (BufferedWriter f = new BufferedWriter(new FileWriter(file))) {
            for (InfoEtiqueta etq : data) {
                // 2. ASIGNACIÓN DE VARIABLES SEGÚN EL API RESPONSE
                String vFpr = etq.getEtqFchProceso();
                String vDSC = etq.getEtqDescLpr() != null ? etq.getEtqDescLpr().trim() : "";
                String vCOR = String.valueOf(etq.getEtqCorrEmpaq());
                String vBLT = etq.getEtqTotBultos();
                String vEPQ = etq.getEtqTipoEmpaque();
                String vSEC = String.valueOf(etq.getEtqSecPedido());
                String vTLP = etq.getEtqTipLpr();
                String vCTD = String.valueOf(etq.getEtqTotUnidades());
                String vCCL = etq.getEtqCodCliente();
                String vNBR = etq.getEtqNombreCliente() != null ? etq.getEtqNombreCliente() : "";
                String vDIR = etq.getEtqDireccCliente() != null ? etq.getEtqDireccCliente() : "";
                String vDIS = etq.getEtqDistrito();
                String vCAMP = etq.getEtqCampana();
                String vLOE = String.valueOf(etq.getEtqLoteProceso());
                String vRemesa = etq.getEtqRemesaAsig() != null ? etq.getEtqRemesaAsig() : "0";

                // Lógica de ceros a la izquierda (vCORAUX = 001, 012, etc.)
                String vCORAUX = String.format("%03d", Integer.parseInt(vCOR));

                // Lógica de Reclamos y Zona
                String vREC = "02".equals(etq.getEtqCtrPedido()) ? "RECLAMOS" : " ";
                String vZON = etq.getEtqZonaCliente();
                String vCTA = (vZON != null && vZON.length() >= 4) ? vZON.substring(0, 4) : vZON;
                String vSCT = etq.getEtqSubCtaCliente() != null ? etq.getEtqSubCtaCliente() : "";

                // Truncamiento de Cliente y Dirección
                String vNBR1 = vNBR.length() > 18 ? vNBR.substring(0, 18) : vNBR;
                String vNBR2 = vNBR.length() > 36 ? vNBR.substring(18, Math.min(vNBR.length(), 36)) : "";
                String vDIR1 = vDIR.length() > 25 ? vDIR.substring(0, 25) : vDIR;
                String vDIR2 = vDIR.length() > 50 ? vDIR.substring(25, Math.min(vDIR.length(), 50)) : "";

                // Código de Barras
                String vCODBAR = "3".equals(vTLP)
                        ? etq.getEtqCtrPedido() + etq.getEtqNroPedido() + etq.getEtqCodAgp() + vCORAUX
                        : etq.getEtqCtrPedido() + etq.getEtqNroPedido() + vCORAUX;

                // 3. GENERACIÓN DE CÓDIGO ZPL (DISEÑO HORIZONTAL)
                f.write("^XA"); f.newLine();
                f.write("^CI28"); f.newLine(); // Soporte para Ñ y tildes
                f.write("^MMR"); f.newLine();
                f.write("^PR6,6"); f.newLine();

                // Bloque 1: Descripción y Empaque
                f.write("^fo680,180^a0n,35,35^FWR^fd" + vDSC + "^FS"); f.newLine();
                f.write("^fo630,350^a0n,50,45^FWR^fd" + vCOR + " / " + vBLT + "^FS"); f.newLine();

                if (!"3".equals(vTLP)) {
                    f.write("^fo440,400^a0n,80,65^FWR^fd" + vRemesa + "^FS"); f.newLine();
                }

                f.write("^fo640,180^AD^fdEmpaque:^FS"); f.newLine();
                f.write("^fo630,280^a0n,45,45^FWR^fd" + vEPQ + "^FS"); f.newLine();
                f.write("^fo500,175^a0n,120,120^FWR^fd" + vSEC + "^FS"); f.newLine();

                // Bloque 2: Unidades / Descripción Clasificación
                String label = "3".equals(vTLP) ? "Descripcion:" : "Unidades:";
                f.write("^fo480,180^AD^fd" + label + "^FS"); f.newLine();
                if (!"3".equals(vTLP)) {
                    f.write("^fo470,300^a0n,38,38^FWR^fd" + vCTD + "^FS"); f.newLine();
                }

                f.write("^fo450,180^a0n,30,35^FWR^fd" + (etq.getEtqDescClasifCli() != null ? etq.getEtqDescClasifCli() : "") + "^FS"); f.newLine();
                f.write("^fo450,200^a0n,30,35^FWR^fd" + vREC + "^FS"); f.newLine();

                // Bloque 3: Cliente y Dirección
                f.write("^fo420,180^AD^fdCliente:^FS"); f.newLine();
                f.write("^fo420,280^a0n,30,30^FWR^fd" + vCCL + "^FS"); f.newLine();
                f.write("^fo390,180^a0n,25,25^FWR^fd" + vNBR1 + "^FS"); f.newLine();
                f.write("^fo360,180^a0n,25,25^FWR^fd" + vNBR2 + "^FS"); f.newLine();

                f.write("^fo340,180^AD^fdDireccion^FS"); f.newLine();
                f.write("^fo320,180^AD^fd" + vDIR1 + "^FS"); f.newLine();
                f.write("^fo300,180^AD^fd" + vDIR2 + "^FS"); f.newLine();
                f.write("^fo280,180^AD^fd" + vDIS + "^FS"); f.newLine();

                // Bloque 4: Pedido y Zona/Ruta
                f.write("^fo248,180^AD^fdPedido^FS"); f.newLine();
                f.write("^fo218,180^a0n,30,30^FWR^fd" + etq.getEtqCtrPedido() + etq.getEtqNroPedido() + "^FS"); f.newLine();

                f.write("^fo120,180^a0n,95,95^FWR^fd" + vCTA.trim() + vSCT.trim() + "^FS"); f.newLine();
                f.write("^fo110,280^a0n,30,30^FWR^fd" + vSCT + "^FS"); f.newLine();

                // Bloque 5: Campaña, Fecha y Lote
                f.write("^fo70,180^AD^fdCamp:^FS"); f.newLine();
                f.write("^fo70,270^a0n,40,40^FWR^fd" + vCAMP + "^FS"); f.newLine();
                f.write("^LRY^fo70,270^GB05,120,40^FS"); f.newLine(); // Línea decorativa

                f.write("^fo35,180^AD^fdF.Prc:^FS"); f.newLine();
                f.write("^fo35,260^a0n,30,30^FWR^fd" + vFpr + "^FS"); f.newLine();
                f.write("^fo35,370^AD^fdLte:^FS"); f.newLine();
                f.write("^fo35,420^a0n,30,30^FWR^fd" + vLOE + "^FS"); f.newLine();

                // Códigos de Barras
                f.write("^fo60,55^BY3,3:1,10"); f.newLine();
                f.write("^fo60,55^BCN,110,N,N,N^FD" + vCODBAR + "^FS"); f.newLine();
                f.write("^fo530,380^BXR,6,200,16^fd" + vCODBAR + "^FS"); f.newLine();

                f.write("^PQ1"); f.newLine();
                f.write("^xz"); f.newLine();
            }
        }
    }
}