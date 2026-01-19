package com.yobel.optimus.lib;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

/**
 * Wrapper para la librería ZXing (Core).
 * Se encarga de la decodificación técnica de códigos de barras.
 */
public class BarcodeDecoder {

    public static String decode(Image image) throws Exception {
        if (image == null) throw new Exception("No se proporcionó una imagen para decodificar.");

        // Convertimos la imagen de JavaFX al formato binario de ZXing
        LuminanceSource source = new JavaFXLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            throw new Exception("No se encontró un código de barras válido en la imagen.");
        }
    }

    /**
     * Implementación personalizada de LuminanceSource para JavaFX.
     * Esto permite procesar la imagen usando solo la dependencia zxing:core.
     */
    private static class JavaFXLuminanceSource extends LuminanceSource {
        private final byte[] luminances;

        public JavaFXLuminanceSource(Image image) {
            super((int) image.getWidth(), (int) image.getHeight());
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            PixelReader pr = image.getPixelReader();

            luminances = new byte[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = pr.getArgb(x, y);
                    // Extraemos canales R, G, B para calcular escala de grises
                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;
                    // Promedio simple de luminancia para ZXing
                    luminances[y * width + x] = (byte) ((r + g + b) / 3);
                }
            }
        }

        @Override
        public byte[] getRow(int y, byte[] row) {
            System.arraycopy(luminances, y * getWidth(), row, 0, getWidth());
            return row;
        }

        @Override
        public byte[] getMatrix() {
            return luminances;
        }
    }
}