package tiquetes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/**
 * Clase base abstracta para paquetes de tiquetes.
 * <p>
 * Mantiene una colección interna de {@link tiquetes.Tiquete} y una bandera de
 * transferibilidad del paquete completo (según las políticas del dominio).
 * Subclases como {@code PaqueteDeluxe} pueden especializar reglas de negocio.
 */
public abstract class PaqueteTiquetes {
    private final List<Tiquete> tiquetes;
    private boolean transferible;
    /**
     * Construye un paquete de tiquetes.
     *
     * @param transferible indica si el paquete completo puede transferirse según
     *                     las políticas del dominio (no implica transferibilidad de
     *                     tiquetes individuales).
     */
    protected PaqueteTiquetes(boolean transferible) {
        this.transferible = transferible;
        this.tiquetes = new ArrayList<>();
    }

    public List<Tiquete> getTiquetes() {
        return Collections.unmodifiableList(tiquetes);
    }
    /**
     * Agrega un {@link tiquetes.Tiquete} al paquete.
     *
     * @param tiquete tiquete a incluir (obligatorio).
     * @throws NullPointerException si {@code tiquete} es {@code null}.
     */
    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }
    /**
     * Elimina todos los tiquetes del paquete.
     * <p>
     * Método de soporte para subclases que necesiten reinicializar la colección.
     * No realiza validaciones adicionales.
     */
    protected void limpiarTiquetes() {
        tiquetes.clear();
    }

    public boolean esTransferible() {
        return transferible;
    }

    protected void setTransferible(boolean transferible) {
        this.transferible = transferible;
    }
}