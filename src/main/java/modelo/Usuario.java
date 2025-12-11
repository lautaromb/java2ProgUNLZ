package modelo;

public abstract class Usuario {

    protected String nombreUsuario;
    protected String contrasena;
    protected double saldo;

    public Usuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.saldo = 0.0;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public double getSaldo() {
        return saldo;
    }

    public void agregarSaldo(double monto) {
        if (monto > 0) saldo += monto;
    }

    public boolean retirarSaldo(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            return true;
        }
        return false;
    }

    public String getContrasena(){
        return contrasena;
    }

    public void transferirSaldo(Usuario destino, double monto) {
        if (retirarSaldo(monto)) {
            destino.agregarSaldo(monto);
        }
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public abstract boolean esEmpleado();
}
