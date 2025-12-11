package com.compilador.optimizador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Optimizador de código intermedio (tres direcciones)
 * Aplica múltiples técnicas de optimización:
 * 1. Propagación de constantes
 * 2. Simplificación de expresiones
 * 3. Eliminación de código muerto
 * 4. Eliminación de redundancias
 */
public class Optimizador {

    private List<String> instruccionesOriginales;
    private List<String> instruccionesOptimizadas;
    private int instruccionesEliminadas;
    private Map<String, String> constantesPropagadas;
    private List<String> detallesEliminaciones;
    private List<String> detallesPropagaciones;
    private List<String> detallesSimplificaciones;

    public Optimizador(List<String> instrucciones) {
        this.instruccionesOriginales = new ArrayList<>(instrucciones);
        this.instruccionesOptimizadas = new ArrayList<>(instrucciones);
        this.instruccionesEliminadas = 0;
        this.constantesPropagadas = new HashMap<>();
        this.detallesEliminaciones = new ArrayList<>();
        this.detallesPropagaciones = new ArrayList<>();
        this.detallesSimplificaciones = new ArrayList<>();
    }

    /**
     * Ejecuta todas las optimizaciones
     */
    public void optimizar() {
        int pasadas = 0;
        int maxPasadas = 5;
        boolean cambios = true;

        while (cambios && pasadas < maxPasadas) {
            cambios = false;
            int tamañoAnterior = instruccionesOptimizadas.size();

            propagacionConstantes();
            simplificacionExpresiones();
            eliminacionRedundancias();
            eliminacionCodigoMuerto();
            eliminacionVariablesNoUsadas();

            if (instruccionesOptimizadas.size() < tamañoAnterior) {
                cambios = true;
            }

            pasadas++;
        }

        calcularMetricas();
    }

    /**
     * Optimización 1: Propagación de constantes
     * Detecta asignaciones de constantes y reemplaza su uso
     */
    private void propagacionConstantes() {
        Map<String, String> constantes = new HashMap<>();
        List<String> resultado = new ArrayList<>();

        for (String instr : instruccionesOptimizadas) {
            String instrOptimizada = instr;

            // Detectar asignaciones de constantes: temp = 5
            Pattern patternAsignConst = Pattern.compile("^(t\\d+)\\s*=\\s*(-?\\d+\\.?\\d*)$");
            Matcher matcherAsignConst = patternAsignConst.matcher(instr.trim());

            if (matcherAsignConst.matches()) {
                String temp = matcherAsignConst.group(1);
                String valor = matcherAsignConst.group(2);
                constantes.put(temp, valor);
                constantesPropagadas.put(temp, valor);
                resultado.add(instrOptimizada);
                continue;
            }

            // Reemplazar uso de temporales con constantes
            String instrOriginal = instrOptimizada;
            for (Map.Entry<String, String> entry : constantes.entrySet()) {
                String temp = entry.getKey();
                String valor = entry.getValue();

                // Reemplazar en expresiones: t1 + t2 -> t1 + 5
                instrOptimizada = instrOptimizada.replaceAll("\\b" + temp + "\\b", valor);
            }

            // Registrar si hubo propagación
            if (!instrOptimizada.equals(instrOriginal)) {
                detallesPropagaciones.add(String.format("  %s → %s",
                    instrOriginal.trim(), instrOptimizada.trim()));
            }

            // Si la instrucción cambió, invalidar temporales que usan esta variable
            if (!instrOptimizada.equals(instr)) {
                Pattern patternAsign = Pattern.compile("^(\\w+)\\s*=");
                Matcher matcherAsign = patternAsign.matcher(instr.trim());
                if (matcherAsign.find()) {
                    String varAsignada = matcherAsign.group(1);
                    constantes.remove(varAsignada);
                }
            }

            resultado.add(instrOptimizada);
        }

        instruccionesOptimizadas = resultado;
    }

    /**
     * Optimización 2: Simplificación de expresiones
     * Evalúa expresiones con constantes en tiempo de compilación
     */
    private void simplificacionExpresiones() {
        List<String> resultado = new ArrayList<>();

        for (String instr : instruccionesOptimizadas) {
            String instrOptimizada = instr;

            // Detectar expresiones aritméticas con constantes: t1 = 5 + 3
            Pattern patternArit = Pattern.compile("^(\\w+)\\s*=\\s*(-?\\d+\\.?\\d*)\\s*([+\\-*/%])\\s*(-?\\d+\\.?\\d*)$");
            Matcher matcherArit = patternArit.matcher(instr.trim());

            if (matcherArit.matches()) {
                String variable = matcherArit.group(1);
                double izq = Double.parseDouble(matcherArit.group(2));
                String operador = matcherArit.group(3);
                double der = Double.parseDouble(matcherArit.group(4));

                double resultado_calc = 0;
                boolean esEntero = !matcherArit.group(2).contains(".") && !matcherArit.group(4).contains(".");

                switch (operador) {
                    case "+": resultado_calc = izq + der; break;
                    case "-": resultado_calc = izq - der; break;
                    case "*": resultado_calc = izq * der; break;
                    case "/":
                        if (der != 0) resultado_calc = izq / der;
                        else {
                            resultado.add(instrOptimizada);
                            continue;
                        }
                        break;
                    case "%":
                        if (der != 0 && esEntero) resultado_calc = (int)izq % (int)der;
                        else {
                            resultado.add(instrOptimizada);
                            continue;
                        }
                        break;
                }

                if (esEntero && !operador.equals("/")) {
                    instrOptimizada = variable + " = " + (int)resultado_calc;
                } else {
                    instrOptimizada = variable + " = " + resultado_calc;
                }

                // Registrar simplificación
                if (!instrOptimizada.equals(instr.trim())) {
                    detallesSimplificaciones.add(String.format("  %s → %s",
                        instr.trim(), instrOptimizada));
                }
            }

            // Detectar comparaciones con constantes: t1 = 5 > 3
            Pattern patternComp = Pattern.compile("^(\\w+)\\s*=\\s*(-?\\d+\\.?\\d*)\\s*(==|!=|<|<=|>|>=)\\s*(-?\\d+\\.?\\d*)$");
            Matcher matcherComp = patternComp.matcher(instr.trim());

            if (matcherComp.matches()) {
                String variable = matcherComp.group(1);
                double izq = Double.parseDouble(matcherComp.group(2));
                String operador = matcherComp.group(3);
                double der = Double.parseDouble(matcherComp.group(4));

                boolean resultado_bool = false;
                switch (operador) {
                    case "==": resultado_bool = izq == der; break;
                    case "!=": resultado_bool = izq != der; break;
                    case "<": resultado_bool = izq < der; break;
                    case "<=": resultado_bool = izq <= der; break;
                    case ">": resultado_bool = izq > der; break;
                    case ">=": resultado_bool = izq >= der; break;
                }

                instrOptimizada = variable + " = " + (resultado_bool ? "1" : "0");

                // Registrar simplificación
                if (!instrOptimizada.equals(instr.trim())) {
                    detallesSimplificaciones.add(String.format("  %s → %s",
                        instr.trim(), instrOptimizada));
                }
            }

            resultado.add(instrOptimizada);
        }

        instruccionesOptimizadas = resultado;
    }

    /**
     * Optimización 3: Eliminación de código muerto
     * Elimina código después de goto/return incondicional hasta siguiente etiqueta
     */
    private void eliminacionCodigoMuerto() {
        List<String> resultado = new ArrayList<>();
        boolean enCodigoMuerto = false;
        int lineaActual = 0;

        for (String instr : instruccionesOptimizadas) {
            String instrTrim = instr.trim();

            // Detectar goto incondicional o return
            if (instrTrim.startsWith("goto ") && !instrTrim.startsWith("goto END_IF")) {
                resultado.add(instr);
                lineaActual++;
                enCodigoMuerto = true;
                continue;
            }

            if (instrTrim.startsWith("return")) {
                resultado.add(instr);
                lineaActual++;
                enCodigoMuerto = true;
                continue;
            }

            // Detectar etiquetas (termina código muerto)
            if (instrTrim.matches("^\\w+:$")) {
                enCodigoMuerto = false;
                resultado.add(instr);
                lineaActual++;
                continue;
            }

            // Si no estamos en código muerto, agregar instrucción
            if (!enCodigoMuerto) {
                resultado.add(instr);
                lineaActual++;
            } else {
                // Registrar código muerto eliminado
                detallesEliminaciones.add(String.format("  Línea %d: Código muerto: %s", lineaActual, instrTrim));
                lineaActual++;
            }
        }

        instruccionesOptimizadas = resultado;
    }

    /**
     * Optimización 4: Eliminación de redundancias
     * Elimina asignaciones inútiles y duplicadas
     */
    private void eliminacionRedundancias() {
        List<String> resultado = new ArrayList<>();
        Set<String> instruccionesVistas = new HashSet<>();
        int lineaActual = 0;

        for (String instr : instruccionesOptimizadas) {
            String instrTrim = instr.trim();

            // Eliminar asignaciones x = x
            Pattern patternAutoAsign = Pattern.compile("^(\\w+)\\s*=\\s*\\1$");
            Matcher matcherAutoAsign = patternAutoAsign.matcher(instrTrim);
            if (matcherAutoAsign.matches()) {
                detallesEliminaciones.add(String.format("  Línea %d: Asignación inútil: %s", lineaActual, instrTrim));
                lineaActual++;
                continue; // Saltar esta instrucción
            }

            // Eliminar instrucciones duplicadas consecutivas (excepto etiquetas, DECLARE, PARAM)
            if (!instrTrim.matches("^\\w+:$") &&
                !instrTrim.startsWith("DECLARE") &&
                !instrTrim.startsWith("PARAM") &&
                !instrTrim.startsWith("func_") &&
                !instrTrim.startsWith("PROGRAMA_")) {

                if (instruccionesVistas.contains(instrTrim)) {
                    detallesEliminaciones.add(String.format("  Línea %d: Duplicado: %s", lineaActual, instrTrim));
                    lineaActual++;
                    continue; // Saltar duplicado
                }
                instruccionesVistas.add(instrTrim);
            } else {
                // Resetear para nuevos bloques
                if (instrTrim.matches("^\\w+:$")) {
                    instruccionesVistas.clear();
                }
            }

            resultado.add(instr);
            lineaActual++;
        }

        instruccionesOptimizadas = resultado;
    }

    /**
     * Optimización 5: Eliminación de variables no usadas
     * Elimina declaraciones de variables que nunca se usan
     */
    private void eliminacionVariablesNoUsadas() {
        List<String> resultado = new ArrayList<>();
        Set<String> variablesDeclaradas = new HashSet<>();
        Set<String> variablesUsadas = new HashSet<>();

        // Primera pasada: identificar todas las variables declaradas y usadas
        for (String instr : instruccionesOptimizadas) {
            String instrTrim = instr.trim();

            // Detectar declaraciones: DECLARE nombre tipo
            if (instrTrim.startsWith("DECLARE ")) {
                String[] partes = instrTrim.split("\\s+");
                if (partes.length >= 2) {
                    String varNombre = partes[1];
                    // Si es array, extraer nombre sin []
                    if (varNombre.contains("[")) {
                        varNombre = varNombre.substring(0, varNombre.indexOf('['));
                    }
                    variablesDeclaradas.add(varNombre);
                }
            } else {
                // Buscar uso de variables en otras instrucciones
                for (String var : variablesDeclaradas) {
                    if (instrTrim.contains(var)) {
                        variablesUsadas.add(var);
                    }
                }
            }
        }

        // Segunda pasada: eliminar declaraciones no usadas
        int lineaActual = 0;
        for (String instr : instruccionesOptimizadas) {
            String instrTrim = instr.trim();

            if (instrTrim.startsWith("DECLARE ")) {
                String[] partes = instrTrim.split("\\s+");
                if (partes.length >= 2) {
                    String varNombre = partes[1];
                    // Si es array, extraer nombre sin []
                    if (varNombre.contains("[")) {
                        varNombre = varNombre.substring(0, varNombre.indexOf('['));
                    }

                    // Si no se usa, omitir
                    if (!variablesUsadas.contains(varNombre)) {
                        detallesEliminaciones.add(String.format("  Línea %d: Variable no usada: %s", lineaActual, instrTrim));
                        lineaActual++;
                        continue;
                    }
                }
            }

            resultado.add(instr);
            lineaActual++;
        }

        instruccionesOptimizadas = resultado;
    }

    /**
     * Calcula métricas de optimización
     */
    private void calcularMetricas() {
        instruccionesEliminadas = instruccionesOriginales.size() - instruccionesOptimizadas.size();
    }

    /**
     * Obtiene el código optimizado
     */
    public List<String> getInstruccionesOptimizadas() {
        return instruccionesOptimizadas;
    }

    /**
     * Obtiene cantidad de instrucciones originales
     */
    public int getCantidadOriginales() {
        return instruccionesOriginales.size();
    }

    /**
     * Obtiene cantidad de instrucciones optimizadas
     */
    public int getCantidadOptimizadas() {
        return instruccionesOptimizadas.size();
    }

    /**
     * Obtiene cantidad de instrucciones eliminadas
     */
    public int getInstruccionesEliminadas() {
        return instruccionesEliminadas;
    }

    /**
     * Obtiene porcentaje de reducción
     */
    public double getPorcentajeReduccion() {
        if (instruccionesOriginales.size() == 0) return 0.0;
        return ((double) instruccionesEliminadas / instruccionesOriginales.size()) * 100.0;
    }

    /**
     * Obtiene cantidad de constantes propagadas
     */
    public int getCantidadConstantesPropagadas() {
        return constantesPropagadas.size();
    }

    /**
     * Imprime el código optimizado
     */
    public void imprimir() {
        System.out.println("=== CÓDIGO OPTIMIZADO (TRES DIRECCIONES) ===");
        System.out.println();
        for (int i = 0; i < instruccionesOptimizadas.size(); i++) {
            System.out.printf("%3d: %s\n", i, instruccionesOptimizadas.get(i));
        }
        System.out.println();
        imprimirMetricas();
    }

    /**
     * Imprime métricas de optimización
     */
    public void imprimirMetricas() {
        System.out.println("=== MÉTRICAS DE OPTIMIZACIÓN ===");
        System.out.println("Instrucciones originales:     " + getCantidadOriginales());
        System.out.println("Instrucciones optimizadas:    " + getCantidadOptimizadas());
        System.out.println("Instrucciones eliminadas:     " + getInstruccionesEliminadas());
        System.out.println("Constantes propagadas:        " + getCantidadConstantesPropagadas());
        System.out.printf("Reducción:                    %.2f%%\n", getPorcentajeReduccion());
        System.out.println();

        // Detalles de eliminaciones
        if (!detallesEliminaciones.isEmpty()) {
            System.out.println("=== INSTRUCCIONES ELIMINADAS ===");
            for (String detalle : detallesEliminaciones) {
                System.out.println(detalle);
            }
            System.out.println();
        }

        // Detalles de propagaciones
        if (!detallesPropagaciones.isEmpty()) {
            System.out.println("=== PROPAGACIONES DE CONSTANTES ===");
            for (String detalle : detallesPropagaciones) {
                System.out.println(detalle);
            }
            System.out.println();
        }

        // Detalles de simplificaciones
        if (!detallesSimplificaciones.isEmpty()) {
            System.out.println("=== SIMPLIFICACIONES ===");
            for (String detalle : detallesSimplificaciones) {
                System.out.println(detalle);
            }
            System.out.println();
        }
    }

    /**
     * Guarda el código optimizado en un archivo
     */
    public void guardarArchivo(String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("=== CÓDIGO OPTIMIZADO (TRES DIRECCIONES) ===");
            writer.println();
            for (int i = 0; i < instruccionesOptimizadas.size(); i++) {
                writer.printf("%3d: %s\n", i, instruccionesOptimizadas.get(i));
            }
            writer.println();
            writer.println("Total instrucciones: " + instruccionesOptimizadas.size());
            writer.println();
            writer.println("=== MÉTRICAS DE OPTIMIZACIÓN ===");
            writer.println("Instrucciones originales:     " + getCantidadOriginales());
            writer.println("Instrucciones optimizadas:    " + getCantidadOptimizadas());
            writer.println("Instrucciones eliminadas:     " + getInstruccionesEliminadas());
            writer.println("Constantes propagadas:        " + getCantidadConstantesPropagadas());
            writer.printf("Reducción:                    %.2f%%\n", getPorcentajeReduccion());
            writer.println();

            // Detalles de eliminaciones
            if (!detallesEliminaciones.isEmpty()) {
                writer.println("=== INSTRUCCIONES ELIMINADAS ===");
                for (String detalle : detallesEliminaciones) {
                    writer.println(detalle);
                }
                writer.println();
            }

            // Detalles de propagaciones
            if (!detallesPropagaciones.isEmpty()) {
                writer.println("=== PROPAGACIONES DE CONSTANTES ===");
                for (String detalle : detallesPropagaciones) {
                    writer.println(detalle);
                }
                writer.println();
            }

            // Detalles de simplificaciones
            if (!detallesSimplificaciones.isEmpty()) {
                writer.println("=== SIMPLIFICACIONES ===");
                for (String detalle : detallesSimplificaciones) {
                    writer.println(detalle);
                }
                writer.println();
            }
        }
    }
}
