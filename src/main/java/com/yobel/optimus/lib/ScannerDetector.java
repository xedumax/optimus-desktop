package com.yobel.optimus.lib;

import javafx.scene.input.KeyEvent;
import java.util.function.Consumer;

public class ScannerDetector {
    private final StringBuilder buffer = new StringBuilder();
    private long lastEventTime = 0;
    private final Consumer<String> onScanComplete;
    private static final long THRESHOLD = 60; // Milisegundos entre teclas

    public ScannerDetector(Consumer<String> onScanComplete) {
        this.onScanComplete = onScanComplete;
    }

    // Getter necesario para el Controller
    public long getLastEventTime() {
        return lastEventTime;
    }

    public int getBufferLength() {
        return buffer.length();
    }

    public void resetBuffer() {
        buffer.setLength(0);
    }

    public void updateLastEventTime(long time) {
        this.lastEventTime = time;
    }


    public String getBufferContent() {
        return buffer.toString();
    }

    public void handleKeyEvent(KeyEvent event) {
        long now = System.currentTimeMillis();
        String character = event.getCharacter();

        // Si el tiempo entre teclas es muy largo (> 150ms),
        // reseteamos el buffer porque lo anterior fue humano.
        if (now - lastEventTime > 150) {
            resetBuffer();
        }

        updateLastEventTime(now);

        // No guardamos los caracteres de control en el buffer de datos
        if (!character.equals("\r") && !character.equals("\n")) {
            buffer.append(character);
        } else {
            // Si es Enter y hay algo, disparamos el callback
            if (buffer.length() > 0) {
                String resultado = buffer.toString().trim();
                resetBuffer();
                onScanComplete.accept(resultado);
            }
        }
    }
}