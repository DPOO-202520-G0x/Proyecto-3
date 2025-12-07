package tiquetes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;
import tiquetes.DatosImpresion;
/**
 * Clase base abstracta para todos los tiquetes de BoletaMaster.
 * <p>
 * Un tiquete pertenece a un {@link eventos.Evento}, está asociado a una
 * {@link eventos.Localidad} y puede tener un {@link Cliente.Cliente} como propietario.
 * Conserva su estado y los componentes de precio:
 * <ul>
 *   <li>precio base</li>
 *   <li>cargo porcentual por servicio</li>
 *   <li>cargo fijo de emisión</li>
 * </ul>
 * <p>
 * Proporciona utilidades comunes como el cálculo del valor total a pagar.
 */
public abstract class Tiquete {
    private int idTiquete;
    private double precio;
    private double cargoServicio;
    private double cargoEmision;
    private String estado;
    private Localidad localidad;
    private Evento evento;
    private Cliente cliente;
    private boolean impreso;
    private LocalDateTime fechaImpresion;
    /**
     * Construye un tiquete con sus datos de contexto y valores económicos.
     *
     * @param cliente        propietario inicial del tiquete (puede ser {@code null} si se asignará luego).
     * @param idTiquete      identificador único del tiquete.
     * @param precio         precio base (debe ser coherente con las reglas de negocio).
     * @param cargoServicio  cargo porcentual/valor por servicio (no negativo).
     * @param cargoEmision   cargo fijo de emisión (no negativo).
     * @param estado         estado del tiquete (obligatorio).
     * @param localidad      localidad asociada (puede ser {@code null} si se define más adelante).
     * @param evento         evento al que pertenece el tiquete (obligatorio).
     *
     * @throws NullPointerException si {@code estado} o {@code evento} son {@code null}.
     */
    protected Tiquete(Cliente cliente, int idTiquete, double precio, double cargoServicio, double cargoEmision,
            String estado, Localidad localidad, Evento evento) {
        this.cliente = cliente;
        this.idTiquete = idTiquete;
        this.precio = precio;
        this.cargoServicio = cargoServicio;
        this.cargoEmision = cargoEmision;
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.localidad = localidad;
        this.evento = Objects.requireNonNull(evento, "El evento es obligatorio");
    }

    public int getIdTiquete() {
        return idTiquete;
    }

    public void setIdTiquete(int idTiquete) {
        this.idTiquete = idTiquete;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        this.precio = precio;
    }

    public double getCargoServicio() {
        return cargoServicio;
    }

    public void setCargoServicio(double cargoServicio) {
        if (cargoServicio < 0) {
            throw new IllegalArgumentException("El cargo de servicio debe ser positivo");
        }
        this.cargoServicio = cargoServicio;
    }

    public double getCargoEmision() {
        return cargoEmision;
    }

    public void setCargoEmision(double cargoEmision) {
        if (cargoEmision < 0) {
            throw new IllegalArgumentException("El cargo de emisión debe ser positivo");
        }
        this.cargoEmision = cargoEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = Objects.requireNonNull(localidad, "La localidad es obligatoria");
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = Objects.requireNonNull(evento, "El evento es obligatorio");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Indica si el tiquete ya fue impreso al menos una vez.
     */
    public boolean estaImpreso() {
        return impreso;
    }

    /**
     * Fecha y hora en la que se imprimió el tiquete por primera vez.
     * Devuelve {@code null} si aún no se ha impreso.
     */
    public LocalDateTime getFechaImpresion() {
        return fechaImpresion;
    }

    /**
     * Genera una representación imprimible del tiquete y marca que ya fue impreso.
     * La representación incluye los datos clave y un bloque de datos que puede
     * alimentarse a un generador de códigos QR externo.
     *
     * @return cadena con la información a imprimir.
     * @throws IllegalStateException si el tiquete ya había sido impreso.
     */
    public synchronized String imprimir() {
        return imprimirConDatos().comoTexto();
    }

    /**
     * Variante estructurada que devuelve un objeto con toda la información de
     * impresión, incluyendo el payload plano para generar el QR.
     */
    public synchronized DatosImpresion imprimirConDatos() {
        if (impreso) {
            throw new IllegalStateException("El tiquete ya fue impreso");
        }
        this.impreso = true;
        this.fechaImpresion = LocalDateTime.now();
        return construirTiqueteParaImpresion();
    }

    /**
     * Calcula el valor total del tiquete como suma del precio base,
     * el cargo por servicio y el cargo de emisión.
     *
     * @return total a pagar por el tiquete.
     */
    public double calcularValorTotal() {
        return precio + cargoServicio + cargoEmision;
    }

    private DatosImpresion construirTiqueteParaImpresion() {
        String fechaEvento = String.format("%s %s", evento.getFecha(), evento.getHora());
        String fechaImpresionLegible = fechaImpresion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String localidadNombre = localidad != null ? localidad.getNombre() : "N/D";
        String qrData = generarContenidoQR(fechaEvento, fechaImpresionLegible, localidadNombre);
        return new DatosImpresion(
                idTiquete,
                evento.getNombre(),
                evento.getIdEvento(),
                fechaEvento,
                fechaImpresionLegible,
                localidadNombre,
                calcularValorTotal(),
                qrData
        );
    }

    private String generarContenidoQR(String fechaEvento, String fechaImpresionLegible, String localidadNombre) {
        return new StringBuilder()
                .append("Evento:").append(evento.getNombre()).append(" (").append(evento.getIdEvento()).append(")\n")
                .append("ID:").append(idTiquete).append('\n')
                .append("Localidad:").append(localidadNombre).append('\n')
                .append("F.Evento:").append(fechaEvento).append('\n')
                .append("F.Impresión:").append(fechaImpresionLegible)
                .toString();
    }
}