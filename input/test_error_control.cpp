// Test de errores en estructuras de control

int main() {
    int a;
    int b;
    int i;

    a = 10;
    b = 5;

    // Error: condición no booleana en if
    if (a) {
        b = 1;
    }

    // Error: condición con asignación en vez de comparación
    if (a = b) {
        b = 2;
    }

    // Error: break fuera de bucle
    break;

    // Error: continue fuera de bucle
    continue;

    // Error: condición vacía en while
    while () {
        a = a + 1;
    }

    // Error: condición no booleana en while
    while (a) {
        a = a - 1;
    }

    // Error: for con condición mal formada
    for (i = 0; i; i = i + 1) {
        a = a + i;
    }

    // Error: variable no declarada en for
    for (j = 0; j < 10; j = j + 1) {
        a = a + 1;
    }

    // Error: break múltiple (sintaxis incorrecta)
    for (i = 0; i < 10; i = i + 1) {
        break;
        break;
    }

    // Error: continue después de break
    while (i < 20) {
        if (i > 15) {
            break;
            continue;
        }
        i = i + 1;
    }

    return a;
}
