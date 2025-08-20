package modelo;

public enum RubroArticulo {
    ALIMENTOS("Alimentos", 'A', 30),
    ELECTRONICA("Electrónica", 'B', 24),
    HOGAR("Hogar", 'C', 15);

    private String nombre;
    private char letra;
    private int descuentoSubsidiado;

    RubroArticulo(String nombre, char letra, int descuento) {
        this.nombre = nombre;
        this.letra = letra;
        this.descuentoSubsidiado = descuento;
    }

    public String getNombreConLetra() {
        return nombre + " (" + letra + ")";
    }

    public int getDescuentoSubsidiado() {
        return descuentoSubsidiado;
    }

    public static RubroArticulo desdeLetra(String opcion) {
        if (opcion == null || opcion.isEmpty()) {
            throw new IllegalArgumentException("❌ Debes ingresar una opción.");
        }

        // Tomar solo la primera letra, ignorando mayúsculas
        char letra = Character.toUpperCase(opcion.trim().charAt(0));

        switch (letra) {
            case 'A': return RubroArticulo.ALIMENTOS;
            case 'B': return RubroArticulo.ELECTRONICA;
            case 'C': return RubroArticulo.HOGAR;
            default:
                System.out.println("❌ Código inválido. Solo se permiten letras y números.");
                return null;
        }
    }
}
