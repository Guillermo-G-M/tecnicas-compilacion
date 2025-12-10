package com.compilador.utils;

/**
 * Utilidad para aplicar colores ANSI en la salida de consola
 * verde éxito, amarillo warnings, rojo errores
 */
public class ColoresANSI {

    // Códigos de color ANSI
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    // Estilos
    public static final String NEGRITA = "\u001B[1m";
    public static final String SUBRAYADO = "\u001B[4m";

    /**
     * Aplica color verde para mensajes de éxito
     */
    public static String exito(String texto) {
        return VERDE + texto + RESET;
    }

    /**
     * Aplica color amarillo para warnings
     */
    public static String warning(String texto) {
        return AMARILLO + texto + RESET;
    }

    /**
     * Aplica color rojo para errores
     */
    public static String error(String texto) {
        return ROJO + texto + RESET;
    }

    /**
     * Aplica color azul para información
     */
    public static String info(String texto) {
        return CYAN + texto + RESET;
    }

    /**
     * Aplica negrita al texto
     */
    public static String negrita(String texto) {
        return NEGRITA + texto + RESET;
    }

    /**
     * Combina color y negrita
     */
    public static String exitoNegrita(String texto) {
        return VERDE + NEGRITA + texto + RESET;
    }

    public static String warningNegrita(String texto) {
        return AMARILLO + NEGRITA + texto + RESET;
    }

    public static String errorNegrita(String texto) {
        return ROJO + NEGRITA + texto + RESET;
    }
}
