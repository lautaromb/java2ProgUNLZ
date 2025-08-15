package org.example;

import modelo.Usuario;
import sistema.Sistema;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        Sistema sistema = new Sistema();
        Scanner scanner = new Scanner(System.in);
        Usuario usuarioLogueado = null;

        while (true) {
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
                    }
                    break;
                case "0":
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println("❌ Opción inválida.");
            }
        }

    }

}