package Cliente;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import eventos.Evento;
import eventos.TipoEvento;
import eventos.Venue;
import tiquetes.Tiquete;

public class Administrador extends Usuario {
    private final String idAdministrador;
    private double ganancias;
    private final List<Venue> venuesAprobados;
    private final Map<TipoEvento, Double> cargosServicio;
    private double cargoEmision;

    public Administrador(double ganancias, String idAdministrador, ArrayList<Venue> venuesAprobados, String login,
            String password, String nombre, double saldo) {
        super(login, password, nombre, saldo);
        this.idAdministrador = Objects.requireNonNull(idAdministrador, "El identificador del administrador es obligatorio");
        this.venuesAprobados = new ArrayList<>();
        if (venuesAprobados != null) {
            this.venuesAprobados.addAll(venuesAprobados);
        }
        this.cargosServicio = new EnumMap<>(TipoEvento.class);
        this.ganancias = ganancias;
    }

    public String getidAdministrador() {
        return idAdministrador;
    }

    public String getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(String idAdministrador) {
        throw new UnsupportedOperationException("El identificador del administrador es inmutable");
    }

    public ArrayList<Venue> getVenuesAprobados() {
        return new ArrayList<>(venuesAprobados);
    }

    public void setVenuesAprobados(ArrayList<Venue> venuesAprobados) {
        this.venuesAprobados.clear();
        if (venuesAprobados != null) {
            this.venuesAprobados.addAll(venuesAprobados);
        }
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {
        if (ganancias < 0) {
            throw new IllegalArgumentException("Las ganancias no pueden ser negativas");
        }
        this.ganancias = ganancias;
    }

    public void aprobarVenue(Venue venue, boolean decision) {
        if (!decision) {
            return;
        }
        if (!venuesAprobados.contains(venue)) {
            venuesAprobados.add(Objects.requireNonNull(venue, "El venue es obligatorio"));
        }
    }

    public void cancelarEvento(Evento evento) {
        Objects.requireNonNull(evento, "El evento es obligatorio");
        evento.cancelar("Cancelado por administrador");
    }

    public void fijarCargoServicio(TipoEvento tipoEvento, double porcentaje) {
        if (porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de cargo de servicio debe ser positivo");
        }
        cargosServicio.put(Objects.requireNonNull(tipoEvento, "El tipo de evento es obligatorio"), porcentaje);
    }

    public void fijarCargoEmision(double cargo, Tiquete tiquete) {
        if (cargo < 0) {
            throw new IllegalArgumentException("El cargo de emisión debe ser positivo");
        }
        this.cargoEmision = cargo;
        if (tiquete != null) {
            tiquete.setCargoEmision(cargo);
        }
    }

    public double verReporteGanancias(String login, String password) {
        if (!autenticar(login, password)) {
            throw new SecurityException("Credenciales inválidas");
        }
        return ganancias;
    }

    public boolean decidirReembolso(String solicitud, boolean decision) {
        Objects.requireNonNull(solicitud, "La solicitud es obligatoria");
        return decision;
    }

    public double getCargoServicio(TipoEvento tipoEvento) {
        return cargosServicio.getOrDefault(tipoEvento, 0.0);
    }

    public double getCargoEmision() {
        return cargoEmision;
    }
}