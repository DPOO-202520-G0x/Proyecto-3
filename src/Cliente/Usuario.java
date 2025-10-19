package Cliente;

import java.util.Objects;

public abstract class Usuario {
    protected String login;
    protected String password;
    private String nombre;
    private double saldo;

    protected Usuario(String login, String password, String nombre, double saldo) {
        this.login = Objects.requireNonNull(login, "El login es obligatorio");
        this.password = Objects.requireNonNull(password, "La contraseña es obligatoria");
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
        if (saldo < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        this.saldo = saldo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = Objects.requireNonNull(login, "El login es obligatorio");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Objects.requireNonNull(password, "La contraseña es obligatoria");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
    }

    public double getSaldo() {
        return saldo;
    }

   public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean autenticar(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    public double consultarSaldo() {
        return saldo;
    }

    public void acreditarSaldo(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto a acreditar debe ser positivo");
        }
        saldo += monto;
    }

    public void usarSaldo(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto a debitar debe ser positivo");
        }
        if (monto > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        saldo -= monto;
    }

}
