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
/**
 * Representa al administrador de la plataforma BoletaMaster.
 * <p>
 * Responsabilidades principales del administrador (según el dominio):
 * <ul>
 *   <li>Aprobar o rechazar {@link eventos.Venue} propuestos antes de su uso en eventos.</li>
 *   <li>Cancelar eventos y manejar las políticas de reembolso correspondientes.</li>
 *   <li>Fijar el cargo porcentual por servicio por {@link eventos.TipoEvento} y la cuota fija de emisión.</li>
 *   <li>Consultar reportes de ganancias de la tiquetera.</li>
 *   <li>Decidir sobre solicitudes de reembolso por calamidad.</li>
 * </ul>
 * Esta clase hereda credenciales y saldo de {@link Cliente.Usuario}.
 */
public class Administrador extends Usuario {
    private  String idAdministrador;
    private double ganancias;
    private final List<Venue> venuesAprobados;
    private final Map<TipoEvento, Double> cargosServicio;
    private double cargoEmision;

    
    /**
     * Crea un administrador.
     *
     * @param ganancias        ganancias iniciales de la tiquetera (no negativas).
     * @param idAdministrador  identificador único del administrador (obligatorio).
     * @param venuesAprobados  lista inicial de venues aprobados (se copia; puede ser {@code null}).
     * @param login            login del administrador (heredado de {@code Usuario}).
     * @param password         password del administrador (heredado de {@code Usuario}).
     * @param nombre           nombre del administrador (heredado de {@code Usuario}).
     * @param saldo            saldo inicial del administrador (heredado de {@code Usuario}).
     * @throws NullPointerException     si {@code idAdministrador} es {@code null}.
     */
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
       this.idAdministrador=idAdministrador;
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
    /**
     * Aprueba un {@link eventos.Venue} propuesto.
     * <p>
     * Si {@code decision} es {@code false}, no se realiza ninguna acción.
     * Si es {@code true}, el venue se agrega a la lista de venues aprobados, si aún no estaba.
     *
     * @param venue    venue a aprobar (obligatorio si {@code decision} es {@code true}).
     * @param decision decisión de aprobación ({@code true} para aprobar).
     * @throws NullPointerException si {@code decision} es {@code true} y {@code venue} es {@code null}.
     */
    public void aprobarVenue(Venue venue, boolean decision) {
        if (!decision) {
            return;
        }
        if (!venuesAprobados.contains(venue)) {
            venuesAprobados.add(Objects.requireNonNull(venue, "El venue es obligatorio"));
        }
    }
    
    /**
     * Cancela un {@link eventos.Evento} por decisión del administrador.
     * <p>
     * Efecto colateral: el evento queda en estado cancelado (según la implementación de {@code Evento.cancelar}).
     *
     * @param evento evento a cancelar (obligatorio).
     * @throws NullPointerException si {@code evento} es {@code null}.
     */
    public void cancelarEvento(Evento evento) {
        Objects.requireNonNull(evento, "El evento es obligatorio");
        evento.cancelar("Cancelado por administrador");
    }
    /**
     * Fija el cargo porcentual por servicio para un {@link eventos.TipoEvento}.
     *
     * @param tipoEvento tipo de evento al que se aplica el cargo (obligatorio).
     * @param porcentaje porcentaje de cargo de servicio (debe ser {@code >= 0}).
     * @throws IllegalArgumentException si {@code porcentaje} es negativo.
     * @throws NullPointerException     si {@code tipoEvento} es {@code null}.
     */
    public void fijarCargoServicio(TipoEvento tipoEvento, double porcentaje) {
        if (porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje de cargo de servicio debe ser positivo");
        }
        cargosServicio.put(Objects.requireNonNull(tipoEvento, "El tipo de evento es obligatorio"), porcentaje);
    }
    /**
     * Fija el cargo fijo por emisión de tiquetes para la tiquetera y, opcionalmente,
     * lo aplica inmediatamente a un {@link tiquetes.Tiquete} específico.
     *
     * @param cargo   valor del cargo fijo de emisión (debe ser {@code >= 0}).
     * @param tiquete tiquete al cual aplicar el cargo inmediatamente (opcional; puede ser {@code null}).
     * @throws IllegalArgumentException si {@code cargo} es negativo.
     */
    public void fijarCargoEmision(double cargo, Tiquete tiquete) {
        if (cargo < 0) {
            throw new IllegalArgumentException("El cargo de emisión debe ser positivo");
        }
        this.cargoEmision = cargo;
        if (tiquete != null) {
            tiquete.setCargoEmision(cargo);
        }
    }
    /**
     * Retorna el valor de las ganancias de la tiquetera, previa autenticación.
     *
     * @param login    login del administrador que consulta.
     * @param password password del administrador que consulta.
     * @return ganancias actuales de la tiquetera.
     * @throws SecurityException si las credenciales son inválidas.
     */
    public double verReporteGanancias(String login, String password) {
        if (!autenticar(login, password)) {
            throw new SecurityException("Credenciales inválidas");
        }
        return ganancias;
    }
    /**
     * Emite la decisión administrativa sobre una solicitud de reembolso por calamidad.
     *
     * @param solicitud descripción o identificador de la solicitud (obligatorio).
     * @param decision  {@code true} para aprobar; {@code false} para rechazar.
     * @return la decisión tomada ({@code true} aprobado, {@code false} rechazado).
     * @throws NullPointerException si {@code solicitud} es {@code null}.
     */
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