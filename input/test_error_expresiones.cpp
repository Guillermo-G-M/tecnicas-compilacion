// Test de errores en expresiones

int main() {
    int a;
    int b;
    int c;
    double x;
    double y;

    a = 10;
    b = 5;
    x = 3.14;

    // Error: división por cero literal
    c = a / 0;

    // Error: operador inválido
    c = a ** b;

    // Error: operador no definido para tipos
    c = a && x;

    // Error: módulo con double
    y = x % 2.0;

    // Error: comparación de asignación
    if (a = 10) {
        b = 1;
    }

    // Error: operador lógico con números
    c = a || b;

    // Error: negación de entero
    c = !a;

    // Error: incremento de double (si no está soportado)
    x++;

    // Error: operación con variable no declarada
    c = a + noExiste;

    // Error: paréntesis desbalanceados
    c = (a + b * c;

    // Error: operador faltante
    c = a b;

    // Error: expresión incompleta
    c = a + ;

    return c;
}
