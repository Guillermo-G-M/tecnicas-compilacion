package com.compilador.visualizador;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.Parser;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Visualizador gráfico del AST usando ANTLR TreeViewer
 */
public class VisualizadorGrafico {

    private Parser parser;
    private ParseTree tree;

    public VisualizadorGrafico(Parser parser, ParseTree tree) {
        this.parser = parser;
        this.tree = tree;
    }

    /**
     * Muestra el árbol en una ventana gráfica
     */
    public void mostrarVentana() {
        // Obtener los nombres de las reglas del parser
        List<String> ruleNames = Arrays.asList(parser.getRuleNames());

        // Crear el visualizador de árbol
        TreeViewer viewer = new TreeViewer(ruleNames, tree);
        viewer.setScale(1.5); // Zoom inicial

        // Crear ventana
        JFrame frame = new JFrame("Visualización del AST - Compilador C++");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel de scroll para el árbol
        JScrollPane scrollPane = new JScrollPane(viewer);
        scrollPane.setPreferredSize(new Dimension(1200, 800));

        // Panel de controles
        JPanel controlPanel = new JPanel();

        // Botón de zoom in
        JButton zoomInBtn = new JButton("Zoom +");
        zoomInBtn.addActionListener(e -> {
            viewer.setScale(viewer.getScale() * 1.2);
            viewer.revalidate();
            viewer.repaint();
        });

        // Botón de zoom out
        JButton zoomOutBtn = new JButton("Zoom -");
        zoomOutBtn.addActionListener(e -> {
            viewer.setScale(viewer.getScale() / 1.2);
            viewer.revalidate();
            viewer.repaint();
        });

        // Botón de reset
        JButton resetBtn = new JButton("Reset Zoom");
        resetBtn.addActionListener(e -> {
            viewer.setScale(1.5);
            viewer.revalidate();
            viewer.repaint();
        });

        controlPanel.add(zoomInBtn);
        controlPanel.add(zoomOutBtn);
        controlPanel.add(resetBtn);

        // Agregar componentes a la ventana
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Mostrar ventana
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar en pantalla
        frame.setVisible(true);
    }

    /**
     * Genera y guarda el árbol como imagen PostScript
     */
    public void guardarComoPS(String rutaArchivo) {
        try {
            List<String> ruleNames = Arrays.asList(parser.getRuleNames());
            TreeViewer viewer = new TreeViewer(ruleNames, tree);
            viewer.save(rutaArchivo);
            System.out.println("✅ Árbol guardado en: " + rutaArchivo);
        } catch (Exception e) {
            System.err.println("❌ Error al guardar árbol: " + e.getMessage());
        }
    }
}
