package com.compilador.analizador.sintactico;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Visualizador del árbol sintáctico abstracto (AST)
 */
public class VisualizadorAST {

    private final ParseTree tree;
    private final Parser parser;

    public VisualizadorAST(ParseTree tree, Parser parser) {
        this.tree = tree;
        this.parser = parser;
    }

    /**
     * Imprime el AST en formato textual indentado
     */
    public void imprimirArbol() {
        System.out.println("=== ÁRBOL SINTÁCTICO (AST) ===");
        System.out.println();
        imprimirNodo(tree, 0);
        System.out.println();
    }

    /**
     * Imprime el AST en formato LISP (compacto)
     */
    public void imprimirArbolLisp() {
        System.out.println("=== ÁRBOL SINTÁCTICO (FORMATO LISP) ===");
        System.out.println();
        String arbol = Trees.toStringTree(tree, parser);
        System.out.println(arbol);
        System.out.println();
    }

    /**
     * Recursivamente imprime los nodos del árbol con indentación
     */
    private void imprimirNodo(ParseTree node, int nivel) {
        String indent = "  ".repeat(nivel);
        String nombreNodo = Trees.getNodeText(node, parser);

        // Simplificar nombres de reglas (quitar números de contexto)
        nombreNodo = nombreNodo.replaceAll("Context@[0-9a-f]+", "");

        System.out.println(indent + "├─ " + nombreNodo);

        // Procesar hijos
        for (int i = 0; i < node.getChildCount(); i++) {
            imprimirNodo(node.getChild(i), nivel + 1);
        }
    }

    /**
     * Cuenta los nodos del árbol
     */
    public int contarNodos() {
        return contarNodosRecursivo(tree);
    }

    private int contarNodosRecursivo(ParseTree node) {
        int count = 1;
        for (int i = 0; i < node.getChildCount(); i++) {
            count += contarNodosRecursivo(node.getChild(i));
        }
        return count;
    }

    /**
     * Guarda el AST en formato LISP en un archivo
     * @param rutaArchivo Ruta del archivo de salida
     * @throws IOException Si hay error al escribir el archivo
     */
    public void guardarArchivo(String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("=== ÁRBOL SINTÁCTICO (FORMATO LISP) ===");
            writer.println();
            String arbol = Trees.toStringTree(tree, parser);
            writer.println(arbol);
            writer.println();
            writer.println("Total nodos: " + contarNodos());
        }
    }
}
