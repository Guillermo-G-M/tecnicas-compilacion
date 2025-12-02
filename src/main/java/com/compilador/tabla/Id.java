package com.compilador.tabla;

/**
 * Clase base abstracta para identificadores (variables y funciones)
 */
public abstract class Id {

    private String nombre;
    private String tipoDato;
    private boolean inicializada;
    private boolean usada;
    private int linea;
    private int columna;
    private String ambito;

    public Id(String nombre, String tipoDato, int linea, int columna, String ambito) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.linea = linea;
        this.columna = columna;
        this.ambito = ambito;
        this.inicializada = false;
        this.usada = false;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public boolean isInicializada() {
        return inicializada;
    }

    public boolean isUsada() {
        return usada;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getAmbito() {
        return ambito;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public void setInicializada(boolean inicializada) {
        this.inicializada = inicializada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre +
               ", Tipo: " + tipoDato +
               ", Inicializada: " + inicializada +
               ", Usada: " + usada +
               ", Línea: " + linea +
               ", Columna: " + columna +
               ", Ámbito: " + ambito;
    }
}
