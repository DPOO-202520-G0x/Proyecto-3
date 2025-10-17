package tiquetes;

import java.util.Date;

import Cliente.Cliente;

public class Transaccion {
	private int idTransaccion;
    private Date fecha;
    private Cliente cliente;
    
    public Transaccion(int idTransaccion, Date fecha, double monto, Cliente cliente) {
		super();
		this.idTransaccion = idTransaccion;
		this.fecha = fecha;
		this.monto = monto;
		this.cliente = cliente;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	private double monto;
    

	
	    public int getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(int idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
		
}
