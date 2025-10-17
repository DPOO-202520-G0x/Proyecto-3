package eventos;



public class Oferta {
	private String idLocalidad;
	private String nombre;
	private double precioBase;
	private boolean numerada;
	private Localidad localidad;
	private Evento evento;
	
	public Oferta(String idLocalidad, String nombre, double precioBase, boolean numerada, Localidad localidad, Evento evento) {
		super();
		this.idLocalidad = idLocalidad;
		this.nombre = nombre;
		this.precioBase = precioBase;
		this.numerada = numerada;
		this.localidad = localidad;
		this.evento=evento;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public String getIdLocalidad() {
		return idLocalidad;
	}
	public void setIdLocalidad(String idLocalidad) {
		this.idLocalidad = idLocalidad;
	}
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
	public Localidad getLocalidad() {
		return localidad;
	}
	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}
	
}
