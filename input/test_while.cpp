// Prueba de Bucles WHILE con diferentes casos

int main() {
    int contador;
    int suma;
    int i;
    int j;
    int limite;

    // WHILE simple
    contador = 0;
    suma = 0;
    while (contador < 10) {
        suma = suma + contador;
        contador = contador + 1;
    }

    // WHILE con condición compleja
    i = 0;
    suma = 0;
    while (i < 20 && suma < 100) {
        suma = suma + i;
        i = i + 1;
    }

    // WHILE con BREAK
    i = 0;
    suma = 0;
    while (i < 100) {
        if (i > 15) {
            break;
        }
        suma = suma + i;
        i = i + 1;
    }

    // WHILE con CONTINUE
    i = 0;
    suma = 0;
    while (i < 20) {
        i = i + 1;
        if (i % 2 == 0) {
            continue;
        }
        suma = suma + i;
    }

    // WHILE anidados
    i = 0;
    suma = 0;
    while (i < 5) {
        j = 0;
        while (j < 5) {
            suma = suma + 1;
            j = j + 1;
        }
        i = i + 1;
    }

    // WHILE anidado con BREAK interno
    i = 0;
    suma = 0;
    while (i < 5) {
        j = 0;
        while (j < 10) {
            if (j == 3) {
                break;
            }
            suma = suma + 1;
            j = j + 1;
        }
        i = i + 1;
    }

    // WHILE con condición usando variable
    limite = 10;
    contador = 0;
    suma = 0;
    while (contador < limite) {
        suma = suma + contador * 2;
        contador = contador + 1;
    }

    return suma;
}
