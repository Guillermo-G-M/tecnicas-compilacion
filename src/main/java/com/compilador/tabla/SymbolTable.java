package com.compilador.tabla;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tabla de símbolos con soporte para múltiples contextos/ámbitos
 */
public class SymbolTable {

    private static SymbolTable instance;
    private List<Map<String, Id>> contextos;
    private List<String> nombresContextos;

    // Lista para preservar TODOS los símbolos agregados (para impresión)
    private List<RegistroSimbolo> todosLosSimbolos;

    private SymbolTable() {
        this.contextos = new ArrayList<>();
        this.nombresContextos = new ArrayList<>();
        this.todosLosSimbolos = new ArrayList<>();
    }

    /**
     * Clase interna para almacenar símbolo con su contexto
     */
    private static class RegistroSimbolo {
        Id simbolo;
        String contexto;

        RegistroSimbolo(Id simbolo, String contexto) {
            this.simbolo = simbolo;
            this.contexto = contexto;
        }
    }

    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    /**
     * Agrega un nuevo contexto a la tabla
     */
    public void agregarContexto(String nombreContexto) {
        this.contextos.add(new HashMap<>());
        this.nombresContextos.add(nombreContexto);
    }

    /**
     * Elimina el último contexto de la tabla
     */
    public void eliminarContexto() {
        if (!contextos.isEmpty()) {
            contextos.remove(contextos.size() - 1);
            nombresContextos.remove(nombresContextos.size() - 1);
        }
    }

    /**
     * Elimina un contexto específico por posición
     */
    public void eliminarContexto(int pos) {
        if (pos >= 0 && pos < contextos.size()) {
            contextos.remove(pos);
            nombresContextos.remove(pos);
        }
    }

    /**
     * Agrega un símbolo al contexto actual (último)
     */
    public void agregarSimbolo(String nombre, Id simbolo) {
        if (!contextos.isEmpty()) {
            Map<String, Id> contextoActual = contextos.get(contextos.size() - 1);
            contextoActual.put(nombre, simbolo);

            // Preservar para impresión
            String nombreContexto = nombresContextos.get(nombresContextos.size() - 1);
            todosLosSimbolos.add(new RegistroSimbolo(simbolo, nombreContexto));
        }
    }

    /**
     * Agrega un símbolo a un contexto específico
     */
    public void agregarSimbolo(String nombre, Id simbolo, int contextoPos) {
        if (contextoPos >= 0 && contextoPos < contextos.size()) {
            Map<String, Id> contexto = contextos.get(contextoPos);
            contexto.put(nombre, simbolo);

            // Preservar para impresión
            String nombreContexto = nombresContextos.get(contextoPos);
            todosLosSimbolos.add(new RegistroSimbolo(simbolo, nombreContexto));
        }
    }

    /**
     * Busca un símbolo por nombre en todos los contextos (desde el más reciente)
     */
    public Id buscarSimbolo(String nombre) {
        for (int i = contextos.size() - 1; i >= 0; i--) {
            Map<String, Id> contexto = contextos.get(i);
            if (contexto.containsKey(nombre)) {
                return contexto.get(nombre);
            }
        }
        return null;
    }

    /**
     * Busca un símbolo solo en el contexto actual
     */
    public Id buscarSimboloEnContextoActual(String nombre) {
        if (!contextos.isEmpty()) {
            Map<String, Id> contextoActual = contextos.get(contextos.size() - 1);
            return contextoActual.get(nombre);
        }
        return null;
    }

    /**
     * Verifica si un símbolo existe en el contexto actual
     */
    public boolean existeEnContextoActual(String nombre) {
        return buscarSimboloEnContextoActual(nombre) != null;
    }

    /**
     * Verifica si un símbolo está declarado en cualquier contexto accesible
     */
    public boolean existeDeclarada(String nombre) {
        return buscarSimbolo(nombre) != null;
    }

    /**
     * Obtiene todas las variables no usadas
     */
    public List<Variable> obtenerVariablesNoUsadas() {
        List<Variable> noUsadas = new ArrayList<>();
        for (Map<String, Id> contexto : contextos) {
            for (Id simbolo : contexto.values()) {
                if (simbolo instanceof Variable && !simbolo.isUsada()) {
                    noUsadas.add((Variable) simbolo);
                }
            }
        }
        return noUsadas;
    }

    /**
     * Obtiene variables no usadas en el contexto actual
     */
    public List<Variable> obtenerVariablesNoUsadasEnContextoActual() {
        List<Variable> noUsadas = new ArrayList<>();
        if (!contextos.isEmpty()) {
            Map<String, Id> contextoActual = contextos.get(contextos.size() - 1);
            for (Id simbolo : contextoActual.values()) {
                if (simbolo instanceof Variable && !simbolo.isUsada()) {
                    noUsadas.add((Variable) simbolo);
                }
            }
        }
        return noUsadas;
    }

    /**
     * Obtiene todas las variables no inicializadas
     */
    public List<Variable> obtenerVariablesNoInicializadas() {
        List<Variable> noInicializadas = new ArrayList<>();
        for (Map<String, Id> contexto : contextos) {
            for (Id simbolo : contexto.values()) {
                if (simbolo instanceof Variable && !simbolo.isInicializada()) {
                    noInicializadas.add((Variable) simbolo);
                }
            }
        }
        return noInicializadas;
    }

    /**
     * Imprime la tabla de símbolos en formato tabular
     */
    public void imprimir() {
        System.out.println("=== TABLA DE SÍMBOLOS ===");
        System.out.println();

        if (todosLosSimbolos.isEmpty()) {
            System.out.println("(vacía)");
            return;
        }

        System.out.printf("%-18s %-10s %-12s %-8s %-10s %-15s %-20s%n",
                         "NOMBRE", "TIPO", "CATEGORÍA", "LÍNEA", "COLUMNA", "ÁMBITO", "DETALLES");
        System.out.println("─".repeat(100));

        for (RegistroSimbolo registro : todosLosSimbolos) {
            Id simbolo = registro.simbolo;
            String ambito = registro.contexto;

            String categoria;
            String detalles = "[private]";

            if (simbolo instanceof Variable) {
                Variable var = (Variable) simbolo;
                categoria = var.isParametro() ? "parametro" : "variable";

                // Agregar detalles de array
                if (var.isEsArray()) {
                    detalles = "[arr:" + var.getsizeArray() + "] [private]";
                }
            } else if (simbolo instanceof Function) {
                categoria = "funcion";
                Function func = (Function) simbolo;

                // Agregar lista de parámetros
                StringBuilder paramsList = new StringBuilder("[private]");
                if (!func.getParametros().isEmpty()) {
                    paramsList.append(" [");
                    for (int i = 0; i < func.getParametros().size(); i++) {
                        paramsList.append(func.getParametros().get(i).getTipoDato());
                        if (i < func.getParametros().size() - 1) {
                            paramsList.append(", ");
                        }
                    }
                    paramsList.append("]");
                }
                detalles = paramsList.toString();
            } else {
                categoria = "desconocido";
            }

            System.out.printf("%-18s %-10s %-12s %-8d %-10d %-15s %-20s%n",
                             simbolo.getNombre(),
                             simbolo.getTipoDato(),
                             categoria,
                             simbolo.getLinea(),
                             simbolo.getColumna(),
                             ambito,
                             detalles);
        }

        System.out.println();
    }

    /**
     * Obtiene la cantidad total de símbolos
     * Retorna el tamaño de todosLosSimbolos para incluir símbolos
     * de contextos que ya fueron eliminados
     */
    public int getCantidadSimbolos() {
        return todosLosSimbolos.size();
    }

    /**
     * Obtiene la cantidad de contextos
     */
    public int getCantidadContextos() {
        return contextos.size();
    }

    public List<Map<String, Id>> getContextos() {
        return contextos;
    }

    public List<String> getNombresContextos() {
        return nombresContextos;
    }

    /**
     * Guarda la tabla de símbolos en un archivo
     * @param rutaArchivo Ruta del archivo de salida
     * @throws IOException Si hay error al escribir el archivo
     */
    public void guardarArchivo(String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("=== TABLA DE SÍMBOLOS ===");
            writer.println();

            if (todosLosSimbolos.isEmpty()) {
                writer.println("(vacía)");
                return;
            }

            writer.printf("%-18s %-10s %-12s %-8s %-10s %-15s %-20s%n",
                         "NOMBRE", "TIPO", "CATEGORÍA", "LÍNEA", "COLUMNA", "ÁMBITO", "DETALLES");
            writer.println("─".repeat(100));

            for (RegistroSimbolo registro : todosLosSimbolos) {
                Id simbolo = registro.simbolo;
                String ambito = registro.contexto;

                String categoria;
                String detalles = "[private]";

                if (simbolo instanceof Variable) {
                    Variable var = (Variable) simbolo;
                    categoria = var.isParametro() ? "parametro" : "variable";

                    if (var.isEsArray()) {
                        detalles = "[arr:" + var.getsizeArray() + "] [private]";
                    }
                } else if (simbolo instanceof Function) {
                    categoria = "funcion";
                    Function func = (Function) simbolo;

                    StringBuilder paramsList = new StringBuilder("[private]");
                    if (!func.getParametros().isEmpty()) {
                        paramsList.append(" [");
                        for (int i = 0; i < func.getParametros().size(); i++) {
                            paramsList.append(func.getParametros().get(i).getTipoDato());
                            if (i < func.getParametros().size() - 1) {
                                paramsList.append(", ");
                            }
                        }
                        paramsList.append("]");
                    }
                    detalles = paramsList.toString();
                } else {
                    categoria = "desconocido";
                }

                writer.printf("%-18s %-10s %-12s %-8d %-10d %-15s %-20s%n",
                             simbolo.getNombre(),
                             simbolo.getTipoDato(),
                             categoria,
                             simbolo.getLinea(),
                             simbolo.getColumna(),
                             ambito,
                             detalles);
            }

            writer.println();
            writer.println("Total símbolos: " + todosLosSimbolos.size());
        }
    }
}
