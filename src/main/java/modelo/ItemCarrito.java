package modelo;

public class ItemCarrito {
    private Articulo articulo;
    private int cantidad;

    public ItemCarrito(Articulo articulo, int cantidad) {
        this.articulo = articulo;
        this.cantidad = cantidad;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // sumar mas unidades del mismo articulo
    public void agregarCantidad(int cant) {
        this.cantidad += cant;
    }

    public double getSubtotal() {
        return articulo.getPrecioFinal() * cantidad;
    }

    @Override
    public String toString() {
        return articulo.getCodigo() + " - " + articulo.getDescripcion() +
                " | Cantidad: " + cantidad +
                " | Precio unit: $" + articulo.getPrecioFinal() +
                " | Subtotal: $" + getSubtotal();
    }
}