package tiquetes;

import java.util.ArrayList;
import java.util.Collection;

import eventos.Evento;
import eventos.Localidad;


public class TiqueteMultiple extends PaqueteTiquetes {
	

	private int cantidadEntradas;
	private double precioTotal;
	public int getCantidadEntradas() {
		return cantidadEntradas;
	}
	public void setCantidadEntradas(int cantidadEntradas) {
		this.cantidadEntradas = cantidadEntradas;
	}
	public double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
	public TiqueteMultiple(int cantidadEntradas, double precioTotal) {
		super();
		this.cantidadEntradas = cantidadEntradas;
		this.precioTotal = precioTotal;
	}
	
	
	

}