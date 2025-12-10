// Prueba de Funciones - Declaración, parámetros, llamadas y retornos

int sumar(int a, int b) {
    int resultado;
    resultado = a + b;
    return resultado;
}

int restar(int x, int y) {
    return x - y;
}

int multiplicar(int m, int n) {
    int temp;
    temp = m * n;
    return temp;
}

double dividir(double num, double den) {
    double cociente;
    cociente = num / den;
    return cociente;
}

int calcularTotal(int a, int b, int c) {
    int suma1;
    int suma2;
    int total;

    suma1 = sumar(a, b);
    suma2 = sumar(suma1, c);
    total = multiplicar(suma2, 2);

    return total;
}

void imprimirMensaje() {
    int dummy;
    dummy = 42;
}

int main() {
    int x;
    int y;
    int z;
    int resultado;
    double division;

    x = 10;
    y = 5;
    z = 3;

    // Llamadas directas
    resultado = sumar(x, y);
    resultado = restar(x, y);
    resultado = multiplicar(x, y);

    // Llamadas anidadas
    resultado = sumar(sumar(x, y), z);
    resultado = multiplicar(restar(x, y), z);

    // Función con múltiples parámetros
    resultado = calcularTotal(x, y, z);

    // Función double
    division = dividir(10.0, 3.0);

    // Función void
    imprimirMensaje();

    return resultado;
}
