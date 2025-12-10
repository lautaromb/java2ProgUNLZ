package Memoria;

import java.io.*;
import java.util.ArrayList;

import modelo.*;

public class GestorArchivos {

    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_ARTICULOS = "articulos.txt";

    // ==========================================
    // GUARDAR USUARIOS
    // ==========================================
    public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_USUARIOS))) {
            for (Usuario u : usuarios) {
                // Formato: tipo|nombreUsuario|contrasena|saldo
                String tipo = u.esEmpleado() ? "EMPLEADO" : "CLIENTE";
                writer.write(tipo + "|" + u.getNombreUsuario() + "|" +
                        u.getContrasena() + "|" + u.getSaldo());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    // ==========================================
    // CARGAR USUARIOS
    // ==========================================
    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(ARCHIVO_USUARIOS);

        if (!archivo.exists()) {
            return usuarios; // devuelve lista vacia si no existe el archivo
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length >= 4) {
                    String tipo = partes[0];
                    String nombreUsuario = partes[1];
                    String contrasena = partes[2];
                    double saldo = Double.parseDouble(partes[3]);

                    Usuario u;
                    if (tipo.equals("EMPLEADO")) {
                        u = new Empleado(nombreUsuario, contrasena);
                    } else {
                        u = new Cliente(nombreUsuario, contrasena);
                    }
                    u.setSaldo(saldo);
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    // ==========================================
    // GUARDAR ARTICULOS
    // ==========================================
    public static void guardarArticulos(ArrayList<Articulo> articulos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_ARTICULOS))) {
            for (Articulo a : articulos) {
                // Formato: codigo|descripcion|precioNeto|stock|rubro|tipo|stockDeseado
                writer.write(a.getCodigo() + "|" +
                        a.getDescripcion() + "|" +
                        a.getPrecioNeto() + "|" +
                        a.getStock() + "|" +
                        a.getRubro().name() + "|" +
                        a.getTipo().name() + "|" +
                        a.getStockDeseado());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar articulos: " + e.getMessage());
        }
    }

    // ==========================================
    // CARGAR ARTICULOS
    // ==========================================
    public static ArrayList<Articulo> cargarArticulos() {
        ArrayList<Articulo> articulos = new ArrayList<>();
        File archivo = new File(ARCHIVO_ARTICULOS);

        if (!archivo.exists()) {
            return articulos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_ARTICULOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length >= 7) {
                    String codigo = partes[0];
                    String descripcion = partes[1];
                    double precioNeto = Double.parseDouble(partes[2]);
                    int stock = Integer.parseInt(partes[3]);
                    RubroArticulo rubro = RubroArticulo.valueOf(partes[4]);
                    TipoArticulo tipo = TipoArticulo.valueOf(partes[5]);
                    int stockDeseado = Integer.parseInt(partes[6]);

                    Articulo a = new Articulo(codigo, descripcion, precioNeto, stock, rubro, tipo);
                    a.setStockDeseado(stockDeseado);
                    articulos.add(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar articulos: " + e.getMessage());
        }

        return articulos;
    }
}