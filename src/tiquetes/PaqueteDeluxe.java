package tiquetes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/**
 * Paquete de tiquetes de tipo Deluxe.
 * <p>
 * Un Paquete Deluxe agrupa tiquetes incluidos (heredados de {@link tiquetes.PaqueteTiquetes}),
 * además de:
 * <ul>
 *   <li>una lista de beneficios asociados (p. ej., accesos/servicios adicionales), y</li>
 *   <li>una lista de tiquetes de cortesía (sin costo para el cliente).</li>
 * </ul>
 * <p>
 * Por diseño, este paquete se crea como no fraccionable (ver {@code super(false)}).
 */
public class PaqueteDeluxe extends PaqueteTiquetes {
    private  List<String> beneficios = new ArrayList<>();
    private  List<Tiquete> cortesias = new ArrayList<>(); 
    /**
     * Crea un paquete Deluxe con beneficios y tiquetes incluidos iniciales.
     *
     * @param beneficiosIniciales         colección de beneficios a agregar (opcional; si es {@code null} no se agrega nada).
     * @param tiquetesIncluidosIniciales  colección inicial de tiquetes incluidos (opcional; si es {@code null} no se agrega nada).
     *                                    Cada tiquete se agrega mediante {@link #agregarTiquete(tiquetes.Tiquete)} del padre.
     */
    public PaqueteDeluxe(Collection<String> beneficiosIniciales,
                         Collection<Tiquete> tiquetesIncluidosIniciales) {
        super(false); 
        if (beneficiosIniciales != null) beneficios.addAll(beneficiosIniciales);
        if (tiquetesIncluidosIniciales != null) {
            for (Tiquete t : tiquetesIncluidosIniciales) super.agregarTiquete(t);
        }
    }

    public List<String> getBeneficios() 
    { 
    	return Collections.unmodifiableList(beneficios); 
    }
    
    /**
     * Agrega un beneficio al paquete Deluxe.
     *
     * @param b beneficio a registrar (obligatorio).
     * @throws NullPointerException si {@code b} es {@code null}.
     */
    public void agregarBeneficio(String b) { beneficios.add(Objects.requireNonNull(b)); }

    public List<Tiquete> getCortesias() 
    { 
    	return Collections.unmodifiableList(cortesias); 
    	}
    /**
     * Agrega un tiquete de cortesía al paquete Deluxe.
     * <p>
     * Nota: las cortesías no forman parte de la lista de tiquetes "incluidos" del paquete base;
     * se mantienen en una colección separada.
     *
     * @param t tiquete de cortesía (obligatorio).
     * @throws NullPointerException si {@code t} es {@code null}.
     */
    public void agregarCortesia(Tiquete t) { cortesias.add(Objects.requireNonNull(t)); }

    /**
     * Retorna una vista inmodificable con todos los accesos del paquete:
     * tiquetes incluidos (del padre) + tiquetes de cortesía.
     *
     * @return lista inmodificable con todos los tiquetes accesibles por el paquete.
     */
    public List<Tiquete> todosLosAccesos() {
        List<Tiquete> all = new ArrayList<>(getTiquetes());
        all.addAll(cortesias);
        return Collections.unmodifiableList(all);
    }
    /**
     * Agrega un tiquete al conjunto de tiquetes incluidos del paquete Deluxe,
     * delegando en la implementación de {@link tiquetes.PaqueteTiquetes#agregarTiquete(tiquetes.Tiquete)}.
     *
     * @param tiquete tiquete a agregar (obligatorio).
     * @throws NullPointerException si {@code tiquete} es {@code null}.
     * @throws IllegalArgumentException si la implementación base restringe la operación (p. ej., no fraccionable).
     */
    @Override 
    public void agregarTiquete(Tiquete tiquete) {
        super.agregarTiquete(tiquete);
    }
}
