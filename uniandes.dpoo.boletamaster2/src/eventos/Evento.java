package eventos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Cliente.Administrador;
import Cliente.Organizador;
import tiquetes.Tiquete;

public class Evento {
    private Administrador administrador;
    private String idEvento;
    private String nombre;
    private LocalDate fecha; // TODO: confirmar tipo de fecha/hora según PDF/UML.
    private LocalTime hora; // TODO: confirmar tipo de fecha/hora según PDF/UML.
    private String estado;
    private TipoEvento tipoEvento;
    private Venue venue;
    private Oferta oferta;
    private Organizador organizador;
    private final List<Tiquete> tiquetes;

    public Evento(Administrador administrador, String idEvento, String nombre, LocalDate fecha, LocalTime hora,
            String estado, TipoEvento tipoEvento, Venue venue, Oferta oferta, Organizador organizador,
            ArrayList<Tiquete> tiquetes) {
        this.administrador = Objects.requireNonNull(administrador, "El administrador es obligatorio");
        this.idEvento = Objects.requireNonNull(idEvento, "El identificador del evento es obligatorio");
        this.nombre = Objects.requireNonNull(nombre, "El nombre del evento es obligatorio");
        this.fecha = Objects.requireNonNull(fecha, "La fecha es obligatoria");
        this.hora = Objects.requireNonNull(hora, "La hora es obligatoria");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.tipoEvento = Objects.requireNonNull(tipoEvento, "El tipo de evento es obligatorio");
        this.venue = venue;
        this.oferta = oferta;
        this.organizador = organizador;
        this.tiquetes = new ArrayList<>();
        if (tiquetes != null) {
            this.tiquetes.addAll(tiquetes);
        }
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = Objects.requireNonNull(administrador, "El administrador es obligatorio");
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = Objects.requireNonNull(idEvento, "El identificador del evento es obligatorio");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del evento es obligatorio");
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = Objects.requireNonNull(fecha, "La fecha es obligatoria");
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = Objects.requireNonNull(hora, "La hora es obligatoria");
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = Objects.requireNonNull(tipoEvento, "El tipo de evento es obligatorio");
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
        return new ArrayList<>(tiquetes);
    }

    public void setTiquetes(ArrayList<Tiquete> tiquetes) {
        this.tiquetes.clear();
        if (tiquetes != null) {
            this.tiquetes.addAll(tiquetes);
        }
    }

    public void registrarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }

    public void asociarVenue(Venue venue) {
        setVenue(venue);
    }

    public void cancelar(String motivo) {
        Objects.requireNonNull(motivo, "El motivo es obligatorio");
        setEstado("Cancelado");
    }
}