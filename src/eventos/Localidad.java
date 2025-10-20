package eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tiquetes.Tiquete;

/**
 * Modela una localidad de un evento en la plataforma BoletaMaster.
 * <p>
 * Reglas de dominio relevantes:
 * <ul>
 *   <li>Las localidades se configuran por evento (no son inherentes al venue).</li>
 *   <li>Todas las boletas de una misma localidad tienen el mismo precio base.</li>
 *   <li>Si la localidad es numerada, cada tiquete debe tener un asiento único en la localidad.</li>
 * </ul>
 * Una localidad puede tener una {@link eventos.Oferta} vigente y agrupar múltiples
 * {@link tiquetes.Tiquete}.
 */
public class Localidad {
    private String nombre;
    private double precioBase;
    private boolean numerada;
    private int numeroAsientos;
    private final List<Tiquete> tiquetes;
    private Oferta oferta;

    /**
     * Crea una localidad con su configuración básica y su colección inicial de tiquetes.
     *
     * @param oferta          oferta asociada (opcional; puede ser {@code null}).
     * @param tiquetes        lista inicial de tiquetes (opcional; se copia si no es {@code null}).
     * @param nombre          nombre de la localidad (obligatorio).
     * @param precioBase      precio base uniforme para los tiquetes de esta localidad (>= 0).
     * @param numerada        indica si la localidad es numerada (asientos únicos por tiquete).
     * @param numeroAsientos  capacidad en asientos (debe ser {@code >= 0}).
     *
     * @throws NullPointerException     si {@code nombre} es {@code null}.
     * @throws IllegalArgumentException si {@code precioBase} o {@code numeroAsientos} son negativos.
     */
    public Localidad(Oferta oferta, ArrayList<Tiquete> tiquetes, String nombre, double precioBase, boolean numerada,
            int numeroAsientos) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la localidad es obligatorio");
        this.precioBase = precioBase;
        this.numerada = numerada;
        this.numeroAsientos = numeroAsientos;
        this.oferta = oferta;
        this.tiquetes = new ArrayList<>();
        if (tiquetes != null) {
            this.tiquetes.addAll(tiquetes);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la localidad es obligatorio");
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base debe ser positivo");
        }
        this.precioBase = precioBase;
    }

    public boolean isNumerada() {
        return numerada;
    }

    public void setNumerada(boolean numerada) {
        this.numerada = numerada;
    }

    public int getNumeroAsientos() {
        return numeroAsientos;
    }

    public void setNumeroAsientos(int numeroAsientos) {
        if (numeroAsientos < 0) {
            throw new IllegalArgumentException("El número de asientos debe ser positivo");
        }
        this.numeroAsientos = numeroAsientos;
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

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }
    /**
     * Agrega un {@link tiquetes.Tiquete} a la colección de la localidad.
     * <p>
     * Nota: la validación de reglas específicas (p. ej., asignación de asiento único
     * en localidades numeradas) debe realizarla la capa que orquesta el caso de uso.
     *
     * @param tiquete tiquete a agregar (obligatorio).
     * @throws NullPointerException si {@code tiquete} es {@code null}.
     */
    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }
    /**
     * Fija el precio base uniforme de la localidad.
     * <p>
     * Atajo para {@link #setPrecioBase(double)} con validación de no negatividad.
     *
     * @param precio nuevo precio base (debe ser {@code >= 0}).
     * @throws IllegalArgumentException si {@code precio} es negativo.
     */
    public void fijarPrecio(double precio) {
        setPrecioBase(precio);
    }
}