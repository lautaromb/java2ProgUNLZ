package modelo;

public class Articulo {

    private String codigo;
    private String descripcion;
    private double precioNeto;
    private int stock;
    private RubroArticulo rubro;
    private TipoArticulo tipo;

    // Para artículos por demanda
    private int stockDeseado;

    public Articulo(String codigo, String descripcion, double precioNeto, int stock, RubroArticulo rubro, TipoArticulo tipo) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioNeto = precioNeto;
        this.stock = stock;
        this.rubro = rubro;
        this.tipo = tipo;
        this.stockDeseado = 0; // Solo se usa si el tipo es POR_DEMANDA
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion + (tipo == TipoArticulo.SUBSIDIADO ? " (S)" : "");
    }

    public double getPrecioFinal() {
        double descuento = 0.0;

        if (tipo == TipoArticulo.SUBSIDIADO) {
            descuento = rubro.getDescuentoSubsidiado();
        } else if (tipo == TipoArticulo.POR_DEMANDA && stockDeseado > 0 && stock > stockDeseado) {
            double porcentaje = ((double) stock / stockDeseado) * 100;
            descuento = Math.min(porcentaje - 100, 50); // Máximo 50%
        }

        return precioNeto * (1 - descuento / 100);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int nuevoStock) {
        this.stock = nuevoStock;
    }

    public TipoArticulo getTipo() {
        return tipo;
    }

    public RubroArticulo getRubro() {
        return rubro;
    }

    public int getStockDeseado() {
        return stockDeseado;
    }

    public void setStockDeseado(int stockDeseado) {
        this.stockDeseado = stockDeseado;
    }

    @Override
    public String toString() {
        return codigo + " - " + getDescripcion() +
                " | Precio: $" + getPrecioFinal() +
                " | Stock: " + stock +
                " | Rubro: " + rubro.getNombreConLetra() +
                " | Tipo: " + tipo.name();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecioNeto(double precioNeto) {
        this.precioNeto = precioNeto;
    }
}
