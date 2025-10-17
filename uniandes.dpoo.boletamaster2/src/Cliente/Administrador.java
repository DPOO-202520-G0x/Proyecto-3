package Cliente;

import java.util.ArrayList;

import eventos.Evento;
import eventos.TipoEvento;
import eventos.Venue;
import tiquetes.Tiquete;
import eventos.Venue;

public class Administrador extends Usuario {
	
	private String idAdministrador;
	private double ganancias;
	private ArrayList<Venue> venuesAprobados;
	
	
	

	public String getidAdministrador() {
		return idAdministrador;
		
	}

	public ArrayList<Venue> getVenuesAprobados() {
		return venuesAprobados;
	}

	public void setVenuesAprobados(ArrayList<Venue> venuesAprobados) {
		this.venuesAprobados = venuesAprobados;
	}

	public void setIdAdministrador(String idAdministrador) {
		this.idAdministrador = idAdministrador;
	}

	public Administrador(double ganancias, String idAdministrador, ArrayList<Venue> venuesAprobados, String login, String password, String nombre, double saldo) {
		super(login, password, nombre, saldo);
		this.idAdministrador = idAdministrador;
		this.venuesAprobados=new ArrayList<>();
		this.ganancias=ganancias;
		
		
	}
	public double getGanancias() {
		return ganancias;
	}

	public void setGanancias(double ganancias) {
		this.ganancias = ganancias;
	}

	public String getIdAdministrador() {
		return idAdministrador;
	}

	public void aprobarVenue(Venue venue, boolean decision){
		if (decision==true) {
			venuesAprobados.add(venue);
		}
	}
	
	public void cancelarEevento(Evento evento){
		evento.setEstado("Cancelado");
		
	}
	
	public void fijarCargoServicio(TipoEvento tipoEvento, double porcentaje) {
	
	}
	public void fijarCargoEmision(double cargo, Tiquete tiquete) {
		tiquete.setCargoEmision(cargo);
	}
	public double verReporteGanancias(String Login, String Password) {
		if (Login.equals(login) && Password.equals(password)) {
			return ganancias;
		}
		return 0;
	}
	
	public boolean decidirReembolso(String solicitud, boolean decision) {
		return decision;
	}
}
