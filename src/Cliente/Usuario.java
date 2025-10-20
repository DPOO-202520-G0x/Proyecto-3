package Cliente;

import java.util.Objects;


/**
 * Clase base abstracta para todos los usuarios del sistema (cliente, organizador, administrador).
 * <p>
 * Proporciona credenciales (login/password), datos de identificación (nombre) y
 * un saldo virtual para procesar abonos y débitos según las reglas del dominio.
 * <p>
 * Métodos clave expuestos por la superclase:
 * <ul>
 *   <li>{@link #autenticar(String, String)}: verificación de credenciales.</li>
 *   <li>{@link #consultarSaldo()}: lectura del saldo virtual.</li>
 *   <li>{@link #acreditarSaldo(double)}: abono de reembolsos u otros créditos.</li>
 *   <li>{@link #usarSaldo(double)}: uso de saldo en compras (con validaciones).</li>
 * </ul>
 */
public abstract class Usuario {
    protected String login;
    protected String password;
    private String nombre;
    private double saldo;

    
    /**
     * Construye un usuario con credenciales, nombre y saldo inicial.
     *
     * @param login    login del usuario (obligatorio).
     * @param password contraseña del usuario (obligatoria).
     * @param nombre   nombre del usuario (obligatorio).
     * @param saldo    saldo virtual inicial (debe ser {@code >= 0}).
     * @throws NullPointerException     si {@code login}, {@code password} o {@code nombre} son {@code null}.
     * @throws IllegalArgumentException si {@code saldo} es negativo.
     */
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
    
    /**
     * Verifica si las credenciales suministradas coinciden con las del usuario.
     *
     * @param login    login a verificar.
     * @param password contraseña a verificar.
     * @return {@code true} si ambas credenciales coinciden; {@code false} en caso contrario.
     */
    public boolean autenticar(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }
    
    /**
     * Retorna el saldo virtual actual del usuario.
     *
     * @return saldo disponible.
     */
    public double consultarSaldo() {
        return saldo;
    }
    
    /**
     * Acredita (abona) un monto al saldo del usuario.
     * <p>
     * Usos típicos: reembolsos aprobados, ajustes o promociones válidas.
     *
     * @param monto monto a acreditar (debe ser {@code > 0}).
     * @throws IllegalArgumentException si {@code monto} es negativo.
     */
    public void acreditarSaldo(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto a acreditar debe ser positivo");
        }
        saldo += monto;
    }
    
    /**
     * Debita (consume) un monto del saldo del usuario.
     * <p>
     * Se valida que el monto sea positivo y que exista saldo suficiente.
     *
     * @param monto monto a debitar (debe ser {@code > 0} y {@code <=} saldo actual).
     * @throws IllegalArgumentException si {@code monto} es negativo o supera el saldo disponible.
     */
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