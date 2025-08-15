package sistema;

import modelo.Cliente;
import modelo.Empleado;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

public class Sistema {

    private ArrayList<Usuario> usuarios;
    private Scanner scanner;

    //clave requerida en el TP Final
    private final String CLAVE_EMPLEADO = "pepepiola123";

    public Sistema() {
        usuarios = new ArrayList<>();
        scanner = new Scanner(System.in);
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
}
