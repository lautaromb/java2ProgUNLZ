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

    public static RubroArticulo desdeLetra(String letra) {
        for (RubroArticulo r : RubroArticulo.values()) {
            if (String.valueOf(r.letra).equalsIgnoreCase(letra)) {
                return r;
            }
        }
        return null;
    }
}
