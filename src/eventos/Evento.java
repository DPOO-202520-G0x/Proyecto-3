package eventos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Cliente.Administrador;
import Cliente.Organizador;
import tiquetes.Tiquete;


/**
 * Representa un evento publicado en la plataforma BoletaMaster.
 * <p>
 * Un evento pertenece a un {@link Cliente.Organizador}, se realiza en un {@link eventos.Venue}
 * (que debe estar aprobado por el administrador) y puede tener una {@link eventos.Oferta}
 * y múltiples {@link tiquetes.Tiquete} vendidos/asignados.
 * <p>
 * Reglas de dominio relevantes:
 * <ul>
 *   <li>Un evento siempre tiene organizador y un venue asociado.</li>
 *   <li>Un venue no puede tener dos eventos el mismo día.</li>
 *   <li>Las localidades y precios se definen por evento (no son inherentes al venue).</li>
 *   <li>El porcentaje de venta del evento se puede calcular con base en la capacidad del venue.</li>
 * </ul>
 */
public class Evento {
    private Administrador administrador;
    private String idEvento;
    private String nombre;
    private LocalDate fecha; 
    private LocalTime hora; 
    private String estado;
    private TipoEvento tipoEvento;
    private Venue venue;
    private Oferta oferta;
    private Organizador organizador;
    private final List<Tiquete> tiquetes;

    /**
     * Crea un evento con todos sus datos principales y colecciones iniciales.
     *
     * @param administrador administrador a cargo/relacionado (obligatorio).
     * @param idEvento      identificador único del evento (obligatorio).
     * @param nombre        nombre del evento (obligatorio).
     * @param fecha         fecha del evento (obligatoria).
     * @param hora          hora del evento (obligatoria).
     * @param estado        estado inicial del evento (obligatorio).
     * @param tipoEvento    tipo del evento (obligatorio).
     * @param venue         venue donde se realizará el evento (puede ser {@code null} si se asociará después).
     * @param oferta        oferta inicial asociada al evento (opcional).
     * @param organizador   organizador del evento (puede ser {@code null} si se setea luego).
     * @param tiquetes      lista inicial de tiquetes (opcional; se copia si no es {@code null}).
     *
     * @throws NullPointerException si cualquiera de: {@code administrador}, {@code idEvento},
     *                              {@code nombre}, {@code fecha}, {@code hora}, {@code estado} o
     *                              {@code tipoEvento} es {@code null}.
     */
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
    
    public int getVendidos() {
    	return tiquetes.size();
    }
    
    public int getCapacidadMaxima() {
    	if (venue == null) {
    	    return 0;
    	} else {
    	    return venue.getCapacidadMaxima();
    	} 
    }
    
    public double getPorcentajeVenta() {
        int capacidad = getCapacidadMaxima();
        if (capacidad <= 0) return 0.0;
        return (getVendidos() * 100.0) / capacidad;
    }


    public void setTiquetes(ArrayList<Tiquete> tiquetes) {
        this.tiquetes.clear();
        if (tiquetes != null) {
            this.tiquetes.addAll(tiquetes);
        }
    }
    /**
     * Registra (agrega) un {@link tiquetes.Tiquete} al evento.
     *
     * @param tiquete tiquete a registrar (obligatorio).
     * @throws NullPointerException si {@code tiquete} es {@code null}.
     */
    public void registrarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }
    /**
     * Asocia un {@link eventos.Venue} al evento.
     * <p>
     * No valida aquí reglas de aprobación o disponibilidad; se asume
     * que dichas verificaciones se realizan en una capa superior.
     *
     * @param Venue venue a asociar (puede ser {@code null} para desasociar).
     */
    public void asociarVenue(Venue venue) {
        setVenue(venue);
    }
    /**
     * Cancela el evento estableciendo su estado como "Cancelado".
     * <p>
     * La lógica de reembolsos, notificaciones u otras acciones relacionadas
     * se gestionan fuera de este método.
     *
     * @param motivo motivo textual de cancelación (obligatorio).
     * @throws NullPointerException si {@code motivo} es {@code null}.
     */
    public void cancelar(String motivo) {
        Objects.requireNonNull(motivo, "El motivo es obligatorio");
        setEstado("Cancelado");
    }
}