package eventos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa un recinto (venue) donde se realizan eventos de BoletaMaster.
 * <p>
 * Aspectos de dominio relevantes:
 * <ul>
 *   <li>Los eventos se asocian a un venue específico.</li>
 *   <li>Un venue no puede tener dos eventos programados el mismo día.</li>
 *   <li>Las localidades se configuran por evento, pero pueden gestionarse desde el venue como soporte.</li>
 * </ul>
 * Mantiene además el registro interno de eventos programados para validaciones de disponibilidad.
 */
public class Venue {
    private String idVenue;
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private final List<Localidad> localidades;
    private final List<Evento> eventosProgramados;
    /**
     * Crea un venue con datos básicos, localidades iniciales y lista de eventos vacía.
     *
     * @param idVenue         identificador único del venue (obligatorio).
     * @param nombre          nombre del venue (obligatorio).
     * @param ubicacion       ubicación (obligatoria).
     * @param capacidadMaxima capacidad máxima de asistentes (>= 0).
     * @param localidades     lista inicial de localidades (opcional; se copia si no es {@code null}).
     *
     * @throws NullPointerException     si {@code idVenue}, {@code nombre} o {@code ubicacion} son {@code null}.
     * @throws IllegalArgumentException si {@code capacidadMaxima} es negativa.
     */
    public Venue(String idVenue, String nombre, String ubicacion, int capacidadMaxima, ArrayList<Localidad> localidades) {
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
    /**
     * Agrega una {@link eventos.Localidad} a la colección del venue.
     *
     * @param localidad localidad a agregar (obligatoria).
     * @throws NullPointerException si {@code localidad} es {@code null}.
     */
    public void agregarLocalidad(Localidad localidad) {
        localidades.add(Objects.requireNonNull(localidad, "La localidad es obligatoria"));
    }
    
    /**
     * Verifica si ya existe un {@link eventos.Evento} programado en la fecha dada.
     * <p>
     * Regla de dominio: un venue no puede albergar dos eventos en la misma fecha.
     *
     * @param fecha fecha a consultar (obligatoria).
     * @return {@code true} si hay un evento en esa fecha; {@code false} en caso contrario.
     * @throws NullPointerException si {@code fecha} es {@code null}.
     */
    public boolean tieneEventoEnFecha(LocalDate fecha) {
        for (Evento evento : eventosProgramados) {
            if (evento.getFecha().equals(fecha)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Registra un {@link eventos.Evento} en la lista interna de eventos programados del venue.
     * <p>
     * No realiza validación de superposición de fechas; se asume que la capa de aplicación
     * invocó previamente {@link #tieneEventoEnFecha(java.time.LocalDate)} y/o aplicó las reglas del dominio.
     *
     * @param evento evento a registrar (obligatorio).
     * @throws NullPointerException si {@code evento} es {@code null}.
     */
    public void registrarEvento(Evento evento) {
        eventosProgramados.add(Objects.requireNonNull(evento, "El evento es obligatorio"));
    }
    /**
     * Elimina un {@link eventos.Evento} del registro interno de eventos programados.
     *
     * @param evento evento a remover (si no está presente, la operación no tiene efecto).
     */
    public void removerEvento(Evento evento) {
        eventosProgramados.remove(evento);
    }
}