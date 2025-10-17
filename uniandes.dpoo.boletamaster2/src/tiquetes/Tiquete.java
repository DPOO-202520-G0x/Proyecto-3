package tiquetes;
import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;
public abstract class Tiquete {
	private int idTiquete;
	private double precio;
	private double cargoServicio;
	private double cargoEmision;
	private String estado;
	private Localidad localidad;
	private Evento evento;
	private Cliente cliente;
	public int getIdTiquete() {
		return idTiquete;
	}
	public void setIdTiquete(int idTiquete) {
		this.idTiquete = idTiquete;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public double getCargoServicio() {
		return cargoServicio;
	}
	public void setCargoServicio(double cargoServicio) {
		this.cargoServicio = cargoServicio;
	}
	public double getCargoEmision() {
		return cargoEmision;
	}
	public void setCargoEmision(double cargoEmision) {
		this.cargoEmision = cargoEmision;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Localidad getLocalidad() {
		return localidad;
	}
	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	
	}
	public void setLocalidad(Evento evento) {
		this.evento = evento;
	
	}
	
	public Tiquete(Cliente cliente, int idTiquete, double precio, double cargoServicio, double cargoEmision, String estado,
			Localidad localidad, Evento evento) {
		super();
		this.idTiquete = idTiquete;
		this.precio = precio;
		this.cargoServicio = cargoServicio;
		this.cargoEmision = cargoEmision;
		this.estado = estado;
		this.localidad = localidad;
		this.evento=evento;
		this.cliente=cliente;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
