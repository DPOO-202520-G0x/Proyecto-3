package tiquetes;

import java.util.ArrayList;

import eventos.Evento;
import eventos.Localidad;

public class PaqueteDeluxe extends PaqueteTiquetes{
	private String beneficios;
	private ArrayList<Tiquete> tiquetesIncluidos;
	public PaqueteDeluxe(String beneficios, ArrayList<Tiquete> tiquetesIncluidos) {
		super();
		this.beneficios = beneficios;
		this.tiquetesIncluidos = tiquetesIncluidos;
	}
	public String getBeneficios() {
		return beneficios;
	}
	public void setBeneficios(String beneficios) {
		this.beneficios = beneficios;
	}
	public ArrayList<Tiquete> getTiquetesIncluidos() {
		return tiquetesIncluidos;
	}
	public void setTiquetesIncluidos(ArrayList<Tiquete> tiquetesIncluidos) {
		this.tiquetesIncluidos = tiquetesIncluidos;
	}
	public void agregarTiquetes(ArrayList<Tiquete> tiquetes) {
		tiquetesIncluidos.addAll(tiquetes);
	}
}
