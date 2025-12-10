// Archivo de prueba para errores de BREAK y CONTINUE fuera de loops

int main() {
    int suma;

    suma = 0;

    // ERROR: break fuera de loop
    break;

    // ERROR: continue fuera de loop
    continue;

    return suma;
}
