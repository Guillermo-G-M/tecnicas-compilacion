package com.compilador.tabla;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para representar funciones
 */
public class Function extends Id {

    private List<Variable> parametros;
    private String tipoRetorno;

    public Function(String nombre, String tipoRetorno, int linea, int columna, String ambito) {
        super(nombre, tipoRetorno, linea, columna, ambito);
        this.tipoRetorno = tipoRetorno;
        this.parametros = new ArrayList<>();
    }

    public Function(String nombre, String tipoRetorno, int linea, int columna, String ambito, List<Variable> parametros) {
        super(nombre, tipoRetorno, linea, columna, ambito);
        this.tipoRetorno = tipoRetorno;
        this.parametros = parametros != null ? parametros : new ArrayList<>();
    }

    // Getters
    public List<Variable> getParametros() {
        return parametros;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public int getCantidadParametros() {
        return parametros.size();
    }

    // Setters
    public void setParametros(List<Variable> parametros) {
        this.parametros = parametros;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public void agregarParametro(Variable parametro) {
        this.parametros.add(parametro);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function - ").append(super.toString());
        sb.append(", Tipo Retorno: ").append(tipoRetorno);
        sb.append(", Parámetros: [");
        for (int i = 0; i < parametros.size(); i++) {
            Variable param = parametros.get(i);
            sb.append(param.getTipoDato()).append(" ").append(param.getNombre());
            if (i < parametros.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
