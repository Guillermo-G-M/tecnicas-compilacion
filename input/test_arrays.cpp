// Prueba de Arrays - Declaración, asignación y acceso

int main() {
    int numeros[5];
    int suma;
    int i;
    double promedios[3];
    char letras[4];

    // Inicializar array de enteros
    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;
    numeros[3] = 40;
    numeros[4] = 50;

    // Operaciones con arrays
    suma = numeros[0] + numeros[1];
    suma = suma + numeros[2];
    suma = suma + numeros[3];
    suma = suma + numeros[4];

    // Array de doubles
    promedios[0] = 7.5;
    promedios[1] = 8.3;
    promedios[2] = 9.1;

    // Array de chars
    letras[0] = 'A';
    letras[1] = 'B';
    letras[2] = 'C';
    letras[3] = 'D';

    // Acceso con expresiones
    i = 0;
    suma = numeros[i];
    i = i + 1;
    suma = suma + numeros[i];
    i = i + 1;
    suma = suma + numeros[i];

    // Modificar elementos
    numeros[0] = numeros[1] + numeros[2];
    numeros[3] = suma * 2;

    return suma;
}
