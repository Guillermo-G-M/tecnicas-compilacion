// Test de errores con arrays

int main() {
    int numeros[5];
    int i;

    // Error: índice fuera de rango (constante)
    numeros[10] = 100;

    // Error: índice negativo
    numeros[-1] = 50;

    // Error: usar array sin índice
    i = numeros;

    // Error: asignar a todo el array
    numeros = 10;

    // Error: array no declarado
    valores[0] = 20;

    // Error: índice de tipo incorrecto
    numeros[3.5] = 30;

    // Error: declarar array con tamaño variable
    int n;
    n = 10;
    int datos[n];

    // Error: declarar array con tamaño negativo
    int negativos[-5];

    // Error: declarar array sin tamaño
    int sinTam[];

    // Error: acceso con variable no declarada
    numeros[indice] = 40;

    return i;
}
