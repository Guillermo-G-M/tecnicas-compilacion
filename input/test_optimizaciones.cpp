// Archivo de prueba para TODAS las optimizaciones del compilador
// Compatible con la gramática del compilador

// Variables globales
int resultado;
int valorFinal;
bool flagNoUsado;        // Optimización 5: Variable no usada
double piNoUsado;        // Optimización 5: Variable no usada

// Función para probar propagación de constantes y simplificación
int calcular() {
    int a;
    int b;
    int c;
    int d;
    int e;
    int temp;

    // Optimización 1: Propagación de constantes
    a = 5;
    b = a;           // b debería propagarse como 5
    c = b + 3;       // c = 5 + 3

    // Optimización 2: Simplificación de expresiones
    d = 10 + 20;     // Debería simplificarse a 30
    e = 8 * 2;       // Debería simplificarse a 16
    temp = 15 - 5;   // Debería simplificarse a 10

    // Optimización 4: Eliminación de redundancias
    temp = temp;     // Auto-asignación redundante
    a = b + c;
    a = b + c;       // Instrucción duplicada

    return temp;
}

// Función para probar eliminación de código muerto
int conCodigoMuerto(int x) {
    int resultado;

    if (x > 0) {
        resultado = x + 10;
        return resultado;

        // Optimización 3: Código muerto después de return
        resultado = 999;
        x = x + 100;
    }

    return 0;
}

// Función con goto y código muerto
int conGoto(int valor) {
    int temp;
    temp = valor;

    if (temp > 5) {
        temp = temp * 2;
        return temp;
    }

    // Optimización 3: Código muerto con goto
    temp = 100;
    return temp;
}

// Función main que usa todo
int main() {
    int x;
    int y;
    int z;
    int w;
    int unusedVar;   // Optimización 5: Variable local no usada

    // Inicializar
    x = 10;

    // Propagación de constantes
    y = 5;
    z = y;           // z debería ser 5
    w = z + 2;       // w = 5 + 2 = 7

    // Simplificación de expresiones constantes
    x = 3 + 7;       // Debería ser 10
    x = x * 2;       // 10 * 2 = 20

    // Redundancias
    y = y;           // Auto-asignación
    z = x + 5;
    z = x + 5;       // Duplicado

    // Llamadas a funciones
    resultado = calcular();
    valorFinal = conCodigoMuerto(resultado);
    valorFinal = conGoto(valorFinal);

    // Código muerto después de return
    if (valorFinal > 0) {
        return valorFinal;

        // Optimización 3: Todo esto es código muerto
        x = 999;
        y = 888;
        z = 777;
    }

    return 0;
}
