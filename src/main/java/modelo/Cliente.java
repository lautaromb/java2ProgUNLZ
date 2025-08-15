package modelo;

public class Cliente extends Usuario{
    public Cliente(String nombreUsuario, String contrasena) {
        super(nombreUsuario, contrasena);
    }

    @Override
    public boolean esEmpleado() {
        return false;
    }
}
