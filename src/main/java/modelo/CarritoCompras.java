package modelo;

import java.util.ArrayList;

public class CarritoCompras {
    private ArrayList<ItemCarrito> items;
    private static final double MONTO_DESCUENTO = 12000.0;
    private static final double PORCENTAJE_DESCUENTO = 0.15; // 15%

    public CarritoCompras() {
        this.items = new ArrayList<>();
    }

    // agregar articulo al carrito
    public void agregarArticulo(Articulo articulo, int cantidad) {
        // buscar si ya existe el articulo en el carrito
        for (ItemCarrito item : items) {
            if (item.getArticulo().getCodigo().equals(articulo.getCodigo())) {
                // si ya esta, solo sumamos la cantidad
                item.agregarCantidad(cantidad);
                return;
            }
        }
        // si no existe, lo agregamos nuevo
        items.add(new ItemCarrito(articulo, cantidad));
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double calcularDescuento() {
        double subtotal = calcularSubtotal();
        if (subtotal > MONTO_DESCUENTO) {
            return subtotal * PORCENTAJE_DESCUENTO;
        }
        return 0.0;
    }

    public double calcularTotal() {
        return calcularSubtotal() - calcularDescuento();
    }

    public ArrayList<ItemCarrito> getItems() {
        return items;
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public void vaciar() {
        items.clear();
    }

    // verificar si hay stock suficiente para todos los items
    public boolean verificarStock() {
        for (ItemCarrito item : items) {
            if (item.getArticulo().getStock() < item.getCantidad()) {
                System.out.println(" Stock insuficiente para: " + item.getArticulo().getDescripcion());
                System.out.println("   Stock disponible: " + item.getArticulo().getStock() +
                        ", Solicitado: " + item.getCantidad());
                return false;
            }
        }
        return true;
    }

    // descontar stock de todos los articulos
    public void descontarStock() {
        for (ItemCarrito item : items) {
            Articulo art = item.getArticulo();
            art.setStock(art.getStock() - item.getCantidad());
        }
    }

    public void mostrarCarrito() {
        if (items.isEmpty()) {
            System.out.println(" El carrito está vacío.");
            return;
        }

        System.out.println("\n               CARRITO DE COMPRAS                     ");

        for (ItemCarrito item : items) {
            System.out.println(item);
        }

        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("Subtotal: $" + calcularSubtotal());

        if (calcularDescuento() > 0) {
            System.out.println("Descuento (15% por compra mayor a $12000): -$" + calcularDescuento());
        }

        System.out.println("TOTAL: $" + calcularTotal());
         }

    // mostrar factura final
    public void mostrarFactura() {
        System.out.println("\n                      FACTURA                          ");

        System.out.println("\nARTÍCULOS COMPRADOS:");
        for (ItemCarrito item : items) {
            System.out.println(item);
        }

        System.out.println("\n─────────────────────────────────────────────────────────");
        System.out.printf("Subtotal:        $%.2f%n", calcularSubtotal());

        if (calcularDescuento() > 0) {
            System.out.printf("Descuento (15%%): -$%.2f%n", calcularDescuento());
        }


        System.out.printf(" \n -- TOTAL FINAL:     $%.2f%n", calcularTotal());

        System.out.println("\n ¡Gracias por su compra!");
    }
}