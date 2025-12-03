package com.compilador.analizador.semantico;

import java.util.ArrayList;
import java.util.List;

/**
 * Sistema de reporte de errores y warnings con colores ANSI
 */
public class ReporteErrores {

    public enum TipoMensaje {
        ERROR,
        WARNING,
        INFO
    }

    public static class Mensaje {
        private TipoMensaje tipo;
        private String descripcion;
        private int linea;
        private int columna;

        public Mensaje(TipoMensaje tipo, String descripcion, int linea, int columna) {
            this.tipo = tipo;
            this.descripcion = descripcion;
            this.linea = linea;
            this.columna = columna;
        }

        public TipoMensaje getTipo() {
            return tipo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public int getLinea() {
            return linea;
        }

        public int getColumna() {
            return columna;
        }
    }

    private List<Mensaje> mensajes;

    // Códigos de color ANSI
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public ReporteErrores() {
        this.mensajes = new ArrayList<>();
    }

    /**
     * Agrega un error al reporte
     */
    public void agregarError(String descripcion, int linea, int columna) {
        mensajes.add(new Mensaje(TipoMensaje.ERROR, descripcion, linea, columna));
    }

    /**
     * Agrega un warning al reporte
     */
    public void agregarWarning(String descripcion, int linea, int columna) {
        mensajes.add(new Mensaje(TipoMensaje.WARNING, descripcion, linea, columna));
    }

    /**
     * Agrega información al reporte
     */
    public void agregarInfo(String descripcion, int linea, int columna) {
        mensajes.add(new Mensaje(TipoMensaje.INFO, descripcion, linea, columna));
    }

    /**
     * Verifica si hay errores
     */
    public boolean tieneErrores() {
        return mensajes.stream().anyMatch(m -> m.getTipo() == TipoMensaje.ERROR);
    }

    /**
     * Verifica si hay warnings
     */
    public boolean tieneWarnings() {
        return mensajes.stream().anyMatch(m -> m.getTipo() == TipoMensaje.WARNING);
    }

    /**
     * Obtiene cantidad de errores
     */
    public int getCantidadErrores() {
        return (int) mensajes.stream().filter(m -> m.getTipo() == TipoMensaje.ERROR).count();
    }

    /**
     * Obtiene cantidad de warnings
     */
    public int getCantidadWarnings() {
        return (int) mensajes.stream().filter(m -> m.getTipo() == TipoMensaje.WARNING).count();
    }

    /**
     * Imprime el reporte con colores
     */
    public void imprimir() {
        if (mensajes.isEmpty()) {
            System.out.println(ANSI_GREEN + "✓ Sin errores ni warnings" + ANSI_RESET);
            return;
        }

        // Separar por tipo
        List<Mensaje> errores = new ArrayList<>();
        List<Mensaje> warnings = new ArrayList<>();
        List<Mensaje> infos = new ArrayList<>();

        for (Mensaje m : mensajes) {
            switch (m.getTipo()) {
                case ERROR:
                    errores.add(m);
                    break;
                case WARNING:
                    warnings.add(m);
                    break;
                case INFO:
                    infos.add(m);
                    break;
            }
        }

        // Imprimir errores
        if (!errores.isEmpty()) {
            System.out.println(ANSI_RED + ANSI_BOLD + "\n❌ ERRORES (" + errores.size() + "):" + ANSI_RESET);
            for (Mensaje error : errores) {
                System.out.printf("   %s[Línea %d:%d]%s %s\n",
                    ANSI_RED, error.getLinea(), error.getColumna(), ANSI_RESET, error.getDescripcion());
            }
        }

        // Imprimir warnings
        if (!warnings.isEmpty()) {
            System.out.println(ANSI_YELLOW + ANSI_BOLD + "\n⚠️  WARNINGS (" + warnings.size() + "):" + ANSI_RESET);
            for (Mensaje warning : warnings) {
                System.out.printf("   %s[Línea %d:%d]%s %s\n",
                    ANSI_YELLOW, warning.getLinea(), warning.getColumna(), ANSI_RESET, warning.getDescripcion());
            }
        }

        // Imprimir info
        if (!infos.isEmpty()) {
            System.out.println(ANSI_BLUE + ANSI_BOLD + "\nℹ️  INFORMACIÓN (" + infos.size() + "):" + ANSI_RESET);
            for (Mensaje info : infos) {
                System.out.printf("   %s[Línea %d:%d]%s %s\n",
                    ANSI_BLUE, info.getLinea(), info.getColumna(), ANSI_RESET, info.getDescripcion());
            }
        }

        System.out.println();
    }

    /**
     * Imprime resumen del reporte
     */
    public void imprimirResumen() {
        int errores = getCantidadErrores();
        int warnings = getCantidadWarnings();

        if (errores == 0 && warnings == 0) {
            System.out.println(ANSI_GREEN + "   ✓ Análisis semántico exitoso" + ANSI_RESET);
        } else {
            if (errores > 0) {
                System.out.println(ANSI_RED + "   ❌ Errores: " + errores + ANSI_RESET);
            }
            if (warnings > 0) {
                System.out.println(ANSI_YELLOW + "   ⚠️  Warnings: " + warnings + ANSI_RESET);
            }
        }
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }
}
