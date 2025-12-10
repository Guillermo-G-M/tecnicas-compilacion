// Prueba de Expresiones - Aritméticas, lógicas y de comparación

int main() {
    int a;
    int b;
    int c;
    int resultado;
    bool condicion;
    double x;
    double y;

    a = 10;
    b = 5;
    c = 3;

    // Expresiones aritméticas complejas
    resultado = a + b * c;
    resultado = (a + b) * c;
    resultado = a * b + c * 2;
    resultado = a / b + c % 2;

    // Expresiones con negativos
    resultado = -a + b;
    resultado = a + (-b);
    resultado = -a * -b;

    // Expresiones con incremento/decremento
    a++;
    b--;
    resultado = a + b;

    // Expresiones de comparación
    condicion = a > b;
    condicion = a >= b;
    condicion = a < b;
    condicion = a <= b;
    condicion = a == b;
    condicion = a != b;

    // Expresiones lógicas
    condicion = a > b && c < 10;
    condicion = a > b || c < 10;
    condicion = !(a > b);
    condicion = a > b && c < 10 || a == 0;

    // Expresiones lógicas complejas
    condicion = (a > b) && (c < 10);
    condicion = (a > b || c < 10) && (a != 0);
    condicion = !(a > b && c < 10);

    // Expresiones con doubles
    x = 10.5;
    y = 3.2;
    x = x + y;
    x = x - y;
    x = x * y;
    x = x / y;

    // Expresiones mixtas
    resultado = a + b - c * 2 / 1 + 5 % 3;
    condicion = a > 0 && b < 20 || c == 3 && a != b;

    return resultado;
}
