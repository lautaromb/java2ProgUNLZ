package sistema;

import java.util.ArrayList;
import java.util.Scanner;

import Memoria.GestorArchivos;
import modelo.*;

public class Sistema {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Articulo> articulos;
    private Scanner scanner;

// TEst
    Usuario ejemplo1 =  new Empleado("Juan","123");


    //clave requerida en el TP Final
    private final String CLAVE_EMPLEADO = "pepepiola123";

    public Sistema() {
        scanner = new Scanner(System.in);

        // AQUÍ se cargan los datos desde los archivos
        usuarios = GestorArchivos.cargarUsuarios();      // Lee usuarios.txt
        articulos = GestorArchivos.cargarArticulos();    // Lee articulos.txt

        // Si no hay usuarios, agregar el de ejemplo
        if (usuarios.isEmpty()) {
            usuarios.add(ejemplo1);
        }

        System.out.println("Datos cargados correctamente.");
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
            System.out.println(" Ya existe un usuario con ese nombre.");
            return;
        }

        System.out.print("Ingrese contraseña: ");
        String pass1 = scanner.nextLine();

        System.out.print("Repita la contraseña: ");
        String pass2 = scanner.nextLine();

        if (!pass1.equals(pass2)) {
            System.out.println(" Las contraseñas no coinciden.");
            return;
        }

        String esEmpleado;

        while (true) {
            System.out.print("¿Es empleado? (s/n): ");
            esEmpleado = scanner.nextLine().trim().toLowerCase();

            if (esEmpleado.equals("s") || esEmpleado.equals("n")) {
                break; // valor válido → salimos del bucle
            }

            System.out.println(" Opción inválida. Por favor ingrese 's' o 'n'.");
        }

        if (esEmpleado.equals("s")) {
            System.out.print("Ingrese clave de empleado: ");
            String clave = scanner.nextLine();
            if (!clave.equals(CLAVE_EMPLEADO)) {
                System.out.println(" Clave de empleado incorrecta.");
                return;
            }
            usuarios.add(new Empleado(nombre, pass1));
            GestorArchivos.guardarUsuarios(usuarios);
            System.out.println(" Empleado registrado correctamente.");
        } else {
            usuarios.add(new Cliente(nombre, pass1));
            GestorArchivos.guardarUsuarios(usuarios);
            System.out.println(" Cliente registrado correctamente.");
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
            System.out.println(" Bienvenido, " + nombre + ".");
            return u;
        } else {
            System.out.println(" Usuario o contraseña incorrectos.");
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
            System.out.println(" Código inválido. Solo se permiten letras y números.");
            return;
        } else {
            System.out.println("Código válido: " + codigo);
        }

        if (buscarArticulo(codigo) != null) {
            System.out.println(" Ya existe un artículo con ese código.");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Precio neto: ");
        double precio = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        RubroArticulo rubro = null;
// --- RUBRO ---
        while (rubro == null) {
            System.out.println("Rubro (A: Alimentos, B: Electrónica, C: Hogar): ");
            rubro = RubroArticulo.desdeLetra(scanner.nextLine().trim());
            if (rubro == null) {
                System.out.println(" Opción inválida. Intente nuevamente.");
            }
        }

// --- TIPO DE ARTÍCULO ---
        Integer tipoNum = null;
        while (tipoNum == null) {
            System.out.println("Tipo de artículo (1: SIMPLE, 2: SUBSIDIADO, 3: POR_DEMANDA): ");
            String entrada = scanner.nextLine().trim();

            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= 1 && valor <= 3) {
                    tipoNum = valor;
                } else {
                    System.out.println(" Opción inválida. Ingrese 1, 2 o 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Debe ingresar un número válido (1, 2 o 3).");
            }
        }

// convertir número a enum
        TipoArticulo tipo;
        switch (tipoNum) {
            case 2: tipo = TipoArticulo.SUBSIDIADO; break;
            case 3: tipo = TipoArticulo.POR_DEMANDA; break;
            default: tipo = TipoArticulo.SIMPLE; break;
        }

// crear artículo
        Articulo articulo = new Articulo(codigo, descripcion, precio, stock, rubro, tipo);

        if (tipo == TipoArticulo.POR_DEMANDA) {
            System.out.print("Stock deseado: ");
            articulo.setStockDeseado(Integer.parseInt(scanner.nextLine()));
        }

        articulos.add(articulo);
        GestorArchivos.guardarArticulos(articulos);
        System.out.println(" Artículo agregado correctamente.");
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
            System.out.println(" No existe un artículo con ese código.");
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
        GestorArchivos.guardarArticulos(articulos);
        System.out.println(" Artículo modificado correctamente.");
    }

    public void eliminarArticulo() {
        System.out.println("=== Eliminar artículo ===");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        Articulo a = buscarArticulo(codigo);
        if (a == null) {
            System.out.println(" No existe un artículo con ese código.");
            return;
        }

        articulos.remove(a);
        GestorArchivos.guardarArticulos(articulos);
        System.out.println(" Artículo eliminado.");
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
                System.out.println(" El monto debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Ingrese un número válido.");
            return;
        }
        usuario.agregarSaldo(usuario.getSaldo() + monto);
        GestorArchivos.guardarUsuarios(usuarios);
        System.out.println(" Dinero agregado correctamente. Saldo actual: $" + usuario.getSaldo());
    }

    public void retirarDinero(Usuario usuario, Scanner scanner) {
    System.out.print("Ingrese el monto a retirar: ");
    String input = scanner.nextLine().trim();
    double monto;
    try {
        monto = Double.parseDouble(input);
        if (monto <= 0) {
            System.out.println(" El monto debe ser mayor a 0.");
            return;
        }
    } catch (NumberFormatException e) {
        System.out.println(" Ingrese un número válido.");
        return;
    }
    if (usuario.retirarSaldo(monto)) {
        System.out.println(" Dinero retirado correctamente. Saldo actual: $" + usuario.getSaldo());
        GestorArchivos.guardarArticulos(articulos);
    } else {
        System.out.println(" Saldo insuficiente.");
    }
}


    public void transferirSaldo(Usuario origen, Scanner scanner) {

        // --- INGRESO DEL DESTINO ---
        System.out.print("Ingrese el nombre del usuario destino: ");
        String destinoNombre = scanner.nextLine().trim();

        if (destinoNombre.isEmpty()) {
            System.out.println(" Debes ingresar un nombre.");
            return;
        }

        Usuario destino = buscarUsuario(destinoNombre);

        if (destino == null) {
            System.out.println(" No existe un usuario con ese nombre.");
            return;
        }

        // --- BLOQUEAR TRANSFERIRSE A UNO MISMO ---
        if (destino == origen) {
            System.out.println(" No puedes transferirte a ti mismo.");
            return;
        }

        // --- BLOQUEAR TRANSFERIR A EMPLEADOS ---
        if (destino instanceof Empleado) {
            System.out.println(" No puedes transferir dinero a un empleado.");
            return;
        }

        // --- BLOQUEAR QUE EMPLEADOS TRANSFIERAN, SI QUERÉS ---
        if (origen instanceof Empleado) {
            System.out.println(" Los empleados no pueden realizar transferencias.");
            return;
        }

        // --- INGRESO DEL MONTO ---
        System.out.print("Ingrese el monto a transferir: ");
        String input = scanner.nextLine().trim();

        double monto;

        try {
            monto = Double.parseDouble(input);
            if (monto <= 0) {
                System.out.println(" El monto debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Ingrese un número válido.");
            return;
        }

        // --- CHECK DE SALDO ---
        if (!origen.retirarSaldo(monto)) {
            System.out.println(" Saldo insuficiente.");
            return;
        }

        // --- TRANSFERENCIA ---
        destino.agregarSaldo(monto);
        System.out.println(" Transferencia exitosa. Tu saldo actual es: $" + origen.getSaldo());

        // Guarda correctamente los usuarios, no los artículos
        GestorArchivos.guardarUsuarios(usuarios);
    }


    public void comprarArticulo(Usuario usuario, Scanner scanner) {
        System.out.print("Ingrese el código del artículo a comprar: ");
        String codigo = scanner.nextLine().trim();
        Articulo articulo = buscarArticulo(codigo);
        if (articulo == null) {
            System.out.println(" No existe un artículo con ese código.");
            return;
        }
        System.out.print("Ingrese la cantidad a comprar: ");
        String input = scanner.nextLine().trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(input);
            if (cantidad <= 0) {
                System.out.println(" La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Ingrese un número válido.");
            return;
        }
        if (articulo.getStock() < cantidad) {
            System.out.println(" Stock insuficiente. Stock disponible: " + articulo.getStock());
            return;
        }
        double total = articulo.getPrecioFinal() * cantidad;
        if (usuario.getSaldo() < total) {
            System.out.println(" Saldo insuficiente. Total de la compra: $" + total + ", Saldo actual: $" + usuario.getSaldo());
            return;
        }
        usuario.retirarSaldo(total);
        articulo.setStock(articulo.getStock() - cantidad);
        System.out.println(" Compra realizada correctamente. Total: $" + total + ", Saldo restante: $" + usuario.getSaldo());
    }

    public void gestionarCarrito(Usuario usuario, Scanner scanner) {
        CarritoCompras carrito = new CarritoCompras();
        boolean finalizarCompra = false;

        while (!finalizarCompra) {
            System.out.println("\n          GESTION DE CARRITO                           ");
            System.out.println("1. Agregar articulo al carrito");
            System.out.println("2. Ver carrito");
            System.out.println("3. Finalizar compra");
            System.out.println("4. Vaciar carrito y salir");
            System.out.print("Opcion: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    agregarAlCarrito(carrito, scanner);
                    break;
                case "2":
                    carrito.mostrarCarrito();
                    break;
                case "3":
                    if (finalizarCompra(carrito, usuario)) {
                        finalizarCompra = true;
                    }
                    break;
                case "4":
                    System.out.println("Carrito vaciado.");
                    finalizarCompra = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }



    private void agregarAlCarrito(CarritoCompras carrito, Scanner scanner) {
        // primero mostramos todos los articulos disponibles
        System.out.println("\n=== Articulos disponibles ===");
        listarArticulos();

        System.out.print("\nIngrese el codigo del articulo: ");
        String codigo = scanner.nextLine().trim();

        Articulo articulo = buscarArticulo(codigo);
        if (articulo == null) {
            System.out.println("No existe un articulo con ese codigo.");
            return;
        }

        System.out.print("Ingrese la cantidad: ");
        String input = scanner.nextLine().trim();
        int cantidad;

        try {
            cantidad = Integer.parseInt(input);
            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un numero valido.");
            return;
        }

        // validar que hay stock
        if (articulo.getStock() < cantidad) {
            System.out.println("Stock insuficiente. Stock disponible: " + articulo.getStock());
            return;
        }

        carrito.agregarArticulo(articulo, cantidad);
        System.out.println("Articulo agregado al carrito.");
        System.out.println("Total actual del carrito: $" + carrito.calcularTotal());
    }

    private boolean finalizarCompra(CarritoCompras carrito, Usuario usuario) {
        if (carrito.estaVacio()) {
            System.out.println("El carrito esta vacio.");
            return false;
        }

        // verificar stock nuevamente por si cambio
        if (!carrito.verificarStock()) {
            return false;
        }

        // mostrar resumen
        carrito.mostrarCarrito();
        System.out.println("\nSu saldo actual es: $" + usuario.getSaldo());

        double total = carrito.calcularTotal();

        // verificar saldo suficiente
        if (usuario.getSaldo() < total) {
            System.out.println("\nSaldo insuficiente para completar la compra.");
            System.out.println("   Necesita: $" + total);
            System.out.println("   Su saldo: $" + usuario.getSaldo());
            System.out.println("   Faltan: $" + (total - usuario.getSaldo()));
            return false;
        }

        // confirmar compra
        System.out.print("\nConfirmar la compra? (s/n): ");
        String confirma = scanner.nextLine().trim().toLowerCase();

        if (!confirma.equals("s")) {
            System.out.println("Compra cancelada.");
            return false;
        }

        // procesar compra
        usuario.retirarSaldo(total);
        carrito.descontarStock();

        // guardar cambios en archivos
        GestorArchivos.guardarUsuarios(usuarios);
        GestorArchivos.guardarArticulos(articulos);

        // mostrar factura
        carrito.mostrarFactura();
        System.out.println("\nSaldo restante: $" + usuario.getSaldo());

        return true;
    }
}
