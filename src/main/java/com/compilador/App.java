package com.compilador;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.compilador.gramatica.CompiladorLexer;
import com.compilador.gramatica.CompiladorParser;

/**
 * Aplicación principal del compilador C++
 * Trabajo Final - Técnicas de Compilación 2025
 */
public class App {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  Compilador C++ - Técnicas de Compilación");
        System.out.println("  Trabajo Final 2025");
        System.out.println("==============================================");
        System.out.println();

        if (args.length == 0) {
            System.out.println("Uso: mvn exec:java -Dexec.args=\"<archivo_entrada>\"");
            System.out.println("Ejemplo: mvn exec:java -Dexec.args=\"input/test_basico.cpp\"");
            return;
        }

        String archivoEntrada = args[0];
        System.out.println("Compilando archivo: " + archivoEntrada);
        System.out.println();

        try {
            // Crear CharStream desde archivo
            CharStream input = CharStreams.fromFileName(archivoEntrada);

            // Crear lexer
            CompiladorLexer lexer = new CompiladorLexer(input);

            // Crear stream de tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Crear parser
            CompiladorParser parser = new CompiladorParser(tokens);

            // Parsear el programa
            ParseTree tree = parser.programa();

            // Imprimir árbol sintáctico
            System.out.println("=== ANÁLISIS SINTÁCTICO EXITOSO ===");
            System.out.println("Árbol sintáctico generado:");
            System.out.println(tree.toStringTree(parser));
            System.out.println();

            System.out.println("✓ Compilación completada sin errores sintácticos");

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
