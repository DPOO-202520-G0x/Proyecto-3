package tiquetes;

import java.time.LocalDateTime;
import java.util.Objects;

import Cliente.Cliente;
/**
 * Representa una transacción registrada en la plataforma BoletaMaster.
 * <p>
 * Usos típicos en el dominio:
 * <ul>
 *   <li>Registrar la compra de tiquetes (monto total cobrado al cliente).</li>
 *   <li>Respaldar operaciones contables y de reporte (por fecha y cliente).</li>
 * </ul>
 * Esta entidad no procesa pagos (la pasarela es externa); solo
 * modela el resultado contable/registral de la operación.
 */
public class Transaccion {
    private int idTransaccion;
    private LocalDateTime fecha; // TODO: confirmar tipo de fecha/hora según PDF/UML.
    private double monto;
    private Cliente cliente;
    /**
     * Crea una transacción con identificador, fecha, monto y cliente asociado.
     *
     * @param idTransaccion identificador único de la transacción.
     * @param fecha         instante en que se registra la transacción (obligatorio).
     * @param monto         monto total de la operación (puede ser 0 en casos como cortesías o ajustes).
     * @param cliente       cliente asociado a la transacción (puede ser {@code null} si se define luego).
     *
     * @throws NullPointerException si {@code fecha} es {@code null}.
     */
    public Transaccion(int idTransaccion, LocalDateTime fecha, double monto, Cliente cliente) {
        this.idTransaccion = idTransaccion;
        this.fecha = Objects.requireNonNull(fecha, "La fecha es obligatoria");
        this.monto = monto;
        this.cliente = cliente;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = Objects.requireNonNull(fecha, "La fecha es obligatoria");
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}