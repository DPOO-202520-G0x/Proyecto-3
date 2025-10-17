package eventos;

import java.util.ArrayList;
import java.util.Date;

import Cliente.Administrador;

public class Venue {
	private String idVenue;
	private String nombre;
	private String ubicacion;
	private int capacidad;
	private ArrayList<Localidad> localidades;
	
	
	
	public Venue( String idVenue, String nombre, String ubicacion, int capacidad, ArrayList<Localidad> localidades) {
		super();
		this.idVenue = idVenue;
		this.nombre = nombre;
		this.ubicacion = ubicacion;
		this.capacidad = capacidad;
		this.localidades = localidades;
		
		
	}
	
	public String getIdVenue() {
		return idVenue;
	}
	public void setIdVenue(String idVenue) {
		this.idVenue = idVenue;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public ArrayList<Localidad> getLocalidades() {
		return localidades;
	}
	public void setLocalidades(ArrayList<Localidad> localidades) {
		this.localidades = localidades;
	}
	
	
	public void aprobarVenue() {
		
	}
	public void agregarLocaidad(Localidad localidad) {
		localidades.add(localidad);
		
	}
	
	
		
	
}

