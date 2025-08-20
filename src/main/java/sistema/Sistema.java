package sistema;

import modelo.*;

import java.util.ArrayList;
import java.util.Scanner;

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
}
