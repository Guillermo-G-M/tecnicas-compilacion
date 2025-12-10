// Prueba de Condicionales IF-ELSE

int main() {
    int a;
    int b;
    int resultado;
    bool activo;

    a = 10;
    b = 5;
    resultado = 0;

    // IF simple sin ELSE
    if (a > b) {
        resultado = 1;
    }

    // IF con ELSE
    if (a < b) {
        resultado = 2;
    } else {
        resultado = 3;
    }

    // IF anidados
    if (a > 0) {
        if (b > 0) {
            resultado = 10;
        } else {
            resultado = 20;
        }
    } else {
        resultado = 30;
    }

    // IF con condiciones lógicas
    if (a > 0 && b > 0) {
        resultado = 100;
    }

    if (a > 0 || b > 0) {
        resultado = 200;
    }

    // IF con negación
    if (!(a > b)) {
        resultado = 300;
    }

    // IF-ELSE encadenados (simulando else-if)
    if (a == 10) {
        resultado = 1000;
    } else {
        if (a == 20) {
            resultado = 2000;
        } else {
            if (a == 30) {
                resultado = 3000;
            } else {
                resultado = 4000;
            }
        }
    }

    // IF con múltiples instrucciones
    if (a > b) {
        resultado = a + b;
        a = a + 1;
        b = b - 1;
        resultado = resultado * 2;
    }

    // IF con condiciones complejas
    if (a > b && (a < 20 || b == 5)) {
        resultado = 500;
    }

    // IF dentro de bloques
    if (a > 0) {
        int temp;
        temp = a + b;
        if (temp > 10) {
            resultado = temp * 2;
        } else {
            resultado = temp / 2;
        }
    }

    // IF con bool
    activo = true;
    if (activo) {
        resultado = 999;
    }

    return resultado;
}
