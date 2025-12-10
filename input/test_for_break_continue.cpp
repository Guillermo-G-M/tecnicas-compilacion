// Archivo de prueba para FOR, BREAK y CONTINUE

int main() {
    int suma;
    int i;
    int j;

    suma = 0;

    // Bucle FOR simple
    for (i = 0; i < 10; i = i + 1) {
        suma = suma + i;
    }

    // FOR con BREAK
    for (i = 0; i < 100; i = i + 1) {
        if (i > 5) {
            break;
        }
        suma = suma + i;
    }

    // FOR con CONTINUE
    for (i = 0; i < 10; i = i + 1) {
        if (i == 3) {
            continue;
        }
        suma = suma + i;
    }

    // FOR anidado con BREAK interno
    for (i = 0; i < 5; i = i + 1) {
        for (j = 0; j < 5; j = j + 1) {
            if (j == 2) {
                break;
            }
            suma = suma + 1;
        }
    }

    return suma;
}
