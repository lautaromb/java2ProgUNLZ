package modelo;

public class Articulo {

    private String codigo;
    private String descripcion;
    private double precioNeto;
    private int stock;
    private RubroArticulo rubro;
    private TipoArticulo tipo;
    private int stockDeseado;


    public Articulo(String codigo, String descripcion, double precioNeto, int stock,
                    RubroArticulo rubro, TipoArticulo tipo) {

        // VALIDACIONES
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (codigo.length() < 2 || codigo.length() > 6) {
            throw new IllegalArgumentException("Código no valido.");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }


        if (precioNeto < 0.25) {
            throw new IllegalArgumentException("El precio no valido.");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser menor a 0.");
        }

        if (rubro == null) {
            throw new IllegalArgumentException("El rubro no puede ser null.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de artículo no puede ser null.");
        }

        // ASIGNACIÓN
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioNeto = precioNeto;
        this.stock = stock;
        this.rubro = rubro;
        this.tipo = tipo;
        this.stockDeseado = 0;
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
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
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
        if (stockDeseado < 0) {
            throw new IllegalArgumentException("El stock deseado no puede ser negativo.");
        }
        this.stockDeseado = stockDeseado;
    }

    @Override
    public String toString() {
        return codigo + " - " + getDescripcion() +
                "  Precio: $" + getPrecioFinal() +
                "  Stock: " + stock +
                "  Rubro: " + rubro.getNombreConLetra() +
                "  Tipo: " + tipo.name();
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        this.descripcion = descripcion;
    }

    public void setPrecioNeto(double precioNeto) {
        if (precioNeto < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precioNeto = precioNeto;
    }

    public double getPrecioNeto() {
        return precioNeto;
    }
}
