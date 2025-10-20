package tiquetes;

import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;
/**
 * Tiquete individual (básico) emitido para una {@link eventos.Localidad} de un {@link eventos.Evento}.
 * <p>
 * Puede opcionalmente tener número de asiento cuando la localidad es numerada.
 * Reglas de dominio relacionadas:
 * <ul>
 *   <li>Si la localidad es numerada, cada tiquete debe tener un asiento único dentro de la localidad.</li>
 *   <li>El precio total del tiquete se calcula en la superclase {@link tiquetes.Tiquete}.</li>
 * </ul>
 */
public class TiqueteBasico extends Tiquete {
    private Integer numeroAsiento;
    private boolean localidadNumerada;
    /**
     * Construye un tiquete básico.
     *
     * @param cliente            propietario inicial del tiquete (puede ser {@code null} si se asignará luego).
     * @param idTiquete          identificador único del tiquete.
     * @param precio             precio base del tiquete (no negativo según reglas externas).
     * @param cargoServicio      cargo por servicio (no negativo).
     * @param cargoEmision       cargo de emisión (no negativo).
     * @param estado             estado del tiquete (obligatorio).
     * @param localidad          localidad asociada (puede ser {@code null} si se define más adelante).
     * @param evento             evento al que pertenece el tiquete (obligatorio).
     * @param numeroAsiento      número de asiento (puede ser {@code null} si la localidad no es numerada o si se asignará después).
     * @param localidadNumerada  indica si la localidad es numerada.
     *
     * @throws NullPointerException si {@code estado} o {@code evento} son {@code null}.
     */
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
    /**
     * Asigna (o reasigna) el número de asiento del tiquete básico.
     * <p>
     * Nota de dominio: la unicidad del asiento por localidad numerada debe
     * verificarse en una capa superior antes de llamar este método.
     *
     * @param numAsiento número de asiento a establecer.
     */
    public void asignarAsiento(int numAsiento) {
        this.numeroAsiento = numAsiento;
    }
}