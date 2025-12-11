package org.example;

import java.util.Scanner;

import memoria.GestorArchivos;
import modelo.*;
import sistema.Sistema;

public class Main {
    public static void main(String[] args) {

        Sistema sistema = new Sistema();
        Scanner scanner = new Scanner(System.in);
        Usuario usuarioLogueado = null;
        boolean logged = false;

        Articulo a1 = new Articulo("A1","Pelota", 1000.0,10, RubroArticulo.HOGAR, TipoArticulo.SIMPLE);
        Articulo a2 = new Articulo("A2","Auricular", 5000.0,20, RubroArticulo.ELECTRONICA, TipoArticulo.POR_DEMANDA);
        Articulo a3 = new Articulo("A3","Queso", 1000.0,10, RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);

        sistema.agregarInicialSiNoExiste(a1);
        sistema.agregarInicialSiNoExiste(a2);
        sistema.agregarInicialSiNoExiste(a3);

        sistema.guardarArticulos();

        while (!logged) {
            System.out.println("\n1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    sistema.registrarUsuario();
                    break;
                case "2":
                    usuarioLogueado = sistema.login();
                    if (usuarioLogueado != null) {
                        System.out.println("¡Sesión iniciada como " +
                                (usuarioLogueado.esEmpleado() ? "empleado" : "cliente") + "!");

                        logged = true;
                        // Abrimos el menú según el tipo de usuario
                        if (usuarioLogueado.esEmpleado()) {
                            menuEmpleado(sistema, scanner);
                        } else {
                            menuCliente(sistema, scanner, usuarioLogueado); // si ya tenés un menú de cliente
                        }
                    }
                    break;
                case "0":
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println(" Opción inválida.");
            }
        }

    }
    // ==============================
    // MENÚ EMPLEADO
    // ==============================
    private static void menuEmpleado(Sistema sistema, Scanner scanner) {
        while (true) {
            System.out.println("\n    MENÚ EMPLEADO  ");
            System.out.println("1. Alta de artículo");
            System.out.println("2. Editar artículo");
            System.out.println("3. Eliminar artículo");
            System.out.println("4. Listar artículos");
            System.out.println("5. Gestionar Stock");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    sistema.agregarArticulo();
                    break;
                case "2":
                    sistema.editarArticulo();
                    break;
                case "3":
                    sistema.eliminarArticulo();
                    break;
                case "4":
                    sistema.listarArticulos();
                    break;
                case "5":
                    sistema.gestionarStock();
                    break;
                case "0":
                    System.out.println(" Sesión cerrada.");
                    return; // vuelve al menú principal
                default:
                    System.out.println(" Opción inválida.");
            }
        }
    }


    // MENÚ CLIENTE
    private static void menuCliente(Sistema sistema, Scanner scanner, Usuario usuario) {
        while (true) {

            System.out.println("\n   MENÚ CLIENTE   ");
            System.out.println("1. Listar artículos disponibles");
            System.out.println("2. Consultar saldo");
            System.out.println("3. Agregar dinero");
            System.out.println("4. Retirar dinero");
            System.out.println("5. Transferir saldo");
            System.out.println("6. Comprar artículo");
            System.out.println("7. Historial Compra");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    sistema.listarArticulos();
                    break;
                case "2":
                    System.out.println(" Saldo actual: $" + usuario.getSaldo());
                    break;
                case "3":
                    sistema.agregarDinero(usuario, scanner);
                    break;
                case "4":
                    sistema.retirarDinero(usuario, scanner);
                    break;
                case "5":
                    sistema.transferirSaldo(usuario, scanner);
                    break;
                case "6":
                    sistema.gestionarCarrito(usuario, scanner);
                    break;
                case "7":
                    ((Cliente) usuario).mostrarHistorial();
                    break;
                case "0":
                    System.out.println(" Sesión cerrada.");
                    return;
                default:
                    System.out.println(" Opción inválida.");
            }
        }
    }

}