package com.compilador.analizador;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener personalizado para capturar errores léxicos y sintácticos
 */
public class ErrorListener extends BaseErrorListener {

    private List<String> errores;
    private boolean tieneErrores;

    public ErrorListener() {
        this.errores = new ArrayList<>();
        this.tieneErrores = false;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                           Object offendingSymbol,
                           int line,
                           int charPositionInLine,
                           String msg,
                           RecognitionException e) {
        tieneErrores = true;
        String error = String.format("[Línea %d:%d] %s", line, charPositionInLine, msg);
        errores.add(error);
    }

    public boolean tieneErrores() {
        return tieneErrores;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void imprimir() {
        if (!errores.isEmpty()) {
            System.err.println("\n❌ ERRORES SINTÁCTICOS:");
            for (String error : errores) {
                System.err.println("   " + error);
            }
            System.err.println();
        }
    }
}
