package com.yobel.optimus.file;

import java.io.*;

public class FileEscribe {
    private File archivo;
    private FileOutputStream fos;
    private PrintStream ps;

    public FileEscribe(String directorio, String nombreArchivo) throws IOException {
        // Asegura que el directorio exista
        File dir = new File(directorio);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.archivo = new File(dir, nombreArchivo);
        this.fos = new FileOutputStream(archivo);
        this.ps = new PrintStream(fos);
    }

    public void escribirTexto(String texto) {
        ps.print(texto);
    }

    public void nuevaLinea() {
        ps.println();
    }

    public void limpiarBuffer() {
        ps.flush();
    }

    public void cerrarStream() throws IOException {
        if (ps != null) {
            ps.close();
        }
        if (fos != null) {
            fos.close();
        }
    }
}