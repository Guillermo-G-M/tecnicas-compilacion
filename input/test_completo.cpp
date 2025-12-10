// Programa completo que combina múltiples características

int contadorGlobal;
double factorGlobal;

int factorial(int n) {
    int resultado;
    int i;

    if (n <= 1) {
        return 1;
    }

    resultado = 1;
    i = 2;
    while (i <= n) {
        resultado = resultado * i;
        i = i + 1;
    }

    return resultado;
}

int fibonacci(int n) {
    int a;
    int b;
    int temp;
    int i;

    if (n <= 0) {
        return 0;
    }

    if (n == 1) {
        return 1;
    }

    a = 0;
    b = 1;
    i = 2;

    while (i <= n) {
        temp = a + b;
        a = b;
        b = temp;
        i = i + 1;
    }

    return b;
}

bool esPrimo(int n) {
    int i;
    bool primo;

    if (n <= 1) {
        return false;
    }

    if (n == 2) {
        return true;
    }

    primo = true;
    i = 2;

    while (i < n) {
        if (n % i == 0) {
            primo = false;
            break;
        }
        i = i + 1;
    }

    return primo;
}

int sumarArray(int arr[10], int tam) {
    int suma;
    int i;

    suma = 0;
    i = 0;

    while (i < tam) {
        suma = suma + arr[i];
        i = i + 1;
    }

    return suma;
}

double promedio(int arr[5]) {
    int suma;
    int i;
    double prom;

    suma = 0;
    for (i = 0; i < 5; i = i + 1) {
        suma = suma + arr[i];
    }

    prom = suma / 5.0;
    return prom;
}

int main() {
    int numeros[10];
    int i;
    int j;
    int resultado;
    int suma;
    bool esPar;
    double prom;
    int contador;

    contadorGlobal = 0;
    factorGlobal = 1.5;

    // Inicializar array
    for (i = 0; i < 10; i = i + 1) {
        numeros[i] = i * 2;
        contadorGlobal = contadorGlobal + 1;
    }

    // Calcular factorial
    resultado = factorial(5);

    // Calcular fibonacci
    resultado = fibonacci(10);

    // Verificar primos en rango
    contador = 0;
    for (i = 2; i < 20; i = i + 1) {
        if (esPrimo(i)) {
            contador = contador + 1;
        }
    }

    // Sumar elementos del array
    suma = sumarArray(numeros, 10);

    // Calcular promedio
    prom = promedio(numeros);

    // Procesar array con condiciones
    for (i = 0; i < 10; i = i + 1) {
        if (numeros[i] % 2 == 0) {
            numeros[i] = numeros[i] / 2;
        } else {
            numeros[i] = numeros[i] * 3 + 1;
        }
    }

    // Búsqueda en array
    resultado = -1;
    for (i = 0; i < 10; i = i + 1) {
        if (numeros[i] == 10) {
            resultado = i;
            break;
        }
    }

    // Bucles anidados con matrices simuladas
    suma = 0;
    for (i = 0; i < 5; i = i + 1) {
        for (j = 0; j < 5; j = j + 1) {
            if (i == j) {
                suma = suma + 1;
            }
        }
    }

    // Operaciones con variables globales
    resultado = contadorGlobal * 2;
    prom = factorGlobal * resultado;

    return resultado;
}
