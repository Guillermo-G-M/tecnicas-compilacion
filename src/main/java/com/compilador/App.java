package com.compilador;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.compilador.gramatica.CompiladorLexer;
import com.compilador.gramatica.CompiladorParser;
import com.compilador.analizador.lexico.TablaTokens;
import com.compilador.analizador.sintactico.VisualizadorAST;

/**
 * Aplicación principal del compilador C++
 * Trabajo Final - Técnicas de Compilación 2025
 */
public class App {

    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════════");
        System.out.println("  Compilador C++ - Técnicas de Compilación");
        System.out.println("  Trabajo Final 2025");
        System.out.println("══════════════════════════════════════════════");
        System.out.println();

        if (args.length == 0) {
            System.out.println("Uso: mvn exec:java -Dexec.args=\"<archivo_entrada>\"");
            System.out.println("Ejemplo: mvn exec:java -Dexec.args=\"input/test_basico.cpp\"");
            return;
        }

        String archivoEntrada = args[0];
        System.out.println("📁 Archivo: " + archivoEntrada);
        System.out.println();

        try {
            // ===== FASE 1: ANÁLISIS LÉXICO =====
            System.out.println("═══ 1. ANÁLISIS LÉXICO ═══");

            CharStream input = CharStreams.fromFileName(archivoEntrada);
            CompiladorLexer lexer = new CompiladorLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            // Mostrar tabla de tokens
            TablaTokens tablaTokens = new TablaTokens(tokens.getTokens(), lexer.getVocabulary());
            tablaTokens.imprimir();

            System.out.println("✅ Análisis léxico completado");
            System.out.println("   📊 Tokens procesados: " + tablaTokens.getCantidad());
            System.out.println();

            // ===== FASE 2: ANÁLISIS SINTÁCTICO =====
            System.out.println("═══ 2. ANÁLISIS SINTÁCTICO ═══");

            // Resetear tokens para el parser
            tokens.seek(0);
            CompiladorParser parser = new CompiladorParser(tokens);

            // Parsear programa
            ParseTree tree = parser.programa();

            // Visualizar AST
            VisualizadorAST visualizador = new VisualizadorAST(tree, parser);
            visualizador.imprimirArbolLisp();

            System.out.println("✅ Análisis sintáctico completado");
            System.out.println("   📊 Nodos en AST: " + visualizador.contarNodos());
            System.out.println();

            // ===== RESUMEN =====
            System.out.println("═══ RESUMEN DE COMPILACIÓN ═══");
            System.out.println("📁 Archivo procesado: " + archivoEntrada);
            System.out.println("🔤 Tokens analizados: " + tablaTokens.getCantidad());
            System.out.println("🌳 Nodos en AST: " + visualizador.contarNodos());
            System.out.println();
            System.out.println("🎉 ¡COMPILACIÓN EXITOSA!");

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

