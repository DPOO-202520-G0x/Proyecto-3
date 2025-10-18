package Cliente;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eventos.Evento;
import eventos.Localidad;
import eventos.Oferta;
import eventos.TipoEvento;
import eventos.Venue;
import tiquetes.Tiquete;
import tiquetes.Transaccion;

public class Organizador extends Usuario {
    private final String idOrganizador;
    private double finanzas;
    private final List<Evento> eventos;

    public Organizador(String login, String password, String nombre, double saldo, String idOrganizador,
            ArrayList<Evento> eventos) {
        super(login, password, nombre, saldo);
        this.idOrganizador = Objects.requireNonNull(idOrganizador, "El identificador del organizador es obligatorio");
        this.eventos = new ArrayList<>();
        if (eventos != null) {
            this.eventos.addAll(eventos);
        }
    }

    public double getFinanzas() {
        return finanzas;
    }

    public void setFinanzas(double finanzas) {
        if (finanzas < 0) {
            throw new IllegalArgumentException("Las finanzas no pueden ser negativas");
        }
        this.finanzas = finanzas;
    }

    public String getIdOrganizador() {
        return idOrganizador;
    }

    public void setIdOrganizador(String idOrganizador) {
        throw new UnsupportedOperationException("El identificador del organizador es inmutable");
    }

    public ArrayList<Evento> getEventos() {
        return new ArrayList<>(eventos);
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos.clear();
        if (eventos != null) {
            this.eventos.addAll(eventos);
        }
    }

    public Evento crearEvento(Administrador administrador, String idEvento, String nombre, LocalDate fecha, LocalTime hora,
            String estado, TipoEvento tipoEvento, Venue venue, Oferta oferta, Organizador organizador,
            ArrayList<Tiquete> tiquetes) {
        Objects.requireNonNull(administrador, "El administrador es obligatorio");
        Objects.requireNonNull(venue, "El venue es obligatorio");
        Objects.requireNonNull(fecha, "La fecha es obligatoria");
        Objects.requireNonNull(hora, "La hora es obligatoria");
        if (!administrador.getVenuesAprobados().contains(venue)) {
            throw new IllegalArgumentException("El venue no ha sido aprobado por el administrador");
        }
        if (venue.tieneEventoEnFecha(fecha)) {
            throw new IllegalArgumentException("El venue ya tiene un evento programado en la fecha indicada");
        }
        Evento evento = new Evento(administrador, idEvento, nombre, fecha, hora, estado, tipoEvento, venue, oferta,
                organizador, tiquetes);
        eventos.add(evento);
        venue.registrarEvento(evento);
        return evento;
    }

    public void asignarLocalidades(Venue venue, Evento evento, String nombre, double precio, boolean numerada, int cupos,
            ArrayList<Localidad> localidades) {
        Objects.requireNonNull(venue, "El venue es obligatorio");
        Objects.requireNonNull(evento, "El evento es obligatorio");
        ArrayList<Localidad> definidas = localidades != null ? localidades : new ArrayList<>();
        Localidad localidad = new Localidad(null, new ArrayList<>(), nombre, precio, numerada, cupos);
        definidas.add(localidad);
        venue.setLocalidades(definidas);
        evento.asociarVenue(venue);
    }

    public Oferta crearOferta(String idLocalidad, String nombre, double precioBase, boolean numerada, Localidad localidad,
            Evento evento) {
        Objects.requireNonNull(localidad, "La localidad es obligatoria");
        Objects.requireNonNull(evento, "El evento es obligatorio");
        Oferta oferta = new Oferta(idLocalidad, nombre, precioBase, numerada, localidad, evento);
        evento.setOferta(oferta);
        return oferta;
    }

    public Transaccion comprarComoCortesia(Transaccion transaccion, Tiquete tiquete) {
        Objects.requireNonNull(transaccion, "La transacción es obligatoria");
        Objects.requireNonNull(tiquete, "El tiquete es obligatorio");
        tiquete.setPrecio(0);
        tiquete.setEstado("CORTESIA");
        return transaccion;
    }

    public double consultarFinanzas(String login, String password) {
        if (!autenticar(login, password)) {
            throw new SecurityException("Credenciales inválidas");
        }
        return finanzas;
    }
}