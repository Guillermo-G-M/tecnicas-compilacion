# Compilador C++ - Técnicas de compilación

Implementación de un compilador para un subconjunto del lenguaje C++.

## 📋 Requisitos

- **Java 21+**
- **Apache Maven 3.9+**
- **ANTLR 4.13.2** (dependencia Maven)

## 🔧 Compilación

### Compilar el proyecto completo
```bash
mvn clean compile
```

- Limpia compilaciones previas (`clean`)
- Ejecuta ANTLR para generar el lexer y parser desde la gramática
- Compila todas las clases Java del proyecto

### Compilar y ejecutar tests unitarios
```bash
mvn clean test
```

## 🚀 Ejecución

### Ejecutar compilador con archivo de prueba

#### Sintaxis general
bash
```bash
mvn -q exec:java -Dexec.args="<ruta_archivo>"
```

powershell
```powershell
mvn -q exec:java "-Dexec.args=<ruta_archivo>"
```

#### Ejemplos con archivos de prueba incluidos

**Test básico (código sin errores):**
```bash
mvn -q exec:java -Dexec.args="input/test_basico.cpp"
```

**Test de errores (código con errores semánticos):**
```bash
mvn -q exec:java -Dexec.args="input/test_errores.cpp"
```

## 📊 Archivos de Salida

El compilador genera automáticamente los siguientes archivos en el directorio `output/`:

- **`codigo_intermedio.txt`**: Código de tres direcciones generado
- **`codigo_optimizado.txt`**: Código optimizado tras aplicar técnicas de optimización
- **`ast_tree.ps`**: Árbol sintáctico en formato PostScript

## 🎯 Fases del Compilador

El compilador ejecuta las siguientes fases en orden:

1. **Análisis Léxico**: Tokenización del código fuente
2. **Análisis Sintáctico**: Construcción del AST (formato LISP)
3. **Visualización del AST**: Ventana gráfica interactiva del árbol sintáctico
4. **Análisis Semántico**: Verificación de tipos, tabla de símbolos
5. **Generación de Código Intermedio**: Código de tres direcciones
6. **Optimización**: Propagación de constantes, eliminación de código muerto

## 🖼️ Visualización Gráfica del AST

El compilador abre automáticamente una ventana gráfica mostrando el árbol sintáctico completo.

### Características:
- ✅ **Ventana interactiva** con árbol completo expandido
- ✅ **Controles de zoom**: Botones Zoom +, Zoom -, Reset
- ✅ **Scroll horizontal y vertical** para navegar el árbol
- ✅ **Exportación automática**: Se guarda en `output/ast_tree.ps` (PostScript)

## 📖 Documentación Técnica

- **Gramática ANTLR**: `src/main/antlr4/Compilador.g4`
- **Analizador Semántico**: `src/main/java/com/compilador/analizador/semantico/`
- **Tabla de Símbolos**: `src/main/java/com/compilador/tabla/`
- **Generador de Código**: `src/main/java/com/compilador/generador/`
- **Optimizador**: `src/main/java/com/compilador/optimizador/`