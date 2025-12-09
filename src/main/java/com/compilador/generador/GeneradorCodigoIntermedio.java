package com.compilador.generador;

import com.compilador.gramatica.CompiladorBaseVisitor;
import com.compilador.gramatica.CompiladorParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Visitor para generar código de tres direcciones a partir del AST
 */
public class GeneradorCodigoIntermedio extends CompiladorBaseVisitor<String> {

    private List<String> instrucciones;
    private int contadorTemporales;
    private int contadorEtiquetas;
    private String funcionActual;

    public GeneradorCodigoIntermedio() {
        this.instrucciones = new ArrayList<>();
        this.contadorTemporales = 0;
        this.contadorEtiquetas = 0;
        this.funcionActual = null;
    }

    public List<String> getInstrucciones() {
        return instrucciones;
    }

    /**
     * Genera un nuevo temporal
     */
    private String nuevoTemporal() {
        return "t" + (++contadorTemporales);
    }

    /**
     * Genera una nueva etiqueta
     */
    private String nuevaEtiqueta(String prefijo) {
        return prefijo + "_" + (++contadorEtiquetas);
    }

    /**
     * Agrega una instrucción al código intermedio
     */
    private void emitir(String instruccion) {
        instrucciones.add(instruccion);
    }

    // ========== PROGRAMA ==========

    @Override
    public String visitPrograma(CompiladorParser.ProgramaContext ctx) {
        emitir("// Código de tres direcciones generado");
        emitir("PROGRAMA_INICIO:");

        if (ctx.instrucciones() != null) {
            visit(ctx.instrucciones());
        }

        emitir("PROGRAMA_FIN:");
        return null;
    }

    @Override
    public String visitInstrucciones(CompiladorParser.InstruccionesContext ctx) {
        if (ctx.instruccion() != null) {
            visit(ctx.instruccion());
        }
        if (ctx.instrucciones() != null) {
            visit(ctx.instrucciones());
        }
        return null;
    }

    @Override
    public String visitInstruccion(CompiladorParser.InstruccionContext ctx) {
        if (ctx.declaracion() != null) {
            return visit(ctx.declaracion());
        } else if (ctx.asignacion() != null) {
            return visit(ctx.asignacion());
        } else if (ctx.if_i() != null) {
            return visit(ctx.if_i());
        } else if (ctx.while_i() != null) {
            return visit(ctx.while_i());
        } else if (ctx.for_i() != null) {
            return visit(ctx.for_i());
        } else if (ctx.return_i() != null) {
            return visit(ctx.return_i());
        } else if (ctx.bloque() != null) {
            return visit(ctx.bloque());
        } else if (ctx.call_f() != null) {
            visit(ctx.call_f());
        }
        return null;
    }

    // ========== DECLARACIONES ==========

    @Override
    public String visitDeclaracion(CompiladorParser.DeclaracionContext ctx) {
        String tipo = ctx.tipodato().getText();
        String nombre = ctx.ID().getText();

        if (ctx.dec_types().dec_var() != null) {
            if (funcionActual == null) {
                emitir("// Declaración de variables globales");
            }
            emitir("DECLARE " + nombre + " " + tipo);

            if (ctx.dec_types().dec_var().inicializacion() != null &&
                ctx.dec_types().dec_var().inicializacion().expresion() != null) {
                String valorExpr = visit(ctx.dec_types().dec_var().inicializacion().expresion());
                emitir(nombre + " = " + valorExpr);
            }

        } else if (ctx.dec_types().dec_array() != null) {
            String tamano = ctx.dec_types().dec_array().NUMERO().getText();
            emitir("DECLARE " + nombre + "[" + tamano + "] " + tipo);

        } else if (ctx.dec_types().dec_f_blk() != null) {
            funcionActual = nombre;
            emitir("func_" + nombre + ":");

            if (ctx.dec_types().dec_f_blk().dec_f_params() != null &&
                ctx.dec_types().dec_f_blk().dec_f_params().params_f() != null) {
                visitParams_f(ctx.dec_types().dec_f_blk().dec_f_params().params_f());
            }

            visit(ctx.dec_types().dec_f_blk().bloque());
            funcionActual = null;

        } else if (ctx.dec_types().dec_f_pyc() != null) {
            emitir("// Prototipo: " + tipo + " " + nombre);
        }

        return null;
    }

    @Override
    public String visitParams_f(CompiladorParser.Params_fContext ctx) {
        if (ctx.tipodato() != null && ctx.ID() != null) {
            String tipo = ctx.tipodato().getText();
            String nombre = ctx.ID().getText();
            emitir("PARAM " + nombre + " " + tipo);

            if (ctx.listaparams_f() != null) {
                visitListaparams_f(ctx.listaparams_f());
            }
        }
        return null;
    }

    @Override
    public String visitListaparams_f(CompiladorParser.Listaparams_fContext ctx) {
        if (ctx.tipodato() != null && ctx.ID() != null) {
            String tipo = ctx.tipodato().getText();
            String nombre = ctx.ID().getText();
            emitir("PARAM " + nombre + " " + tipo);

            if (ctx.listaparams_f() != null) {
                visitListaparams_f(ctx.listaparams_f());
            }
        }
        return null;
    }

    // ========== ASIGNACIONES ==========

    @Override
    public String visitAsignacion(CompiladorParser.AsignacionContext ctx) {
        return visitAsign_type(ctx.asign_type());
    }

    @Override
    public String visitAsign_type(CompiladorParser.Asign_typeContext ctx) {
        if (ctx.ID() != null) {
            String variable = ctx.ID().getText();

            if (ctx.expresion().size() == 1) {
                String valorExpr = visit(ctx.expresion(0));
                emitir(variable + " = " + valorExpr);
            } else if (ctx.expresion().size() == 2) {
                String indice = visit(ctx.expresion(0));
                String valor = visit(ctx.expresion(1));
                emitir(variable + "[" + indice + "] = " + valor);
            }
        } else if (ctx.id_factor() != null) {
            return visit(ctx.id_factor());
        }
        return null;
    }

    // ========== EXPRESIONES ==========

    @Override
    public String visitExpresion(CompiladorParser.ExpresionContext ctx) {
        return visit(ctx.expresion_or());
    }

    @Override
    public String visitExpresion_or(CompiladorParser.Expresion_orContext ctx) {
        if (ctx.OR() != null) {
            String izq = visit(ctx.expresion_or());
            String der = visit(ctx.expresion_and());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " || " + der);
            return temp;
        }
        return visit(ctx.expresion_and());
    }

    @Override
    public String visitExpresion_and(CompiladorParser.Expresion_andContext ctx) {
        if (ctx.AND() != null) {
            String izq = visit(ctx.expresion_and());
            String der = visit(ctx.expresion_neg());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " && " + der);
            return temp;
        }
        return visit(ctx.expresion_neg());
    }

    @Override
    public String visitExpresion_neg(CompiladorParser.Expresion_negContext ctx) {
        if (ctx.NEG() != null) {
            String expr = visit(ctx.expresion_neg());
            String temp = nuevoTemporal();
            emitir(temp + " = !" + expr);
            return temp;
        }
        return visit(ctx.expresion_comp());
    }

    @Override
    public String visitExpresion_comp(CompiladorParser.Expresion_compContext ctx) {
        if (ctx.comparaciones() != null) {
            String izq = visit(ctx.expresion_comp());
            String op = ctx.comparaciones().getText();
            String der = visit(ctx.expresion_arit());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " " + op + " " + der);
            return temp;
        }
        return visit(ctx.expresion_arit());
    }

    @Override
    public String visitExpresion_arit(CompiladorParser.Expresion_aritContext ctx) {
        if (ctx.SUMA() != null) {
            String izq = visit(ctx.expresion_arit());
            String der = visit(ctx.termino());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " + " + der);
            return temp;
        } else if (ctx.RESTA() != null) {
            String izq = visit(ctx.expresion_arit());
            String der = visit(ctx.termino());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " - " + der);
            return temp;
        }
        return visit(ctx.termino());
    }

    @Override
    public String visitTermino(CompiladorParser.TerminoContext ctx) {
        if (ctx.MULT() != null) {
            String izq = visit(ctx.termino());
            String der = visit(ctx.factor());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " * " + der);
            return temp;
        } else if (ctx.DIV() != null) {
            String izq = visit(ctx.termino());
            String der = visit(ctx.factor());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " / " + der);
            return temp;
        } else if (ctx.MOD() != null) {
            String izq = visit(ctx.termino());
            String der = visit(ctx.factor());
            String temp = nuevoTemporal();
            emitir(temp + " = " + izq + " % " + der);
            return temp;
        }
        return visit(ctx.factor());
    }

    @Override
    public String visitFactor(CompiladorParser.FactorContext ctx) {
        String signo = "";
        if (ctx.sign_factor() != null && ctx.sign_factor().getText().length() > 0) {
            signo = ctx.sign_factor().getText();
        }

        if (ctx.NUMERO() != null) {
            String valor = ctx.NUMERO().getText();
            if (!signo.isEmpty()) {
                String temp = nuevoTemporal();
                emitir(temp + " = " + signo + valor);
                return temp;
            }
            return valor;

        } else if (ctx.DECIMAL() != null) {
            String valor = ctx.DECIMAL().getText();
            if (!signo.isEmpty()) {
                String temp = nuevoTemporal();
                emitir(temp + " = " + signo + valor);
                return temp;
            }
            return valor;

        } else if (ctx.ID() != null) {
            String nombre = ctx.ID().getText();
            if (ctx.expresion() != null) {
                String indice = visit(ctx.expresion());
                String temp = nuevoTemporal();
                emitir(temp + " = " + nombre + "[" + indice + "]");
                if (!signo.isEmpty()) {
                    String temp2 = nuevoTemporal();
                    emitir(temp2 + " = " + signo + temp);
                    return temp2;
                }
                return temp;
            }
            if (!signo.isEmpty()) {
                String temp = nuevoTemporal();
                emitir(temp + " = " + signo + nombre);
                return temp;
            }
            return nombre;

        } else if (ctx.call_f_factor() != null) {
            return visit(ctx.call_f_factor());

        } else if (ctx.expresion() != null) {
            String valor = visit(ctx.expresion());
            if (!signo.isEmpty()) {
                String temp = nuevoTemporal();
                emitir(temp + " = " + signo + valor);
                return temp;
            }
            return valor;

        } else if (ctx.id_factor() != null) {
            return visit(ctx.id_factor());

        } else if (ctx.CHAR_LITERAL() != null) {
            return ctx.CHAR_LITERAL().getText();

        } else if (ctx.TRUE() != null) {
            return "true";

        } else if (ctx.FALSE() != null) {
            return "false";
        }

        return "0";
    }

    @Override
    public String visitId_factor(CompiladorParser.Id_factorContext ctx) {
        String nombre = ctx.ID().getText();
        String op = ctx.inc_dec().getText();

        if (ctx.inc_dec() != null && ctx.getChild(0) == ctx.inc_dec()) {
            emitir(nombre + " = " + nombre + " " + (op.equals("++") ? "+" : "-") + " 1");
        } else if (ctx.inc_dec() != null && ctx.getChild(1) == ctx.inc_dec()) {
            String temp = nuevoTemporal();
            emitir(temp + " = " + nombre);
            emitir(nombre + " = " + nombre + " " + (op.equals("++") ? "+" : "-") + " 1");
            return temp;
        }
        return nombre;
    }

    @Override
    public String visitCall_f_factor(CompiladorParser.Call_f_factorContext ctx) {
        String nombreFuncion = ctx.ID().getText();

        List<String> args = new ArrayList<>();
        if (ctx.params_call_f() != null) {
            procesarArgumentos(ctx.params_call_f(), args);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CALL func_").append(nombreFuncion);
        for (String arg : args) {
            sb.append(", ").append(arg);
        }
        emitir(sb.toString());

        String temp = nuevoTemporal();
        emitir(temp + " = RETURN_VALUE");
        return temp;
    }

    @Override
    public String visitCall_f(CompiladorParser.Call_fContext ctx) {
        String nombreFuncion = ctx.ID().getText();

        List<String> args = new ArrayList<>();
        if (ctx.params_call_f() != null) {
            procesarArgumentos(ctx.params_call_f(), args);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CALL func_").append(nombreFuncion);
        for (String arg : args) {
            sb.append(", ").append(arg);
        }
        emitir(sb.toString());
        return null;
    }

    private void procesarArgumentos(CompiladorParser.Params_call_fContext ctx, List<String> args) {
        if (ctx.expresion() != null) {
            String arg = visit(ctx.expresion());
            args.add(arg);

            if (ctx.listaexpresion() != null) {
                procesarListaArgumentos(ctx.listaexpresion(), args);
            }
        }
    }

    private void procesarListaArgumentos(CompiladorParser.ListaexpresionContext ctx, List<String> args) {
        if (ctx.expresion() != null) {
            String arg = visit(ctx.expresion());
            args.add(arg);

            if (ctx.listaexpresion() != null) {
                procesarListaArgumentos(ctx.listaexpresion(), args);
            }
        }
    }

    // ========== ESTRUCTURAS DE CONTROL ==========

    @Override
    public String visitIf_i(CompiladorParser.If_iContext ctx) {
        String condicion = visit(ctx.expresion());

        String etiqThen = nuevaEtiqueta("THEN");
        String etiqEndIf = nuevaEtiqueta("END_IF");

        emitir("if " + condicion + " goto " + etiqThen);

        if (ctx.else_if() != null && ctx.else_if().instruccion() != null) {
            visit(ctx.else_if().instruccion());
        }

        emitir("goto " + etiqEndIf);
        emitir(etiqThen + ":");

        visit(ctx.instruccion());

        emitir(etiqEndIf + ":");
        return null;
    }

    @Override
    public String visitWhile_i(CompiladorParser.While_iContext ctx) {
        String etiqInicio = nuevaEtiqueta("WHILE_START");
        String etiqFin = nuevaEtiqueta("WHILE_END");

        emitir(etiqInicio + ":");

        String condicion = visit(ctx.expresion());
        emitir("if !" + condicion + " goto " + etiqFin);

        visit(ctx.instruccion());

        emitir("goto " + etiqInicio);
        emitir(etiqFin + ":");
        return null;
    }

    @Override
    public String visitFor_i(CompiladorParser.For_iContext ctx) {
        if (ctx.expresion_for(0) != null) {
            visit(ctx.expresion_for(0));
        }

        String etiqInicio = nuevaEtiqueta("FOR_START");
        String etiqFin = nuevaEtiqueta("FOR_END");

        emitir(etiqInicio + ":");

        if (ctx.expresion_for(1) != null) {
            String condicion = visit(ctx.expresion_for(1));
            emitir("if !" + condicion + " goto " + etiqFin);
        }

        visit(ctx.instruccion());

        if (ctx.expresion_for(2) != null) {
            visit(ctx.expresion_for(2));
        }

        emitir("goto " + etiqInicio);
        emitir(etiqFin + ":");
        return null;
    }

    @Override
    public String visitExpresion_for(CompiladorParser.Expresion_forContext ctx) {
        if (ctx.exp_for() != null) {
            return visit(ctx.exp_for());
        }
        return null;
    }

    @Override
    public String visitExp_for(CompiladorParser.Exp_forContext ctx) {
        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        } else if (ctx.asignacion_npyc() != null) {
            return visit(ctx.asignacion_npyc());
        }
        return null;
    }

    @Override
    public String visitAsignacion_npyc(CompiladorParser.Asignacion_npycContext ctx) {
        if (ctx.ID() != null) {
            String variable = ctx.ID().getText();

            if (ctx.expresion().size() == 1) {
                String valorExpr = visit(ctx.expresion(0));
                emitir(variable + " = " + valorExpr);
            } else if (ctx.expresion().size() == 2) {
                String indice = visit(ctx.expresion(0));
                String valor = visit(ctx.expresion(1));
                emitir(variable + "[" + indice + "] = " + valor);
            }
        }
        return null;
    }

    @Override
    public String visitReturn_i(CompiladorParser.Return_iContext ctx) {
        if (ctx.expresion() != null) {
            String valor = visit(ctx.expresion());
            emitir("return " + valor);
        } else {
            emitir("return");
        }
        return null;
    }

    @Override
    public String visitBloque(CompiladorParser.BloqueContext ctx) {
        if (ctx.instrucciones() != null) {
            visit(ctx.instrucciones());
        }
        return null;
    }

    /**
     * Imprime el código intermedio generado
     */
    public void imprimir() {
        System.out.println("=== CÓDIGO INTERMEDIO (TRES DIRECCIONES) ===");
        System.out.println();
        for (int i = 0; i < instrucciones.size(); i++) {
            System.out.printf("%3d: %s\n", i, instrucciones.get(i));
        }
        System.out.println();
        System.out.println("Total instrucciones: " + instrucciones.size());
        System.out.println();
    }

    /**
     * Guarda el código intermedio en un archivo
     */
    public void guardarArchivo(String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("=== CÓDIGO INTERMEDIO (TRES DIRECCIONES) ===");
            writer.println();
            for (int i = 0; i < instrucciones.size(); i++) {
                writer.printf("%3d: %s\n", i, instrucciones.get(i));
            }
            writer.println();
            writer.println("Total instrucciones: " + instrucciones.size());
        }
    }
}
