package com.yobel.optimus.util;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DatePickerUtil {

    private DatePickerUtil() {
        // Evita instanciación
    }

    /**
     * Configura el DatePicker con formato yyyy-MM-dd,
     * bloquea edición manual y asigna fecha actual por defecto.
     */
    public static void configurar(DatePicker datePicker) {
        configurar(datePicker, "yyyy-MM-dd", true, true);
    }

    /**
     * Configuración flexible para futuros casos
     */
    public static void configurar(
            DatePicker datePicker,
            String pattern,
            boolean setTodayByDefault,
            boolean bloquearEdicionManual
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty())
                        ? LocalDate.parse(string, formatter)
                        : null;
            }
        });

        if (bloquearEdicionManual) {
            datePicker.getEditor().setEditable(false);
            datePicker.getEditor().setFocusTraversable(false);
        }

        if (setTodayByDefault) {
            datePicker.setValue(LocalDate.now());
        }
    }
}