package tiquetes;

import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;

public class TiqueteBasico extends Tiquete {
    private Integer numeroAsiento;
    private boolean localidadNumerada;

    public TiqueteBasico(Cliente cliente, int idTiquete, double precio, double cargoServicio, double cargoEmision,
            String estado, Localidad localidad, Evento evento, Integer numeroAsiento, boolean localidadNumerada) {
        super(cliente, idTiquete, precio, cargoServicio, cargoEmision, estado, localidad, evento);
        this.numeroAsiento = numeroAsiento;
        this.localidadNumerada = localidadNumerada;
    }

    public Integer getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public boolean isLocalidadNumerada() {
        return localidadNumerada;
    }

    public void setLocalidadNumerada(boolean localidadNumerada) {
        this.localidadNumerada = localidadNumerada;
    }

    public void asignarAsiento(int numAsiento) {
        this.numeroAsiento = numAsiento;
    }
}