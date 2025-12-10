// Prueba de Tipos de Datos - int, double, char, bool, string

int globalInt;
double globalDouble;
char globalChar;
bool globalBool;

int main() {
    int entero;
    double decimal;
    char caracter;
    bool booleano;
    int resultado;

    // Tipo INT
    entero = 42;
    entero = entero + 10;
    entero = entero - 5;
    entero = entero * 2;
    entero = entero / 3;
    entero = entero % 7;

    // Tipo DOUBLE
    decimal = 3.14;
    decimal = decimal + 2.5;
    decimal = decimal - 1.2;
    decimal = decimal * 2.0;
    decimal = decimal / 4.0;

    // Tipo CHAR
    caracter = 'A';
    caracter = 'Z';
    caracter = '0';
    caracter = ' ';

    // Tipo BOOL
    booleano = true;
    booleano = false;
    booleano = entero > 10;
    booleano = decimal < 5.0;
    booleano = caracter == 'A';

    // Variables globales
    globalInt = 100;
    globalDouble = 99.99;
    globalChar = 'X';
    globalBool = true;

    // Operaciones mixtas
    resultado = entero + globalInt;
    decimal = decimal + globalDouble;

    // Comparaciones
    booleano = entero > globalInt;
    booleano = decimal < globalDouble;
    booleano = caracter != globalChar;

    // Expresiones con bool
    if (booleano) {
        entero = 1;
    }

    if (globalBool) {
        decimal = 1.0;
    }

    // Operaciones lógicas con bool
    booleano = true && false;
    booleano = true || false;
    booleano = !true;
    booleano = globalBool && booleano;

    // Arrays de diferentes tipos
    int enteros[3];
    double decimales[2];
    char caracteres[5];
    bool estados[4];

    enteros[0] = 10;
    enteros[1] = 20;
    enteros[2] = 30;

    decimales[0] = 1.5;
    decimales[1] = 2.7;

    caracteres[0] = 'H';
    caracteres[1] = 'O';
    caracteres[2] = 'L';
    caracteres[3] = 'A';
    caracteres[4] = '!';

    estados[0] = true;
    estados[1] = false;
    estados[2] = true;
    estados[3] = false;

    // Asignaciones desde arrays
    entero = enteros[0];
    decimal = decimales[1];
    caracter = caracteres[2];
    booleano = estados[0];

    return entero;
}
