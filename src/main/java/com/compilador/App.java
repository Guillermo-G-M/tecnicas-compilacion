package com.compilador;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.compilador.gramatica.CompiladorLexer;
import com.compilador.gramatica.CompiladorParser;
import com.compilador.analizador.lexico.TablaTokens;
import com.compilador.analizador.sintactico.VisualizadorAST;
import com.compilador.analizador.semantico.AnalizadorSemantico;
import com.compilador.analizador.semantico.ReporteErrores;
import com.compilador.analizador.ErrorListener;
import com.compilador.tabla.SymbolTable;
import com.compilador.generador.GeneradorCodigoIntermedio;

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

            // Agregar listener de errores al lexer
            ErrorListener errorListenerLexico = new ErrorListener();
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListenerLexico);

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            // Mostrar tabla de tokens
            TablaTokens tablaTokens = new TablaTokens(tokens.getTokens(), lexer.getVocabulary());
            tablaTokens.imprimir();

            // Imprimir errores léxicos si existen
            if (errorListenerLexico.tieneErrores()) {
                errorListenerLexico.imprimir();
            }

            System.out.println("✅ Análisis léxico completado");
            System.out.println("   📊 Tokens procesados: " + tablaTokens.getCantidad());
            if (errorListenerLexico.tieneErrores()) {
                System.out.println("   ❌ Errores léxicos: " + errorListenerLexico.getErrores().size());
            }
            System.out.println();

            // ===== FASE 2: ANÁLISIS SINTÁCTICO =====
            System.out.println("═══ 2. ANÁLISIS SINTÁCTICO ═══");

            // Resetear tokens para el parser
            tokens.seek(0);
            CompiladorParser parser = new CompiladorParser(tokens);

            // Agregar listener de errores al parser
            ErrorListener errorListenerSintactico = new ErrorListener();
            parser.removeErrorListeners();
            parser.addErrorListener(errorListenerSintactico);

            // Parsear programa
            ParseTree tree = parser.programa();

            // Visualizar AST
            VisualizadorAST visualizador = new VisualizadorAST(tree, parser);
            visualizador.imprimirArbolLisp();

            // Imprimir errores sintácticos si existen
            if (errorListenerSintactico.tieneErrores()) {
                errorListenerSintactico.imprimir();
            }

            System.out.println("✅ Análisis sintáctico completado");
            System.out.println("   📊 Nodos en AST: " + visualizador.contarNodos());
            if (errorListenerSintactico.tieneErrores()) {
                System.out.println("   ❌ Errores sintácticos: " + errorListenerSintactico.getErrores().size());
            }
            System.out.println();

            // Verificar si hay errores antes de continuar
            boolean hayErroresLexSin = errorListenerLexico.tieneErrores() || errorListenerSintactico.tieneErrores();

            // ===== FASE 3: ANÁLISIS SEMÁNTICO =====
            SymbolTable tablaSimbolos = null;
            ReporteErrores reporte = null;
            GeneradorCodigoIntermedio generador = null;

            if (!hayErroresLexSin) {
                System.out.println("═══ 3. ANÁLISIS SEMÁNTICO ═══");

                // Resetear tabla de símbolos
                SymbolTable.resetInstance();

                // Crear analizador semántico
                AnalizadorSemantico analizador = new AnalizadorSemantico();

                // Recorrer el AST con el listener
                ParseTreeWalker walker = new ParseTreeWalker();
                walker.walk(analizador, tree);

                // Validar variables no usadas
                analizador.validarVariablesNoUsadas();

                // Mostrar tabla de símbolos
                tablaSimbolos = analizador.getTablaSimbolos();
                tablaSimbolos.imprimir();

                System.out.println("✅ Análisis semántico completado");
                System.out.println("   📊 Símbolos en tabla: " + tablaSimbolos.getCantidadSimbolos());
                System.out.println("   📊 Contextos: " + tablaSimbolos.getCantidadContextos());

                // Mostrar reporte de errores/warnings
                reporte = analizador.getReporte();
                reporte.imprimir();

                System.out.println();

                // ===== FASE 4: GENERACIÓN DE CÓDIGO INTERMEDIO =====
                if (reporte.getCantidadErrores() == 0) {
                    System.out.println("═══ 4. GENERACIÓN DE CÓDIGO INTERMEDIO ═══");

                    generador = new GeneradorCodigoIntermedio();
                    generador.visit(tree);
                    generador.imprimir();

                    System.out.println("✅ Código intermedio generado");
                    System.out.println();
                } else {
                    System.out.println("⚠️  Generación de código omitida debido a errores semánticos");
                    System.out.println();
                }
            } else {
                System.out.println("⚠️  Análisis semántico omitido debido a errores anteriores");
                System.out.println();
            }

            // ===== RESUMEN =====
            System.out.println("═══ RESUMEN DE COMPILACIÓN ═══");
            System.out.println("📁 Archivo procesado: " + archivoEntrada);
            System.out.println("🔤 Tokens analizados: " + tablaTokens.getCantidad());
            System.out.println("🌳 Nodos en AST: " + visualizador.contarNodos());

            // Contar todos los errores
            int totalErrores = errorListenerLexico.getErrores().size() +
                              errorListenerSintactico.getErrores().size();
            int totalWarnings = 0;

            if (tablaSimbolos != null) {
                System.out.println("📋 Símbolos: " + tablaSimbolos.getCantidadSimbolos());
            }

            if (reporte != null) {
                totalErrores += reporte.getCantidadErrores();
                totalWarnings = reporte.getCantidadWarnings();
            }

            if (generador != null) {
                System.out.println("📝 Instrucciones generadas: " + generador.getInstrucciones().size());
            }

            System.out.println("📊 Errores: " + totalErrores);
            System.out.println("📊 Warnings: " + totalWarnings);
            System.out.println();

            if (totalErrores == 0) {
                System.out.println("🎉 ¡COMPILACIÓN EXITOSA!");
            } else {
                System.out.println("❌ Compilación con errores");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

