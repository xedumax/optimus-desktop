package com.yobel.optimus.lib;

import javafx.scene.input.KeyEvent;
import java.util.function.Consumer;

public class ScannerDetector {
    private final StringBuilder buffer = new StringBuilder();
    private long lastEventTime = 0;
    private final Consumer<String> onScanComplete;
    private static final long THRESHOLD = 50; // ms

    public ScannerDetector(Consumer<String> onScanComplete) {
        this.onScanComplete = onScanComplete;
    }

    // Método para identificar si es ráfaga de escáner
    public boolean isScannerBurst(long currentTime) {
        return (currentTime - lastEventTime < THRESHOLD);
    }

    public void resetBuffer() {
        buffer.setLength(0);
    }

    public void handleKeyEvent(KeyEvent event) {
        String character = event.getCharacter();
        lastEventTime = System.currentTimeMillis();

        if (character.equals("\r") || character.equals("\n")) {
            if (buffer.length() > 0) {
                onScanComplete.accept(buffer.toString().trim());
                buffer.setLength(0);
            }
        } else {
            buffer.append(character);
        }
    }
}