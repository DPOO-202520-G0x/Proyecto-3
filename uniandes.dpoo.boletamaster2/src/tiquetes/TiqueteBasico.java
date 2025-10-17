package tiquetes;

import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;

public class TiqueteBasico extends Tiquete{
	private int numeroAsiento;
	private boolean localidadNumerada;
	
	public TiqueteBasico(Cliente cliente, int idTiquete, double precio, double cargoServicio, double cargoEmision,
			String estado, Localidad localidad, Evento evento) {
		super(cliente, idTiquete, precio, cargoServicio, cargoEmision, estado, localidad, evento);
		this.localidadNumerada=localidadNumerada;
		this.numeroAsiento=numeroAsiento;
	}

	public int getNumeroAsiento() {
		return numeroAsiento;
	}

	public void setNumeroAsiento(int numeroAsiento) {
		this.numeroAsiento = numeroAsiento;
	}

	public boolean isLocalidadNumerada() {
		return localidadNumerada;
	}

	public void setLocalidadNumerada(boolean localidadNumerada) {
		this.localidadNumerada = localidadNumerada;
	}
	public void asignarAsiento(int numAsiento) {
		setNumeroAsiento(numAsiento);
	}
	
	}
	
	