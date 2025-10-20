package eventos;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa una oferta (descuento porcentual) aplicable a una {@link eventos.Localidad}
 * de un {@link eventos.Evento} dentro de una ventana de tiempo.
 * <p>
 * Reglas de dominio:
 * <ul>
 *   <li>Descuento porcentual en el rango 0..100 (p. ej., 10 = 10%).</li>
 *   <li>Vigencia definida por un intervalo [inicio, fin] inclusivo en los extremos.</li>
 *   <li>La oferta es válida solo para la localidad y el evento asociados.</li>
 * </ul>
 * Esta clase es inmutable.
 */
public final class Oferta {
    private final Localidad localidad;
    private final Evento evento;
    private final double porcentaje;          // 0..100 (ej. 10 = 10%)
    private final LocalDateTime inicio;       
    private final LocalDateTime fin;          

    /**
     * Crea una oferta con localidad, evento, descuento y ventana de vigencia.
     *
     * @param localidad   localidad a la que aplica la oferta (obligatoria).
     * @param evento      evento al que pertenece la localidad (obligatorio).
     * @param porcentaje  porcentaje de descuento en 0..100 (10 = 10%).
     * @param inicio      fecha/hora de inicio de la vigencia (obligatoria).
     * @param fin         fecha/hora de fin de la vigencia (obligatoria; >= inicio).
     *
     * @throws NullPointerException     si {@code localidad}, {@code evento}, {@code inicio} o {@code fin} son {@code null}.
     * @throws IllegalArgumentException si {@code porcentaje} está fuera de 0..100 o si {@code fin} es anterior a {@code inicio}.
     */
    public Oferta(Localidad localidad,
                  Evento evento,
                  double porcentaje,          // 0..100
                  LocalDateTime inicio,
                  LocalDateTime fin) {
        this.localidad = Objects.requireNonNull(localidad, "La localidad es obligatoria");
        this.evento    = Objects.requireNonNull(evento, "El evento es obligatorio");
        if (porcentaje < 0.0 || porcentaje > 100.0) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100 (ej. 10 = 10%).");
        }
        this.porcentaje = porcentaje;
        this.inicio     = Objects.requireNonNull(inicio, "La fecha/hora de inicio es obligatoria");
        this.fin        = Objects.requireNonNull(fin, "La fecha/hora de fin es obligatoria");
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha/hora fin debe ser posterior o igual a inicio.");
        }
    }
    /**
     * Crea una oferta "abierta" (0% de descuento, vigencia ilimitada) asociada a una localidad y evento.
     * <p>
     * Atajo del constructor principal con:
     * <ul>
     *   <li>porcentaje = 0.0</li>
     *   <li>inicio = {@code LocalDateTime.MIN}</li>
     *   <li>fin = {@code LocalDateTime.MAX}</li>
     * </ul>
     *
     * @param idLocalidad no utilizado en esta implementación (parámetro de compatibilidad).
     * @param nombre      no utilizado en esta implementación (parámetro de compatibilidad).
     * @param precioBase  no utilizado en esta implementación (parámetro de compatibilidad).
     * @param numerada    no utilizado en esta implementación (parámetro de compatibilidad).
     * @param localidad   localidad objetivo (obligatoria).
     * @param evento      evento objetivo (obligatorio).
     *
     * @throws NullPointerException si {@code localidad} o {@code evento} son {@code null}.
     */
    public Oferta(String idLocalidad, String nombre, double precioBase, boolean numerada,Localidad localidad, Evento evento) {
    	this(localidad, evento, 0.0, LocalDateTime.MIN, LocalDateTime.MAX);
}
    
    /**
     * Indica si la oferta está vigente en el tiempo indicado.
     * <p>
     * La vigencia incluye los extremos del intervalo: {@code now} puede ser igual a
     * {@code inicio} o igual a {@code fin}.
     *
     * @param now instante a evaluar (obligatorio).
     * @return {@code true} si {@code now} está en [inicio, fin]; {@code false} en caso contrario.
     * @throws NullPointerException si {@code now} es {@code null}.
     */
    public boolean activa(LocalDateTime now) {
        return (now.equals(inicio) || now.isAfter(inicio))
            && (now.equals(fin)    || now.isBefore(fin));
    }
    
    /**
     * Aplica el descuento al precio base si la oferta está vigente en el instante dado.
     *
     * @param precioBase precio original sobre el que se aplica el descuento.
     * @param now        instante de evaluación de vigencia (obligatorio).
     * @return precio con descuento si la oferta está activa; en caso contrario, {@code precioBase}.
     * @throws NullPointerException si {@code now} es {@code null}.
     */
    public double aplicar(double precioBase, LocalDateTime now) {
        if (!activa(now)) return precioBase;
        double factor = 1.0 - (porcentaje / 100.0);
        return precioBase * factor;
    }

    public Localidad getLocalidad() 
    { return localidad; 
    }
    
    public Evento getEvento()       
    { return evento; 
    }
    
    public double getPorcentaje()   
    { return porcentaje; 
    }
    
    public LocalDateTime getInicio()
    { return inicio; 
    }
    
    public LocalDateTime getFin()   
    { return fin; 
    }
}
