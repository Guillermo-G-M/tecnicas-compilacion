package com.compilador.tabla;

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

    private SymbolTable() {
        this.contextos = new ArrayList<>();
        this.nombresContextos = new ArrayList<>();
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
        }
    }

    /**
     * Agrega un símbolo a un contexto específico
     */
    public void agregarSimbolo(String nombre, Id simbolo, int contextoPos) {
        if (contextoPos >= 0 && contextoPos < contextos.size()) {
            Map<String, Id> contexto = contextos.get(contextoPos);
            contexto.put(nombre, simbolo);
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

        if (contextos.isEmpty()) {
            System.out.println("(vacía)");
            return;
        }

        System.out.printf("%-15s %-15s %-10s %-12s %-8s %-8s %-8s%n",
                         "CONTEXTO", "NOMBRE", "TIPO", "CATEGORÍA", "INIT", "USADO", "POS");
        System.out.println("─".repeat(85));

        for (int i = 0; i < contextos.size(); i++) {
            String nombreContexto = nombresContextos.get(i);
            Map<String, Id> contexto = contextos.get(i);

            for (Map.Entry<String, Id> entry : contexto.entrySet()) {
                Id simbolo = entry.getValue();
                String categoria = simbolo instanceof Variable ? "Variable" : "Función";
                String posicion = simbolo.getLinea() + ":" + simbolo.getColumna();

                System.out.printf("%-15s %-15s %-10s %-12s %-8s %-8s %-8s%n",
                                 nombreContexto,
                                 simbolo.getNombre(),
                                 simbolo.getTipoDato(),
                                 categoria,
                                 simbolo.isInicializada() ? "Sí" : "No",
                                 simbolo.isUsada() ? "Sí" : "No",
                                 posicion);
            }
        }

        System.out.println();
    }

    /**
     * Obtiene la cantidad total de símbolos
     */
    public int getCantidadSimbolos() {
        int total = 0;
        for (Map<String, Id> contexto : contextos) {
            total += contexto.size();
        }
        return total;
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
}
