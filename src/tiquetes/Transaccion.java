package tiquetes;

import java.time.LocalDateTime;
import java.util.Objects;

import Cliente.Cliente;

public class Transaccion {
    private int idTransaccion;
    private LocalDateTime fecha; // TODO: confirmar tipo de fecha/hora según PDF/UML.
    private double monto;
    private Cliente cliente;

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