package Cliente;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;


import Cliente.Administrador;
import eventos.Evento;
import eventos.Localidad;
import eventos.Oferta;
import eventos.TipoEvento;
import eventos.Venue;
import tiquetes.Tiquete;
import tiquetes.Transaccion;

public class Organizador extends Usuario{

	private String idOrganizador;
	private double finanzas;
	public double getFinanzas() {
		return finanzas;
	}


	public void setFinanzas(double finanzas) {
		this.finanzas = finanzas;
	}

	private ArrayList<Evento> eventos;
	
	
	public Organizador(String login, String password, String nombre, double saldo, String idOrganizador, ArrayList<Evento> eventos) {
		super(login, password, nombre, saldo);
		this.idOrganizador = idOrganizador;
		this.eventos = eventos;
		
	}


	public String getIdOrganizador() {
		return idOrganizador;
	}


	public void setIdOrganizador(String idOrganizador) {
		this.idOrganizador = idOrganizador;
	}


	public ArrayList<Evento> getEventos() {
		return eventos;
	}


	public void setEventos(ArrayList<Evento> eventos) {
		this.eventos = eventos;
	}

	public Evento crearEvento(Administrador administrador ,String idEvento, String nombre, Date fecha, Time hora, String estado, TipoEvento tipoEvento,
			Venue venue, Oferta oferta, Organizador organizador, ArrayList<Tiquete> tiquetes) {
		
		ArrayList<Venue> venuesAprobados = administrador.getVenuesAprobados();
		if (venuesAprobados.contains(venue)) {
			Evento evento= new Evento(administrador, idEvento, nombre, fecha, hora, estado, tipoEvento, venue, oferta, organizador, tiquetes);
			return evento;
	}
		return null;
	}
	
	public void asignarLocalidades(Venue venue, Evento evento, String nombre, double precio, boolean numerada, int cupos, ArrayList<Localidad> localidades) {
		venue.setLocalidades(localidades);
		evento.setVenue(venue);
	}
	public Oferta crearOferta(String idLocalidad, String nombre, double precioBase, boolean numerada, Localidad localidad, Evento evento) {
		Oferta oferta= new Oferta(idLocalidad, nombre, precioBase, numerada,localidad, evento);
		return oferta;
	
	}
	
	public Transaccion comprarComoCortestia(Transaccion transaccion, Tiquete tiquete) {
		return transaccion;
	}
	
	public double consultarFinanzas(String Login, String Password) {
		if(Login.equals(login) && Password.equals(password)) {
			return finanzas;
		}
		return 0;
	}
}
	
	


