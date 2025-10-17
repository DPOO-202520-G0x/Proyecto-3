package Cliente;

import java.util.ArrayList;

import eventos.Evento;
import eventos.Localidad;
import tiquetes.Tiquete;

public class Cliente extends Usuario{
	private String idCliente;
	private ArrayList<Tiquete> tiquetes;
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public ArrayList<Tiquete> getTiquetes() {
		return tiquetes;
	}
	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}
	public Cliente(String login, String password, String nombre, double saldo, String idCliente) {
		super(login, password, nombre, saldo);
		this.idCliente = idCliente;
		this.tiquetes = tiquetes;
	}
	
	
	 {
		
	}
	
	
	
}
