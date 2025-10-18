package tiquetes;

import java.util.ArrayList;
import java.util.List;

import eventos.Evento;

public class TiqueteTemporada extends PaqueteTiquetes {
    private int cantidadEventos;
    private double precioTotal;
    private final List<Evento> eventosTemporada;

    public TiqueteTemporada(int cantidadEventos, double precioTotal) {
        super(true);
        if (cantidadEventos <= 0) {
            throw new IllegalArgumentException("La cantidad de eventos debe ser positiva");
        }
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio total debe ser positivo");
        }
        this.cantidadEventos = cantidadEventos;
        this.precioTotal = precioTotal;
        this.eventosTemporada = new ArrayList<>();
    }

    public int getCantidadEventos() {
        return cantidadEventos;
    }

    public void setCantidadEventos(int cantidadEventos) {
        if (cantidadEventos <= 0) {
            throw new IllegalArgumentException("La cantidad de eventos debe ser positiva");
        }
        this.cantidadEventos = cantidadEventos;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio total debe ser positivo");
        }
        this.precioTotal = precioTotal;
    }

    public List<Evento> getEventosTemporada() {
        return new ArrayList<>(eventosTemporada);
    }

    public void definirEventos(ArrayList<Evento> eventos) {
        eventosTemporada.clear();
        if (eventos != null) {
            eventosTemporada.addAll(eventos);
        }
    }
}