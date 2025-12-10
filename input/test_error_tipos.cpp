// Test de errores de tipos de datos

int globalInt;
double globalDouble;

int main() {
    int entero;
    double decimal;
    char caracter;
    bool booleano;

    entero = 42;
    decimal = 3.14;

    // Error: asignar double a int sin conversión explícita
    entero = decimal;

    // Error: asignar char a int
    caracter = 'A';
    entero = caracter;

    // Error: comparar tipos incompatibles
    if (entero == decimal) {
        entero = 1;
    }

    // Error: operación aritmética con char
    caracter = caracter + 10;

    // Error: asignar int a bool
    booleano = 5;

    // Error: usar bool en operación aritmética
    entero = booleano + 10;

    // Error: asignar string literal a int
    entero = "texto";

    return entero;
}
