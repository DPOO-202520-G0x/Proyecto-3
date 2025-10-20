package tiquetes;

import java.util.ArrayList;
import java.util.List;

import eventos.Evento;
/**
 * Paquete de tiquetes tipo "temporada", que agrupa entradas para varios {@link eventos.Evento}.
 * <p>
 * Extiende {@link tiquetes.PaqueteTiquetes} y se construye como transferible del
 * paquete completo (véase {@code super(true)}). Las políticas de dominio (p. ej.,
 * tope aplicado al paquete, manejo de eventos vencidos, etc.) deben validarse en
 * la capa de aplicación que orquesta el caso de uso.
 */
public class TiqueteTemporada extends PaqueteTiquetes {
    private int cantidadEventos;
    private double precioTotal;
    private final List<Evento> eventosTemporada;
    /**
     * Crea un paquete de temporada con una cantidad de eventos y un precio total.
     *
     * @param cantidadEventos número de eventos incluidos en la temporada (debe ser {@code > 0}).
     * @param precioTotal     precio total del paquete de temporada (debe ser {@code >= 0}).
     *
     * @throws IllegalArgumentException si {@code cantidadEventos <= 0} o {@code precioTotal < 0}.
     */
    public TiqueteTemporada(int cantidadEventos, double precioTotal) {
        super(true);
        if (cantidadEventos <= 0) {
            throw new IllegalArgumentException("La cantidad de eventos debe ser positiva");
        }
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio total debe ser positivo");
        }
        this.cantidadEventos = cantidadEventos;
        this.precioTotal = precioTotal;
        this.eventosTemporada = new ArrayList<>();
    }

    public int getCantidadEventos() {
        return cantidadEventos;
    }

    public void setCantidadEventos(int cantidadEventos) {
        if (cantidadEventos <= 0) {
            throw new IllegalArgumentException("La cantidad de eventos debe ser positiva");
        }
        this.cantidadEventos = cantidadEventos;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio total debe ser positivo");
        }
        this.precioTotal = precioTotal;
    }

    public List<Evento> getEventosTemporada() {
        return new ArrayList<>(eventosTemporada);
    }
    /**
     * Define (reemplaza) el conjunto de {@link eventos.Evento} que componen la temporada.
     * <p>
     * Efecto: limpia la lista interna y agrega todos los eventos proporcionados.
     * No valida superposiciones de fechas ni disponibilidad; esas reglas deben
     * aplicarse en una capa superior.
     *
     * @param eventos lista de eventos que compondrán la temporada (puede ser {@code null} para dejarla vacía).
     */
    public void definirEventos(ArrayList<Evento> eventos) {
        eventosTemporada.clear();
        if (eventos != null) {
            eventosTemporada.addAll(eventos);
        }
    }
}