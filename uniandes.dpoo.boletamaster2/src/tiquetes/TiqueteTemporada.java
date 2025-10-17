package tiquetes;

import java.util.ArrayList;

import eventos.Evento;
import eventos.Localidad;

public class TiqueteTemporada extends PaqueteTiquetes {
	private int cantidadEventos;
	private double precioTotal;
	private ArrayList<Evento> eventosTemporada;
	public int getCantidadEventos() {
		return cantidadEventos;
	}
	public void setCantidadEventos(int cantidadEventos) {
		this.cantidadEventos = cantidadEventos;
	}
	public double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
	public TiqueteTemporada(int cantidadEventos, double precioTotal) {
		super();
		this.cantidadEventos = cantidadEventos;
		this.precioTotal = precioTotal;
	}
	public void definirEventos(ArrayList<Evento> eventos) {
		eventosTemporada.addAll(eventos);
	}
}
