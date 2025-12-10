// Test de errores con variables

int variableGlobal;

int funcion(int param) {
    int local;
    local = param * 2;
    return local;
}

int main() {
    int a;
    int b;

    // Error: usar variable no declarada
    c = 10;

    // Error: variable ya declarada
    int a;

    // Error: usar variable antes de inicializar (warning en algunos casos)
    int x;
    int y;
    y = x + 5;

    // Error: redeclarar variable en mismo ámbito
    int b;
    b = 20;

    // Error: usar variable de otro ámbito
    a = local;

    // Error: asignar a variable no declarada
    noExiste = 100;

    // Error: usar variable global no declarada
    resultado = variableGlobal + a;

    // Error: variable con nombre de función
    int funcion;
    funcion = 50;

    return a;
}
