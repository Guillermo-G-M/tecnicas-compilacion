// Test de errores con funciones

int sumar(int a, int b) {
    int resultado;
    resultado = a + b;
    return resultado;
}

// Error: función redeclarada
int sumar(int x, int y) {
    return x + y;
}

void funcionVoid() {
    int temp;
    temp = 42;
    // Error: return con valor en función void
    return temp;
}

int funcionInt() {
    int valor;
    valor = 10;
    // Error: return sin valor en función int
    return;
}

int main() {
    int resultado;

    // Error: llamar función no declarada
    resultado = multiplicar(5, 3);

    // Error: número incorrecto de parámetros
    resultado = sumar(5);
    resultado = sumar(5, 3, 2);

    // Error: asignar función void a variable
    resultado = funcionVoid();

    // Error: usar función como variable
    sumar = 100;

    // Error: parámetros de tipo incorrecto
    resultado = sumar(3.14, 2.5);

    return resultado;
}

// Error: return fuera de función
return 0;
