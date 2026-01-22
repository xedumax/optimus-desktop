package com.yobel.optimus.util;

public class AppConfig {

    public static class Directorios {
        public static String rutaEtiquetas() {
            return "C:/OptimusDesk/Prd/ETQ/";
        }
    }
    /**
     * Retorna la URL base dependiendo del ambiente guardado en AppContext.
     * Si no hay ambiente definido, retorna DEV por seguridad.
     */
    public static String getBaseUrl() {
        String env = AppContext.getAmbiente();
        if (env == null) env = "DEV";

        return switch (env) {
            case "QAS"  -> "https://optimus-test.yobel.biz";
            case "PROD" -> "https://optimus.yobel.biz";
            default     -> "https://optimus-dev.yobel.biz";
        };
    }

    // --- AGRUPACIÓN DE ENDPOINTS ---
    // Usamos métodos para que siempre se concatenen con la BaseUrl actualizada.

    public static class Auth {
        public static String login() {
            return getBaseUrl() + "/api/msi/autorizacion/auth/login";
        }
        public static String sistemas() {
            return getBaseUrl() + "/api/msi/autorizacion/system";
        }
    }

    public static class Maestros {
        public static String agrupadores() {
            return getBaseUrl() + "/api/prd/matriz/agrupadorProduccion/listSel/001";
        }
        public static String cuentas() {
            return getBaseUrl() + "/api/dyt/gestion-cuentas/listAll";
        }
        public static String ventanas() {
            return getBaseUrl() + "/api/dyt/gestion-maestros/ventanaDsp/list/";
        }
        public static String lineasProduccion(String cuenta, String agp) {
            return getBaseUrl() + "/api/prd/matriz/lineaProduccion/listSel/" + cuenta + "/" + agp;
        }
    }

    public static class Operaciones {
        public static String capturaBultos() {
            return getBaseUrl() + "/api/prd/procesoregistroinfo/procesoPedidos/capturaBultos";
        }
        public static String infoEtiquetas(String cuenta, String agp, String lpr) {
            // Reemplaza valores vacíos por "null" string según el API
            String c = (cuenta == null || cuenta.isEmpty()) ? "null" : cuenta;
            String a = (agp == null || agp.isEmpty()) ? "null" : agp;
            String l = (lpr == null || lpr.isEmpty()) ? "null" : lpr;

            return "https://optimus-dev.yobel.biz/api/prd/procesoproducto/procesoEtiquetas/InfoEtiquetas/"
                    + c + "/" + a + "/" + l;
        }
    }

    // --- NUEVA SECCIÓN DE ETIQUETAS ---
    public static class Etiquetas {
        public static String datosImpresion(String cuenta) {
            // Este endpoint reemplaza a la vista AIPETQ
            return getBaseUrl() + "/api/prd/impresion/etiquetas/datos/" + cuenta;
        }
        public static String configuracion(String cuenta) {
            // Este endpoint reemplaza a la vista AIPDXP
            return getBaseUrl() + "/api/prd/impresion/etiquetas/config/" + cuenta;
        }
    }


}