package sistema;

import java.util.ArrayList;
import java.util.Scanner;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Empleado;
import modelo.RubroArticulo;
import modelo.TipoArticulo;
import modelo.Usuario;

public class Sistema {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Articulo> articulos;
    private Scanner scanner;
// TEst
    Usuario ejemplo1 =  new Empleado("Juan","123");


    //clave requerida en el TP Final
    private final String CLAVE_EMPLEADO = "pepepiola123";

    public Sistema() {
        usuarios = new ArrayList<>();
        articulos = new ArrayList<>();
        scanner = new Scanner(System.in);
        usuarios.add(ejemplo1);

    }

    // ---------------------------------------------
    // REGISTRO DE USUARIOS
    // ---------------------------------------------
    public void registrarUsuario() {
        System.out.println("=== Registro de usuario ===");
        System.out.print("Ingrese nombre de usuario: ");
        String nombre = scanner.nextLine();

        // Validar si ya existe
        if (buscarUsuario(nombre) != null) {
            System.out.println("❌ Ya existe un usuario con ese nombre.");
            return;
        }

        System.out.print("Ingrese contraseña: ");
        String pass1 = scanner.nextLine();

        System.out.print("Repita la contraseña: ");
        String pass2 = scanner.nextLine();

        if (!pass1.equals(pass2)) {
            System.out.println("❌ Las contraseñas no coinciden.");
            return;
        }

        System.out.print("¿Es empleado? (s/n): ");
        String esEmpleado = scanner.nextLine().toLowerCase();

        if (esEmpleado.equals("s")) {
            System.out.print("Ingrese clave de empleado: ");
            String clave = scanner.nextLine();
            if (!clave.equals(CLAVE_EMPLEADO)) {
                System.out.println("❌ Clave de empleado incorrecta.");
                return;
            }
            usuarios.add(new Empleado(nombre, pass1));
            System.out.println("✅ Empleado registrado correctamente.");
        } else {
            usuarios.add(new Cliente(nombre, pass1));
            System.out.println("✅ Cliente registrado correctamente.");
        }
    }

    // ---------------------------------------------
    // LOGIN
    // ---------------------------------------------
    public Usuario login() {
        System.out.println("=== Iniciar sesión ===");
        System.out.print("Usuario: ");
        String nombre = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        Usuario u = buscarUsuario(nombre);
        if (u != null && u.validarContrasena(pass)) {
            System.out.println("✅ Bienvenido, " + nombre + ".");
            return u;
        } else {
            System.out.println("❌ Usuario o contraseña incorrectos.");
            return null;
        }
    }

    // ---------------------------------------------
    // BUSCAR USUARIO POR NOMBRE
    // ---------------------------------------------
    private Usuario buscarUsuario(String nombre) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombre)) {
                return u;
            }
        }
        return null;
    }

    // ==============================
    // ABM ARTÍCULOS
    // ==============================
    public void agregarArticulo() {
        System.out.println("=== Alta de artículo ===");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        if (!codigo.matches("[a-zA-Z0-9]+")) {
            System.out.println("❌ Código inválido. Solo se permiten letras y números.");
            return;
        } else {
            System.out.println("Código válido: " + codigo);
        }

        if (buscarArticulo(codigo) != null) {
            System.out.println("❌ Ya existe un artículo con ese código.");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Precio neto: ");
        double precio = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        RubroArticulo rubro = null;
        while (rubro == null){
            System.out.println("Rubro (A: Alimentos, B: Electrónica, C: Hogar): ");
            rubro = RubroArticulo.desdeLetra(scanner.nextLine());
        }

        System.out.println("Tipo de artículo (1: SIMPLE, 2: SUBSIDIADO, 3: POR_DEMANDA): ");
        int tipoNum = Integer.parseInt(scanner.nextLine());
        TipoArticulo tipo = TipoArticulo.SIMPLE;
        if (tipoNum == 2) tipo = TipoArticulo.SUBSIDIADO;
        if (tipoNum == 3) tipo = TipoArticulo.POR_DEMANDA;

        Articulo articulo = new Articulo(codigo, descripcion, precio, stock, rubro, tipo);

        if (tipo == TipoArticulo.POR_DEMANDA) {
            System.out.print("Stock deseado: ");
            articulo.setStockDeseado(Integer.parseInt(scanner.nextLine()));
        }

        articulos.add(articulo);
        System.out.println("✅ Artículo agregado correctamente.");
    }

    public void listarArticulos() {
        System.out.println("=== Listado de artículos ===");
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }
        for (Articulo a : articulos) {
            System.out.println(a);
        }
    }

    public void editarArticulo() {
        System.out.println("=== Editar artículo ===");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        Articulo a = buscarArticulo(codigo);
        if (a == null) {
            System.out.println("❌ No existe un artículo con ese código.");
            return;
        }

        System.out.print("Nueva descripción: ");
        a.setDescripcion(scanner.nextLine());

        System.out.print("Nuevo precio neto: ");
        a.setPrecioNeto(Double.parseDouble(scanner.nextLine()));

        System.out.print("Nuevo stock: ");
        a.setStock(Integer.parseInt(scanner.nextLine()));

        if (a.getTipo() == TipoArticulo.POR_DEMANDA) {
            System.out.print("Nuevo stock deseado: ");
            a.setStockDeseado(Integer.parseInt(scanner.nextLine()));
        }

        System.out.println("✅ Artículo modificado correctamente.");
    }

    public void eliminarArticulo() {
        System.out.println("=== Eliminar artículo ===");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        Articulo a = buscarArticulo(codigo);
        if (a == null) {
            System.out.println("❌ No existe un artículo con ese código.");
            return;
        }

        articulos.remove(a);
        System.out.println("✅ Artículo eliminado.");
    }

    private Articulo buscarArticulo(String codigo) {
        for (Articulo a : articulos) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) {
                return a;
            }
        }
        return null;
    }


    // SALDO
    public void agregarDinero(Usuario usuario, Scanner scanner) {
        System.out.print("Ingrese el monto a agregar: ");
        String input = scanner.nextLine().trim();
        double monto;
        try {
            monto = Double.parseDouble(input);
            if (monto <= 0) {
                System.out.println("❌ El monto debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Ingrese un número válido.");
            return;
        }
        usuario.agregarSaldo(usuario.getSaldo() + monto);
        System.out.println("✅ Dinero agregado correctamente. Saldo actual: $" + usuario.getSaldo());
    }

    public void retirarDinero(Usuario usuario, Scanner scanner) {
    System.out.print("Ingrese el monto a retirar: ");
    String input = scanner.nextLine().trim();
    double monto;
    try {
        monto = Double.parseDouble(input);
        if (monto <= 0) {
            System.out.println("❌ El monto debe ser mayor a 0.");
            return;
        }
    } catch (NumberFormatException e) {
        System.out.println("❌ Ingrese un número válido.");
        return;
    }
    if (usuario.retirarSaldo(monto)) {
        System.out.println("✅ Dinero retirado correctamente. Saldo actual: $" + usuario.getSaldo());
    } else {
        System.out.println("❌ Saldo insuficiente.");
    }
}


    public void transferirSaldo(Usuario usuario, Scanner scanner) {
        System.out.print("Ingrese el nombre del usuario destino: ");
        String destinoNombre = scanner.nextLine().trim();
        Usuario destino = buscarUsuario(destinoNombre);
        if (destino == null) {
            System.out.println("❌ No existe un usuario con ese nombre.");
            return;
        }
        if (destino == usuario) {
            System.out.println("❌ No puedes transferirte a ti mismo.");
            return;
        }

        System.out.print("Ingrese el monto a transferir: ");
        String input = scanner.nextLine().trim();
        double monto;
        try {
            monto = Double.parseDouble(input);
            if (monto <= 0) {
                System.out.println("❌ El monto debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Ingrese un número válido.");
            return;
        }

        if (usuario.retirarSaldo(monto)) {
            destino.agregarSaldo(monto);
            System.out.println("✅ Dinero transferido correctamente. Saldo actual: $" + usuario.getSaldo());
        } else {
            System.out.println("❌ Saldo insuficiente.");
        }
    }

    public void comprarArticulo(Usuario usuario, Scanner scanner) {
        System.out.print("Ingrese el código del artículo a comprar: ");
        String codigo = scanner.nextLine().trim();
        Articulo articulo = buscarArticulo(codigo);
        if (articulo == null) {
            System.out.println("❌ No existe un artículo con ese código.");
            return;
        }
        System.out.print("Ingrese la cantidad a comprar: ");
        String input = scanner.nextLine().trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(input);
            if (cantidad <= 0) {
                System.out.println("❌ La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Ingrese un número válido.");
            return;
        }
        if (articulo.getStock() < cantidad) {
            System.out.println("❌ Stock insuficiente. Stock disponible: " + articulo.getStock());
            return;
        }
        double total = articulo.getPrecioFinal() * cantidad;
        if (usuario.getSaldo() < total) {
            System.out.println("❌ Saldo insuficiente. Total de la compra: $" + total + ", Saldo actual: $" + usuario.getSaldo());
            return;
        }
        usuario.retirarSaldo(total);
        articulo.setStock(articulo.getStock() - cantidad);
        System.out.println("✅ Compra realizada correctamente. Total: $" + total + ", Saldo restante: $" + usuario.getSaldo());
    }

}
