package modelo;

import java.util.ArrayList;

public class Cliente extends Usuario{

    private ArrayList<String> historialCompras;

    public Cliente(String nombreUsuario, String contrasena) {
        super(nombreUsuario, contrasena);
    }

    @Override
    public boolean esEmpleado() {
        return false;
    }

    public void agregarCompraAlHistorial(String resumen) {
        if (historialCompras == null) {
            historialCompras = new ArrayList<>();
        }
        historialCompras.add(resumen);
    }

    public void mostrarHistorial() {
        if (historialCompras == null) {
            historialCompras = new ArrayList<>();
        }

        if (historialCompras.isEmpty()) {
            System.out.println("No tiene compras registradas.");
            return;
        }
        System.out.println("\n=== HISTORIAL DE COMPRAS ===");
        for (int i = 0; i < historialCompras.size(); i++) {
            System.out.println((i + 1) + ". " + historialCompras.get(i));
        }
    }
}
