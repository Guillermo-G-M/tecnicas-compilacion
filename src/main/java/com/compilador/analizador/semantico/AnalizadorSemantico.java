package com.compilador.analizador.semantico;

import com.compilador.gramatica.CompiladorBaseListener;
import com.compilador.gramatica.CompiladorParser;
import com.compilador.tabla.Function;
import com.compilador.tabla.Id;
import com.compilador.tabla.SymbolTable;
import com.compilador.tabla.Variable;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Analizador semantico que extiende el listener de ANTLR
 * Realiza validaciones de tipos, declaraciones, uso de variables, etc.
 */
public class AnalizadorSemantico extends CompiladorBaseListener {

    private SymbolTable tablaSimbolos;
    private ReporteErrores reporte;
    private String contextoActual;
    private int contadorContextos;
    private Function funcionActual;

    public AnalizadorSemantico() {
        this.tablaSimbolos = SymbolTable.getInstance();
        this.reporte = new ReporteErrores();
        this.contextoActual = "global";
        this.contadorContextos = 0;
        this.funcionActual = null;

        // Inicializar contexto global
        tablaSimbolos.agregarContexto("global");
    }

    public SymbolTable getTablaSimbolos() {
        return tablaSimbolos;
    }

    public ReporteErrores getReporte() {
        return reporte;
    }

    // ========== GESTION DE AMBITOS ==========

    @Override
    public void enterBloque(CompiladorParser.BloqueContext ctx) {
        contadorContextos++;
        String nombreContexto = "bloque_" + contadorContextos;
        tablaSimbolos.agregarContexto(nombreContexto);
        contextoActual = nombreContexto;
    }

    @Override
    public void exitBloque(CompiladorParser.BloqueContext ctx) {
        // Validar variables no usadas ANTES de eliminar contexto
        validarVariablesNoUsadasEnContextoActual();

        tablaSimbolos.eliminarContexto();
        // Restaurar contexto anterior (simplificado)
        if (tablaSimbolos.getCantidadContextos() > 0) {
            List<String> nombres = tablaSimbolos.getNombresContextos();
            contextoActual = nombres.get(nombres.size() - 1);
        }
    }

    // ========== DECLARACIONES ==========

    @Override
    public void enterDeclaracion(CompiladorParser.DeclaracionContext ctx) {
        String tipo = ctx.tipodato().getText();
        String nombre = ctx.ID().getText();
        int linea = ctx.start.getLine();
        int columna = ctx.start.getCharPositionInLine();

        // Verificar si ya existe en el contexto actual
        if (tablaSimbolos.existeEnContextoActual(nombre)) {
            reporte.agregarError(
                "Variable '" + nombre + "' ya declarada en este ámbito",
                linea, columna
            );
            return;
        }

        // Determinar si es variable, array o función
        if (ctx.dec_types().dec_var() != null) {
            // Variable simple
            Variable var = new Variable(nombre, tipo, linea, columna, contextoActual);

            // Verificar si tiene inicialización
            if (ctx.dec_types().dec_var().inicializacion() != null &&
                ctx.dec_types().dec_var().inicializacion().EQ() != null) {
                var.setInicializada(true);
            }

            tablaSimbolos.agregarSimbolo(nombre, var);

        } else if (ctx.dec_types().dec_array() != null) {
            // Array
            String tamanoStr = ctx.dec_types().dec_array().NUMERO().getText();
            int tamano = Integer.parseInt(tamanoStr);
            Variable array = new Variable(nombre, tipo, linea, columna, contextoActual, tamano);

            // Verificar si tiene inicialización
            if (ctx.dec_types().dec_array().inicializacion_array() != null &&
                ctx.dec_types().dec_array().inicializacion_array().EQ() != null) {
                array.setInicializada(true);
            }

            tablaSimbolos.agregarSimbolo(nombre, array);

        } else if (ctx.dec_types().dec_f_blk() != null || ctx.dec_types().dec_f_pyc() != null) {
            // Función
            Function funcion = new Function(nombre, tipo, linea, columna, contextoActual);

            // Procesar parámetros si existen
            CompiladorParser.Dec_f_paramsContext paramsCtx = null;
            if (ctx.dec_types().dec_f_blk() != null) {
                paramsCtx = ctx.dec_types().dec_f_blk().dec_f_params();
            } else if (ctx.dec_types().dec_f_pyc() != null) {
                paramsCtx = ctx.dec_types().dec_f_pyc().dec_f_params();
            }

            if (paramsCtx != null && paramsCtx.params_f() != null) {
                procesarParametros(funcion, paramsCtx.params_f(), linea, columna);
            }

            funcion.setInicializada(true);
            tablaSimbolos.agregarSimbolo(nombre, funcion);

            // Si es una función con bloque, guardar referencia para agregar parámetros al contexto
            if (ctx.dec_types().dec_f_blk() != null) {
                funcionActual = funcion;
            }
        }
    }

    @Override
    public void enterDec_f_blk(CompiladorParser.Dec_f_blkContext ctx) {
        // Crear contexto para la función antes de entrar al bloque
        if (funcionActual != null) {
            contadorContextos++;
            String nombreContexto = "funcion_" + funcionActual.getNombre();
            tablaSimbolos.agregarContexto(nombreContexto);
            contextoActual = nombreContexto;

            // Agregar parámetros al contexto de la función
            for (Variable param : funcionActual.getParametros()) {
                tablaSimbolos.agregarSimbolo(param.getNombre(), param);
            }

            funcionActual = null;
        }
    }

    @Override
    public void exitDec_f_blk(CompiladorParser.Dec_f_blkContext ctx) {
        // Validar variables no usadas ANTES de eliminar contexto
        validarVariablesNoUsadasEnContextoActual();

        // Salir del contexto de la función
        tablaSimbolos.eliminarContexto();
        if (tablaSimbolos.getCantidadContextos() > 0) {
            List<String> nombres = tablaSimbolos.getNombresContextos();
            contextoActual = nombres.get(nombres.size() - 1);
        }
    }

    /**
     * Procesa los parámetros de una función
     */
    private void procesarParametros(Function funcion, CompiladorParser.Params_fContext paramsCtx,
                                    int linea, int columna) {
        // params_f : tipodato ID listaparams_f | ;
        if (paramsCtx.tipodato() != null && paramsCtx.ID() != null) {
            String tipoParam = paramsCtx.tipodato().getText();
            String nombreParam = paramsCtx.ID().getText();
            Variable param = new Variable(nombreParam, tipoParam, linea, columna, funcion.getNombre());
            param.setInicializada(true);
            funcion.agregarParametro(param);

            // Procesar resto de parámetros recursivamente
            if (paramsCtx.listaparams_f() != null) {
                procesarListaParametros(funcion, paramsCtx.listaparams_f(), linea, columna);
            }
        }
    }

    private void procesarListaParametros(Function funcion, CompiladorParser.Listaparams_fContext listaCtx,
                                         int linea, int columna) {
        // listaparams_f : COMA tipodato ID listaparams_f | ;
        if (listaCtx.tipodato() != null && listaCtx.ID() != null) {
            String tipoParam = listaCtx.tipodato().getText();
            String nombreParam = listaCtx.ID().getText();
            Variable param = new Variable(nombreParam, tipoParam, linea, columna, funcion.getNombre());
            param.setInicializada(true);
            funcion.agregarParametro(param);

            if (listaCtx.listaparams_f() != null) {
                procesarListaParametros(funcion, listaCtx.listaparams_f(), linea, columna);
            }
        }
    }

    // ========== ASIGNACIONES ==========

    @Override
    public void enterAsignacion(CompiladorParser.AsignacionContext ctx) {
        validarAsignacion(ctx.asign_type());
    }

    private void validarAsignacion(CompiladorParser.Asign_typeContext ctx) {
        if (ctx.ID() != null) {
            String nombre = ctx.ID().getText();
            int linea = ctx.start.getLine();
            int columna = ctx.start.getCharPositionInLine();

            // Verificar que la variable está declarada
            Id simbolo = tablaSimbolos.buscarSimbolo(nombre);
            if (simbolo == null) {
                reporte.agregarError(
                    "Variable '" + nombre + "' no declarada",
                    linea, columna
                );
            } else if (simbolo instanceof Function) {
                // Error específico: no se puede asignar a una función
                reporte.agregarError(
                    "No se puede asignar valor a '" + nombre + "' porque es una función",
                    linea, columna
                );
            } else {
                // Marcar como usada e inicializada
                simbolo.setUsada(true);
                simbolo.setInicializada(true);
            }
        }
    }

    // ========== USO DE VARIABLES EN EXPRESIONES ==========

    @Override
    public void enterFactor(CompiladorParser.FactorContext ctx) {
        // Verificar uso de ID en expresiones
        if (ctx.ID() != null) {
            String nombre = ctx.ID().getText();
            int linea = ctx.start.getLine();
            int columna = ctx.start.getCharPositionInLine();

            Id simbolo = tablaSimbolos.buscarSimbolo(nombre);
            if (simbolo == null) {
                reporte.agregarError(
                    "Variable '" + nombre + "' no declarada",
                    linea, columna
                );
            } else {
                // Marcar como usada
                simbolo.setUsada(true);

                // Warning si no está inicializada
                if (!simbolo.isInicializada()) {
                    reporte.agregarWarning(
                        "Variable '" + nombre + "' puede no estar inicializada",
                        linea, columna
                    );
                }
            }
        }
    }

    // ========== LLAMADAS A FUNCIONES ==========

    @Override
    public void enterCall_f(CompiladorParser.Call_fContext ctx) {
        String nombre = ctx.ID().getText();
        int linea = ctx.start.getLine();
        int columna = ctx.start.getCharPositionInLine();

        // Verificar que la función existe
        Id simbolo = tablaSimbolos.buscarSimbolo(nombre);
        if (simbolo == null) {
            reporte.agregarError(
                "Función '" + nombre + "' no declarada",
                linea, columna
            );
        } else if (!(simbolo instanceof Function)) {
            reporte.agregarError(
                "'" + nombre + "' no es una función",
                linea, columna
            );
        } else {
            simbolo.setUsada(true);
        }
    }

    // ========== VALIDACION FINAL ==========

    /**
     * Valida variables no usadas en el contexto actual ANTES de eliminarlo
     */
    private void validarVariablesNoUsadasEnContextoActual() {
        if (tablaSimbolos.getCantidadContextos() > 0) {
            List<Variable> noUsadas = tablaSimbolos.obtenerVariablesNoUsadasEnContextoActual();
            for (Variable var : noUsadas) {
                reporte.agregarWarning(
                    "Variable '" + var.getNombre() + "' declarada pero no usada",
                    var.getLinea(), var.getColumna()
                );
            }
        }
    }

    /**
     * Valida variables no usadas al finalizar el análisis (solo contexto global)
     */
    public void validarVariablesNoUsadas() {
        List<Variable> noUsadas = tablaSimbolos.obtenerVariablesNoUsadas();
        for (Variable var : noUsadas) {
            reporte.agregarWarning(
                "Variable '" + var.getNombre() + "' declarada pero no usada",
                var.getLinea(), var.getColumna()
            );
        }
    }
}
