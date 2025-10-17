package eventos;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import Cliente.Administrador;
import Cliente.Organizador;
import tiquetes.Tiquete;

public class Evento {
	private String idEvento;
	private String nombre;
	private Date fecha;
	private Time hora;
	private String estado;
	private TipoEvento tipoEvento;
	private Venue venue;
	private Oferta oferta;
	private Organizador organizador;
	private ArrayList<Tiquete> tiquetes;
	private Administrador administrador;
	
	
	
	
	public Administrador getAdministrador() {
		return administrador;
	}
	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}
	public Evento(Administrador administrador,String idEvento, String nombre, Date fecha, Time hora, String estado, TipoEvento tipoEvento,
			Venue venue, Oferta oferta, Organizador organizador, ArrayList<Tiquete> tiquetes) {
		super();
		this.idEvento = idEvento;
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = estado;
		this.tipoEvento = tipoEvento;
		this.venue = venue;
		this.oferta = oferta;
		this.organizador = organizador;
		this.tiquetes = tiquetes;
	}
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	public Oferta getOferta() {
		return oferta;
	}
	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}
	public Organizador getOrganizador() {
		return organizador;
	}
	public void setOrganizador(Organizador organizador) {
		this.organizador = organizador;
	}
	public ArrayList<Tiquete> getTiquetes() {
		return tiquetes;
	}
	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}
	public String getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(String idEvento) {
		this.idEvento = idEvento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Time getHora() {
		return hora;
	}
	public void setHora(Time hora) {
		this.hora = hora;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	public void asociarVenue(Venue venue) {
		setVenue(venue);
	}
	public void cancelar(String motivo) {
		setEstado("Cancelado");
	}
	
}
