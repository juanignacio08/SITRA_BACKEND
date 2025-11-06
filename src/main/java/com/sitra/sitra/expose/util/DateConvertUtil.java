package com.sitra.sitra.expose.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class DateConvertUtil {

    public static LocalDate parseFechaDDMMYYYY(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null; // O lanzar una excepción, según tu lógica de negocio
        }

        // Define el patrón exacto de la fecha (Día/Mes/Año)
        // DD para día con dos dígitos, MM para mes con dos dígitos, YYYY para año con cuatro dígitos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Parsea la cadena de texto a un objeto LocalDate
            return LocalDate.parse(fechaStr, formatter);
        } catch (DateTimeParseException e) {
            // Maneja el error si la cadena no coincide con el formato esperado
            System.err.println("Error al parsear la fecha '" + fechaStr + "': " + e.getMessage());
            throw new IllegalArgumentException("Formato de fecha inválido. Se esperaba DD/MM/AAAA.", e);
        }
    }

    public static String formatLocalDateToDDMMYYYY(LocalDate localDate) {
        if (localDate == null) {
            return null; // O una cadena vacía, según tu lógica de negocio
        }

        // Define el patrón de formato de salida
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formatea el LocalDate a una cadena de texto usando el patrón
        return localDate.format(formatter);
    }

}

