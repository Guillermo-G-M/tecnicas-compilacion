package com.compilador;

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
            System.out.println("Uso: java -jar compilador.jar <archivo_entrada>");
            System.out.println("Ejemplo: java -jar compilador.jar input/programa.cpp");
            return;
        }

        String archivoEntrada = args[0];
        System.out.println("Compilando archivo: " + archivoEntrada);
        System.out.println();

        // TODO: Implementar proceso de compilación
        System.out.println("Compilador en desarrollo...");
    }
}
