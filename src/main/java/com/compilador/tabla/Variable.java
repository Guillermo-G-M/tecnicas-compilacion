package com.compilador.tabla;

/**
 * Clase para representar variables
 */
public class Variable extends Id {

    private boolean esArray;
    private int sizeArray;

    public Variable(String nombre, String tipoDato, int linea, int columna, String ambito) {
        super(nombre, tipoDato, linea, columna, ambito);
        this.esArray = false;
        this.sizeArray = 0;
    }

    public Variable(String nombre, String tipoDato, int linea, int columna, String ambito, int sizeArray) {
        super(nombre, tipoDato, linea, columna, ambito);
        this.esArray = true;
        this.sizeArray = sizeArray;
    }

    // Getters
    public boolean isEsArray() {
        return esArray;
    }

    public int getsizeArray() {
        return sizeArray;
    }

    // Setters
    public void setEsArray(boolean esArray) {
        this.esArray = esArray;
    }

    public void setsizeArray(int sizeArray) {
        this.sizeArray = sizeArray;
        this.esArray = sizeArray > 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Variable - ").append(super.toString());
        if (esArray) {
            sb.append(", Array[").append(sizeArray).append("]");
        }
        return sb.toString();
    }
}
