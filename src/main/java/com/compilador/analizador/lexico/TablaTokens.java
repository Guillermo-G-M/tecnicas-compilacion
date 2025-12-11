package com.compilador.analizador.lexico;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Tabla de tokens reconocidos por el analizador léxico
 */
public class TablaTokens {

    private final List<Token> tokens;
    private final Vocabulary vocabulary;

    public TablaTokens(List<Token> tokens, Vocabulary vocabulary) {
        this.tokens = tokens;
        this.vocabulary = vocabulary;
    }

    /**
     * Imprime la tabla de tokens en formato tabular
     */
    public void imprimir() {
        System.out.println("=== TABLA DE TOKENS ===");
        System.out.println();
        System.out.printf("%-8s %-20s %-25s %-8s %-8s%n",
                         "#", "TIPO", "LEXEMA", "LÍNEA", "COLUMNA");
        System.out.println("─".repeat(75));

        int contador = 1;
        for (Token token : tokens) {
            if (token.getType() == Token.EOF) {
                continue; // No mostrar EOF
            }

            String tipo = vocabulary.getSymbolicName(token.getType());
            String lexema = token.getText();
            int linea = token.getLine();
            int columna = token.getCharPositionInLine();

            // Truncar lexemas muy largos
            if (lexema.length() > 25) {
                lexema = lexema.substring(0, 22) + "...";
            }

            System.out.printf("%-8d %-20s %-25s %-8d %-8d%n",
                             contador++, tipo, lexema, linea, columna);
        }

        System.out.println();
        System.out.println("Total tokens procesados: " + (contador - 1));
        System.out.println();
    }

    /**
     * Retorna la cantidad de tokens (sin contar EOF)
     */
    public int getCantidad() {
        return (int) tokens.stream()
                          .filter(t -> t.getType() != Token.EOF)
                          .count();
    }

    /**
     * Guarda la tabla de tokens en un archivo
     * @param rutaArchivo Ruta del archivo de salida
     * @throws IOException Si hay error al escribir el archivo
     */
    public void guardarArchivo(String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("=== TABLA DE TOKENS ===");
            writer.println();
            writer.printf("%-8s %-20s %-25s %-8s %-8s%n",
                         "#", "TIPO", "LEXEMA", "LÍNEA", "COLUMNA");
            writer.println("─".repeat(75));

            int contador = 1;
            for (Token token : tokens) {
                if (token.getType() == Token.EOF) {
                    continue;
                }

                String tipo = vocabulary.getSymbolicName(token.getType());
                String lexema = token.getText();
                int linea = token.getLine();
                int columna = token.getCharPositionInLine();

                if (lexema.length() > 25) {
                    lexema = lexema.substring(0, 22) + "...";
                }

                writer.printf("%-8d %-20s %-25s %-8d %-8d%n",
                             contador++, tipo, lexema, linea, columna);
            }

            writer.println();
            writer.println("Total tokens procesados: " + (contador - 1));
        }
    }
}
