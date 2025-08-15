package modelo;

public class Empleado extends Usuario {
    public Empleado(String nombreUsuario, String contrasena) {
        super(nombreUsuario, contrasena);
    }

    @Override
    public boolean esEmpleado() {
        return true;
    }
}
