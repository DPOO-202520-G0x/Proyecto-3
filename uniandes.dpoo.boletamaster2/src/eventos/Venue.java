package eventos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Venue {
    private String idVenue;
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private final List<Localidad> localidades;
    private final List<Evento> eventosProgramados;

    public Venue(String idVenue, String nombre, String ubicacion, int capacidad, ArrayList<Localidad> localidades) {
        this.idVenue = Objects.requireNonNull(idVenue, "El identificador del venue es obligatorio");
        this.nombre = Objects.requireNonNull(nombre, "El nombre del venue es obligatorio");
        this.ubicacion = Objects.requireNonNull(ubicacion, "La ubicación es obligatoria");
        this.capacidadMaxima = capacidadMaxima;
        this.localidades = new ArrayList<>();
        if (localidades != null) {
            this.localidades.addAll(localidades);
        }
        this.eventosProgramados = new ArrayList<>();
    }

    public String getIdVenue() {
        return idVenue;
    }

    public void setIdVenue(String idVenue) {
        this.idVenue = Objects.requireNonNull(idVenue, "El identificador del venue es obligatorio");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del venue es obligatorio");
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = Objects.requireNonNull(ubicacion, "La ubicación es obligatoria");
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        if (capacidadMaxima < 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.capacidadMaxima = capacidadMaxima;
    }

    public ArrayList<Localidad> getLocalidades() {
        return new ArrayList<>(localidades);
    }

    public void setLocalidades(ArrayList<Localidad> localidades) {
        this.localidades.clear();
        if (localidades != null) {
            this.localidades.addAll(localidades);
        }
    }

    public void agregarLocalidad(Localidad localidad) {
        localidades.add(Objects.requireNonNull(localidad, "La localidad es obligatoria"));
    }

    public boolean tieneEventoEnFecha(LocalDate fecha) {
        for (Evento evento : eventosProgramados) {
            if (evento.getFecha().equals(fecha)) {
                return true;
            }
        }
        return false;
    }

    public void registrarEvento(Evento evento) {
        eventosProgramados.add(Objects.requireNonNull(evento, "El evento es obligatorio"));
    }

    public void removerEvento(Evento evento) {
        eventosProgramados.remove(evento);
    }
}