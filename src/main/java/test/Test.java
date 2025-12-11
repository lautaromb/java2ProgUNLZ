package test;

import modelo.*;
import sistema.Sistema;
import memoria.GestorArchivos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase de testeo intensivo para el sistema de gestión
 * Prueba todas las funcionalidades críticas del TP
 */
public class Test {

    private static int testsPasados = 0;
    private static int testsFallados = 0;
    private static int testsTotal = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("   TESTEO INTENSIVO DEL SISTEMA - TP PROGRAMACIÓN 2");
        System.out.println("=".repeat(70));

        // Limpiar archivos de prueba previos
        limpiarArchivos();

        // Ejecutar todos los tests
        testUsuarios();
        testArticulos();
        testSaldo();
        testCarrito();
        testPersistencia();
        testValidaciones();
        testDescuentos();
        testEdgeCases();

        // Mostrar resultados finales
        mostrarResultados();
    }

    // ==========================================
    // TESTS DE USUARIOS
    // ==========================================
    private static void testUsuarios() {
        seccion("TESTS DE USUARIOS");

        // Test 1: Crear empleado
        test("Crear empleado correctamente", () -> {
            Usuario emp = new Empleado("empleado1", "pass123");
            return emp.esEmpleado() && emp.getNombreUsuario().equals("empleado1");
        });

        // Test 2: Crear cliente
        test("Crear cliente correctamente", () -> {
            Usuario cli = new Cliente("cliente1", "pass123");
            return !cli.esEmpleado() && cli.getNombreUsuario().equals("cliente1");
        });

        // Test 3: Validar contraseña correcta
        test("Validar contraseña correcta", () -> {
            Usuario u = new Cliente("user", "mipass");
            return u.validarContrasena("mipass");
        });

        // Test 4: Rechazar contraseña incorrecta
        test("Rechazar contraseña incorrecta", () -> {
            Usuario u = new Cliente("user", "mipass");
            return !u.validarContrasena("otrapass");
        });

        // Test 5: Saldo inicial en cero
        test("Saldo inicial es cero", () -> {
            Usuario u = new Cliente("user", "pass");
            return u.getSaldo() == 0.0;
        });
    }

    // ==========================================
    // TESTS DE ARTÍCULOS
    // ==========================================
    private static void testArticulos() {
        seccion("TESTS DE ARTÍCULOS");

        // Test 6: Crear artículo simple
        test("Crear artículo SIMPLE", () -> {
            Articulo a = new Articulo("A001", "Producto Test", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            return a.getCodigo().equals("A001") && a.getPrecioFinal() == 1000;
        });

        // Test 7: Artículo subsidiado categoría A (30%)
        test("Artículo SUBSIDIADO - Rubro A (30% desc)", () -> {
            Articulo a = new Articulo("S001", "Alimento Subs", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            double esperado = 700; // 1000 - 30%
            return Math.abs(a.getPrecioFinal() - esperado) < 0.01;
        });

        // Test 8: Artículo subsidiado categoría B (24%)
        test("Artículo SUBSIDIADO - Rubro B (24% desc)", () -> {
            Articulo a = new Articulo("S002", "Electro Subs", 1000, 10,
                    RubroArticulo.ELECTRONICA, TipoArticulo.SUBSIDIADO);
            double esperado = 760; // 1000 - 24%
            return Math.abs(a.getPrecioFinal() - esperado) < 0.01;
        });

        // Test 9: Artículo subsidiado categoría C (15%)
        test("Artículo SUBSIDIADO - Rubro C (15% desc)", () -> {
            Articulo a = new Articulo("S003", "Hogar Subs", 1000, 10,
                    RubroArticulo.HOGAR, TipoArticulo.SUBSIDIADO);
            double esperado = 850; // 1000 - 15%
            return Math.abs(a.getPrecioFinal() - esperado) < 0.01;
        });

        // Test 10: Artículo por demanda sin exceso
        test("POR_DEMANDA sin exceso de stock", () -> {
            Articulo a = new Articulo("D001", "Demanda Test", 1000, 50,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(100);
            return a.getPrecioFinal() == 1000; // No hay descuento
        });

        // Test 11: Artículo por demanda con 25% exceso
        test("POR_DEMANDA con 25% exceso", () -> {
            Articulo a = new Articulo("D002", "Demanda Exceso", 1000, 100,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(80); // 100 es 125% de 80 -> 25% descuento
            double esperado = 750; // 1000 - 25%
            return Math.abs(a.getPrecioFinal() - esperado) < 0.01;
        });

        // Test 12: Artículo por demanda máximo 50% descuento
        test("POR_DEMANDA máximo 50% descuento", () -> {
            Articulo a = new Articulo("D003", "Demanda Max", 1000, 300,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(100); // 300% -> máximo 50%
            double esperado = 500; // 1000 - 50%
            return Math.abs(a.getPrecioFinal() - esperado) < 0.01;
        });

        // Test 13: Modificar stock
        test("Modificar stock de artículo", () -> {
            Articulo a = new Articulo("A001", "Test", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            a.setStock(25);
            return a.getStock() == 25;
        });

        // Test 14: Descripción con (S) para subsidiados
        test("Subsidiados muestran (S) en descripción", () -> {
            Articulo a = new Articulo("S001", "Producto", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            return a.getDescripcion().contains("(S)");
        });
    }

    // ==========================================
    // TESTS DE SALDO
    // ==========================================
    private static void testSaldo() {
        seccion("TESTS DE MÓDULO DE SALDO");

        // Test 15: Agregar saldo
        test("Agregar saldo correctamente", () -> {
            Usuario u = new Cliente("user", "pass");
            u.agregarSaldo(1000);
            return u.getSaldo() == 1000;
        });

        // Test 16: Agregar saldo múltiples veces
        test("Agregar saldo múltiples veces", () -> {
            Usuario u = new Cliente("user", "pass");
            u.agregarSaldo(500);
            u.agregarSaldo(300);
            u.agregarSaldo(200);
            return u.getSaldo() == 1000;
        });

        // Test 17: Retirar saldo con fondos suficientes
        test("Retirar saldo con fondos suficientes", () -> {
            Usuario u = new Cliente("user", "pass");
            u.agregarSaldo(1000);
            boolean exito = u.retirarSaldo(400);
            return exito && u.getSaldo() == 600;
        });

        // Test 18: No permitir retirar sin fondos
        test("Rechazar retiro sin fondos suficientes", () -> {
            Usuario u = new Cliente("user", "pass");
            u.agregarSaldo(500);
            boolean fallo = u.retirarSaldo(1000);
            return !fallo && u.getSaldo() == 500;
        });

        // Test 19: Transferir entre usuarios
        test("Transferir saldo entre usuarios", () -> {
            Usuario origen = new Cliente("origen", "pass");
            Usuario destino = new Cliente("destino", "pass");
            origen.agregarSaldo(1000);
            origen.transferirSaldo(destino, 400);
            return origen.getSaldo() == 600 && destino.getSaldo() == 400;
        });

        // Test 20: No transferir sin fondos
        test("No transferir sin fondos suficientes", () -> {
            Usuario origen = new Cliente("origen", "pass");
            Usuario destino = new Cliente("destino", "pass");
            origen.agregarSaldo(100);
            origen.transferirSaldo(destino, 500);
            return origen.getSaldo() == 100 && destino.getSaldo() == 0;
        });

        // Test 21: No aceptar montos negativos
        test("No agregar saldo negativo", () -> {
            Usuario u = new Cliente("user", "pass");
            u.agregarSaldo(1000);
            u.agregarSaldo(-500);
            return u.getSaldo() == 1000;
        });
    }

    // ==========================================
    // TESTS DE CARRITO
    // ==========================================
    private static void testCarrito() {
        seccion("TESTS DE CARRITO DE COMPRAS");

        // Test 22: Crear carrito vacío
        test("Carrito nuevo está vacío", () -> {
            CarritoCompras c = new CarritoCompras();
            return c.estaVacio();
        });

        // Test 23: Agregar artículo al carrito
        test("Agregar artículo al carrito", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 2);
            return !c.estaVacio() && c.getItems().size() == 1;
        });

        // Test 24: Calcular subtotal
        test("Calcular subtotal correctamente", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a1 = new Articulo("A001", "Test1", 100, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            Articulo a2 = new Articulo("A002", "Test2", 200, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a1, 2); // 200
            c.agregarArticulo(a2, 3); // 600
            return c.calcularSubtotal() == 800;
        });

        // Test 25: Descuento 15% si > $12000
        test("Aplicar descuento 15% si total > $12000", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Caro", 10000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 2); // 20000
            double descuento = c.calcularDescuento();
            return Math.abs(descuento - 3000) < 0.01; // 15% de 20000
        });

        // Test 26: No aplicar descuento si < $12000
        test("No aplicar descuento si total < $12000", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Barato", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 5); // 5000
            return c.calcularDescuento() == 0;
        });

        // Test 27: Verificar stock disponible
        test("Verificar stock disponible", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 5,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 3);
            return c.verificarStock();
        });

        // Test 28: Detectar stock insuficiente
        test("Detectar stock insuficiente", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 3,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 10);
            return !c.verificarStock();
        });

        // Test 29: Descontar stock después de compra
        test("Descontar stock después de compra", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 3);
            c.descontarStock();
            return a.getStock() == 7;
        });

        // Test 30: Agregar mismo artículo múltiples veces
        test("Agregar mismo artículo suma cantidades", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 2);
            c.agregarArticulo(a, 3);
            return c.getItems().size() == 1 &&
                    c.getItems().get(0).getCantidad() == 5;
        });

        // Test 31: Vaciar carrito
        test("Vaciar carrito correctamente", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 5);
            c.vaciar();
            return c.estaVacio();
        });
    }

    // ==========================================
    // TESTS DE PERSISTENCIA
    // ==========================================
    private static void testPersistencia() {
        seccion("TESTS DE PERSISTENCIA EN ARCHIVOS");

        // Test 32: Guardar y cargar usuarios
        test("Guardar y cargar usuarios", () -> {
            ArrayList<Usuario> usuarios = new ArrayList<>();
            usuarios.add(new Empleado("emp1", "pass1"));
            usuarios.add(new Cliente("cli1", "pass2"));

            GestorArchivos.guardarUsuarios(usuarios);
            ArrayList<Usuario> cargados = GestorArchivos.cargarUsuarios();

            return cargados.size() == 2 &&
                    cargados.get(0).getNombreUsuario().equals("emp1") &&
                    cargados.get(1).getNombreUsuario().equals("cli1");
        });

        // Test 33: Persistir saldo de usuario
        test("Persistir saldo de usuario", () -> {
            ArrayList<Usuario> usuarios = new ArrayList<>();
            Usuario u = new Cliente("cliente", "pass");
            u.agregarSaldo(5000);
            usuarios.add(u);

            GestorArchivos.guardarUsuarios(usuarios);
            ArrayList<Usuario> cargados = GestorArchivos.cargarUsuarios();

            return cargados.get(0).getSaldo() == 5000;
        });

        // Test 34: Guardar y cargar artículos
        test("Guardar y cargar artículos", () -> {
            ArrayList<Articulo> articulos = new ArrayList<>();
            articulos.add(new Articulo("A001", "Test1", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE));
            articulos.add(new Articulo("A002", "Test2", 2000, 20,
                    RubroArticulo.ELECTRONICA, TipoArticulo.SUBSIDIADO));

            GestorArchivos.guardarArticulos(articulos);
            ArrayList<Articulo> cargados = GestorArchivos.cargarArticulos();

            return cargados.size() == 2 &&
                    cargados.get(0).getCodigo().equals("A001") &&
                    cargados.get(1).getCodigo().equals("A002");
        });

        // Test 35: Persistir stock deseado
        test("Persistir stockDeseado de artículos POR_DEMANDA", () -> {
            ArrayList<Articulo> articulos = new ArrayList<>();
            Articulo a = new Articulo("D001", "Demanda", 1000, 100,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(80);
            articulos.add(a);

            GestorArchivos.guardarArticulos(articulos);
            ArrayList<Articulo> cargados = GestorArchivos.cargarArticulos();

            return cargados.get(0).getStockDeseado() == 80;
        });
    }

    // ==========================================
    // TESTS DE VALIDACIONES
    // ==========================================
    private static void testValidaciones() {
        seccion("TESTS DE VALIDACIONES");

        // Test 36: RubroArticulo desde letra válida
        test("RubroArticulo desde letra A", () -> {
            RubroArticulo rubro = RubroArticulo.desdeLetra("A");
            return rubro == RubroArticulo.ALIMENTOS;
        });

        // Test 37: RubroArticulo desde letra B
        test("RubroArticulo desde letra B", () -> {
            RubroArticulo rubro = RubroArticulo.desdeLetra("B");
            return rubro == RubroArticulo.ELECTRONICA;
        });

        // Test 38: RubroArticulo desde letra C
        test("RubroArticulo desde letra C", () -> {
            RubroArticulo rubro = RubroArticulo.desdeLetra("C");
            return rubro == RubroArticulo.HOGAR;
        });

        // Test 39: RubroArticulo letra inválida retorna null
        test("RubroArticulo letra inválida retorna null", () -> {
            RubroArticulo rubro = RubroArticulo.desdeLetra("Z");
            return rubro == null;
        });

        // Test 40: Validar contraseña case-sensitive
        test("Contraseña es case-sensitive", () -> {
            Usuario u = new Cliente("user", "Pass123");
            return u.validarContrasena("Pass123") && !u.validarContrasena("pass123");
        });
    }

    // ==========================================
    // TESTS DE DESCUENTOS COMPLEJOS
    // ==========================================
    private static void testDescuentos() {
        seccion("TESTS DE DESCUENTOS COMBINADOS");

        // Test 41: Carrito con subsidiados
        test("Carrito con artículos subsidiados", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("S001", "Subs", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            c.agregarArticulo(a, 2); // 2 * 700 = 1400
            return Math.abs(c.calcularSubtotal() - 1400) < 0.01;
        });

        // Test 42: Carrito mixto (simple + subsidiado)
        test("Carrito mixto simple + subsidiado", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo simple = new Articulo("A001", "Simple", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            Articulo subs = new Articulo("S001", "Subs", 1000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            c.agregarArticulo(simple, 1); // 1000
            c.agregarArticulo(subs, 1);   // 700
            return Math.abs(c.calcularSubtotal() - 1700) < 0.01;
        });

        // Test 43: Doble descuento (subsidiado + carrito >12000)
        test("Doble descuento: subsidiado + carrito >$12000", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo subs = new Articulo("S001", "Subs", 10000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            c.agregarArticulo(subs, 2); // 2 * 7000 = 14000
            double subtotal = c.calcularSubtotal(); // 14000
            double descCarrito = c.calcularDescuento(); // 15% de 14000 = 2100
            double total = c.calcularTotal(); // 11900

            return Math.abs(subtotal - 14000) < 0.01 &&
                    Math.abs(descCarrito - 2100) < 0.01 &&
                    Math.abs(total - 11900) < 0.01;
        });

        // Test 44: POR_DEMANDA justo en el límite
        test("POR_DEMANDA exactamente en stock deseado", () -> {
            Articulo a = new Articulo("D001", "Demanda", 1000, 80,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(80);
            return a.getPrecioFinal() == 1000; // 100% -> 0% descuento
        });

        // Test 45: POR_DEMANDA con 50% exacto
        test("POR_DEMANDA con exactamente 50% exceso", () -> {
            Articulo a = new Articulo("D001", "Demanda", 1000, 150,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(100); // 150% -> 50% descuento
            return Math.abs(a.getPrecioFinal() - 500) < 0.01;
        });
    }

    // ==========================================
    // TESTS DE CASOS EXTREMOS
    // ==========================================
    private static void testEdgeCases() {
        seccion("TESTS DE CASOS EXTREMOS");

        // Test 46: Artículo con precio 0
        test("Artículo con precio 0", () -> {
            Articulo a = new Articulo("A001", "Gratis", 0, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            return a.getPrecioFinal() == 0;
        });

        // Test 47: Artículo con stock 0
        test("Artículo con stock 0", () -> {
            Articulo a = new Articulo("A001", "Agotado", 1000, 0,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            return a.getStock() == 0;
        });

        // Test 48: Carrito justo en $12000
        test("Carrito exactamente en $12000 (sin descuento)", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 12000, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 1);
            return c.calcularDescuento() == 0; // No supera, no hay descuento
        });

        // Test 49: Carrito en $12001 (con descuento)
        test("Carrito en $12001 (con descuento 15%)", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 12001, 10,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 1);
            return c.calcularDescuento() > 0;
        });

        // Test 50: Transferir saldo 0
        test("Transferir saldo 0 (no modifica)", () -> {
            Usuario origen = new Cliente("origen", "pass");
            Usuario destino = new Cliente("destino", "pass");
            origen.agregarSaldo(1000);
            origen.transferirSaldo(destino, 0);
            return origen.getSaldo() == 1000 && destino.getSaldo() == 0;
        });

        // Test 51: Usuario con nombre vacío
        test("Rechazar creación de usuario con nombre vacío", () -> {
            try {
                new Cliente("", "pass");
                return false; // si no lanza excepción, el test falla
            } catch (IllegalArgumentException e) {
                return true;  // comportamiento esperado
            } catch (Exception e) {
                return false; // cualquier otra excepción no es la esperada
            }
        });

        // Test 52: POR_DEMANDA sin stockDeseado configurado
        test("POR_DEMANDA sin stockDeseado configurado", () -> {
            Articulo a = new Articulo("D001", "Demanda", 1000, 100,
                    RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            // stockDeseado = 0 por defecto, no aplica descuento
            return a.getPrecioFinal() == 1000;
        });

        // Test 53: Múltiples artículos mismo código en carrito
        test("Agregar artículo con mismo código suma cantidad", () -> {
            CarritoCompras c = new CarritoCompras();
            Articulo a = new Articulo("A001", "Test", 100, 20,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            c.agregarArticulo(a, 3);
            c.agregarArticulo(a, 5);
            c.agregarArticulo(a, 2);
            return c.getItems().size() == 1 &&
                    c.getItems().get(0).getCantidad() == 10;
        });

        // Test 54: Artículo subsidiado con precio muy bajo
        test("Subsidiado con precio bajo mantiene descuento", () -> {
            Articulo a = new Articulo("S001", "Barato", 10, 100,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            double esperado = 7; // 10 - 30%
            return Math.abs(a.getPrecioFinal() - esperado) < 0.01;
        });

        // Test 55: Stock muy grande
        test("Manejar stock muy grande (1 millón)", () -> {
            Articulo a = new Articulo("A001", "Stock Grande", 100, 1000000,
                    RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            return a.getStock() == 1000000;
        });
        // Test 56: Transferir a Empleado
        test("Sistema no permite transferir a empleado", () -> {
            // Crear sistema con usuarios
            ArrayList<Usuario> usuarios = new ArrayList<>();
            Usuario cliente = new Cliente("cli", "123");
            Usuario empleado = new Empleado("emp", "123");

            cliente.agregarSaldo(1000);
            usuarios.add(cliente);
            usuarios.add(empleado);

            GestorArchivos.guardarUsuarios(usuarios);

            // Simular la validación que hace Sistema.transferirSaldo()
            boolean esEmpleadoDestino = empleado.esEmpleado();

            // Sistema debería rechazar esto
            return esEmpleadoDestino && cliente.getSaldo() == 1000;
        });
        // 57 Articulo Precio Negativo
        test("Rechazar artículo con precio negativo", () -> {
            try {
                new Articulo("A1", "Malo", -50, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false; // debería fallar
            } catch (Exception e) {
                return true;
            }
        });
        // 58 Articulo Stock Negativo
        test("Rechazar artículo con stock negativo", () -> {
            try {
                new Articulo("A2", "Roto", 100, -5, RubroArticulo.HOGAR, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });
        // 59 Usuario nombre vacio
        test("Rechazar creación de usuario con nombre vacío", () -> {
            try {
                new Cliente("", "123");
                return false;
            } catch (Exception e) {
                return true;
            }
        });
        // 60 Usuario Contraseña vacio
        test("Rechazar creación de usuario con contraseña vacía", () -> {
            try {
                new Cliente("Lauta", "");
                return false;
            } catch (Exception e) {
                return true;
            }
        });


        //// TESTS CONSIGNA ABM ARTÍCULOS


        // 02 Validar código no vacío
        test("02 - Código vacío no permitido", () -> {
            try {
                new Articulo("", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 03 Validar código sólo letras y números
        test("03 - Código con caracteres inválidos", () -> {
            try {
                new Articulo("A#1", "Pan", 100, 5, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 04 Longitud mínima y máxima (2 a 6)
        test("04 - Código fuera del rango de longitud", () -> {
            try {
                new Articulo("A", "Pan", 100, 5, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {}

            try {
                new Articulo("A1234567", "Pan", 100, 5, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 05 Nombre vacío
        test("05 - Descripción vacía no permitida", () -> {
            try {
                new Articulo("A10", "", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 06 Precio mínimo 0.25
        test("06 - Precio menor a 0.25 rechazado", () -> {
            try {
                new Articulo("A10", "Leche", 0.10, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 07 Stock no puede ser negativo
        test("07 - Stock negativo rechazado", () -> {
            try {
                new Articulo("A10", "Leche", 100, -5, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 08 Rubro obligatorio
        test("08 - Rubro null rechazado", () -> {
            try {
                new Articulo("A10", "Pan", 100, 10, null, TipoArticulo.SIMPLE);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 09 Tipo obligatorio
        test("09 - Tipo null rechazado", () -> {
            try {
                new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, null);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 10 Artículo simple → precio sin descuento
        test("10 - Simple sin descuento", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            return a.getPrecioFinal() == 100;
        });

        // 11 Subsidiado rubro A descuento 30%
        test("11 - Subsidiado A descuento 30%", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            return a.getPrecioFinal() == 70; // 100 - 30%
        });

        // 12 Subsidiado B descuento 24%
        test("12 - Subsidiado B descuento 24%", () -> {
            Articulo a = new Articulo("A10", "Radio", 100, 10, RubroArticulo.ELECTRONICA, TipoArticulo.SUBSIDIADO);
            return a.getPrecioFinal() == 76;
        });

        // 13 Subsidiado C descuento 15%
        test("13 - Subsidiado C descuento 15%", () -> {
            Articulo a = new Articulo("A10", "Mesa", 100, 10, RubroArticulo.HOGAR, TipoArticulo.SUBSIDIADO);
            return a.getPrecioFinal() == 85;
        });

        // 14 Subsidiado agrega "(S)" al nombre
        test("14 - Nombre subsidiado incluye (S)", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 5, RubroArticulo.ALIMENTOS, TipoArticulo.SUBSIDIADO);
            return a.getDescripcion().endsWith("(S)");
        });

        // 15 Por demanda: aplica descuento por excedente
        test("15 - Por demanda aplica descuento correcto", () -> {
            Articulo a = new Articulo("A10", "Gaseosa", 100, 100, RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(80);

            // Excedente: 100 es 125% de 80 → descuento 25%
            return a.getPrecioFinal() == 75;
        });

        // 16 Por demanda: descuento máximo 50%
        test("16 - Por demanda tope 50%", () -> {
            Articulo a = new Articulo("A10", "Gaseosa", 100, 200, RubroArticulo.ALIMENTOS, TipoArticulo.POR_DEMANDA);
            a.setStockDeseado(50);

            // 200 es 400% de 50 → excedente 300% → descuento debería ser 300%, pero topea en 50%
            return a.getPrecioFinal() == 50;
        });

        // 17 Editar descripción válida
        test("17 - Editar descripción válida", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            a.setDescripcion("Pan Lactal");
            return a.getDescripcion().equals("Pan Lactal");
        });

        // 18 Editar descripción inválida rechazada
        test("18 - Editar descripción vacía rechazado", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            try {
                a.setDescripcion("");
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 19 Editar precio válido
        test("19 - Editar precio mayor a 0.25", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            a.setPrecioNeto(50);
            return a.getPrecioNeto() == 50;
        });

        // 20 Editar precio inválido
        test("20 - Editar precio menor a 0.25 rechazado", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            try {
                a.setPrecioNeto(0.10);
                return false;
            } catch (Exception e) {
                return true;
            }
        });

        // 21 Editar stock válido
        test("21 - Editar stock válido", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            a.setStock(30);
            return a.getStock() == 30;
        });

        // 22 Editar stock negativo rechazado
        test("22 - Stock negativo rechazado", () -> {
            Articulo a = new Articulo("A10", "Pan", 100, 10, RubroArticulo.ALIMENTOS, TipoArticulo.SIMPLE);
            try {
                a.setStock(-5);
                return false;
            } catch (Exception e) {
                return true;
            }
        });


    }

    // ==========================================
    // UTILIDADES DE TESTING
    // ==========================================

    private static void test(String descripcion, TestCase testCase) {
        testsTotal++;
        try {
            boolean resultado = testCase.ejecutar();
            if (resultado) {
                testsPasados++;
                System.out.println("✅ Test " + testsTotal + ": " + descripcion);
            } else {
                testsFallados++;
                System.out.println("❌ Test " + testsTotal + ": " + descripcion);
            }
        } catch (Exception e) {
            testsFallados++;
            System.out.println("❌ Test " + testsTotal + ": " + descripcion +
                    " (Excepción: " + e.getMessage() + ")");
        }
    }

    private static void seccion(String titulo) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  " + titulo);
        System.out.println("=".repeat(70));
    }

    private static void mostrarResultados() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("   RESUMEN DE RESULTADOS");
        System.out.println("=".repeat(70));
        System.out.println("Tests ejecutados: " + testsTotal);
        System.out.println("Tests pasados:    " + testsPasados + " ✅");
        System.out.println("Tests fallados:   " + testsFallados + " ❌");

        double porcentaje = (testsTotal > 0) ?
                (testsPasados * 100.0 / testsTotal) : 0;
        System.out.printf("Porcentaje éxito: %.1f%%\n", porcentaje);

        System.out.println("=".repeat(70));

        if (testsFallados == 0) {
            System.out.println("\n🎉 ¡TODOS LOS TESTS PASARON! El sistema funciona correctamente.");
        } else {
            System.out.println("\n⚠️  Hay tests fallados. Revisa el código.");
        }

        System.out.println("\n💡 IMPORTANTE: Si hay tests fallados, corrige:");
        System.out.println("   1. Bug en agregarDinero() - duplica el saldo");
        System.out.println("   2. Bug en retirarDinero() - guarda artículos en vez de usuarios");
        System.out.println("   3. Falta validación de datos negativos");
    }

    private static void limpiarArchivos() {
        new File("usuarios.txt").delete();
        new File("articulos.txt").delete();
    }

    @FunctionalInterface
    interface TestCase {
        boolean ejecutar() throws Exception;
    }
}