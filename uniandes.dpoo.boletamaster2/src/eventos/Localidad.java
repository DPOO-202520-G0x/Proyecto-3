package eventos;

import java.util.ArrayList;

import tiquetes.Tiquete;

public class Localidad {
	private String nombre;
	private double precioBase;
	private boolean numerada;
	private int numeroAsientos;
	private ArrayList<Tiquete> tiquetes;
	private Oferta oferta;
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}
	public boolean isNumerada() {
		return numerada;
	}
	public void setNumerada(boolean numerada) {
		this.numerada = numerada;
	}
	public int getNumeroAsientos() {
		return numeroAsientos;
	}
	public void setNumeroAsientos(int numeroAsientos) {
		this.numeroAsientos = numeroAsientos;
	}
	public ArrayList<Tiquete> getTiquetes() {
		return tiquetes;
	}
	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}
	
	public Localidad(Oferta oferta, ArrayList<Tiquete> tiquetes, String nombre, double precioBase, boolean numerada, int numeroAsientos) {
		super();
		this.nombre = nombre;
		this.precioBase = precioBase;
		this.numerada = numerada;
		this.numeroAsientos = numeroAsientos;
		this.tiquetes=new ArrayList<>();
		this.oferta=oferta;
	}
	public void fijarPrecio(double precio) {
		setPrecioBase(precio);
	}
	
}
