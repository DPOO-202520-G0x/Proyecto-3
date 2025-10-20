package tiquetes;
/**
 * Paquete de tiquetes que agrupa múltiples entradas con un precio total.
 * <p>
 * Extiende {@link tiquetes.PaqueteTiquetes} y se construye como transferible
 * del paquete completo (véase {@code super(true)}). Las políticas sobre
 * transferencia parcial o restricciones adicionales deben evaluarse en la
 * capa de aplicación según las reglas del dominio.
 */
public class TiqueteMultiple extends PaqueteTiquetes {
    private int cantidadEntradas;
    private double precioTotal;
    /**
     * Crea un paquete múltiple con cantidad de entradas y precio total.
     *
     * @param cantidadEntradas número de entradas del paquete (debe ser {@code > 0}).
     * @param precioTotal      precio total del paquete (debe ser {@code >= 0}).
     *
     * @throws IllegalArgumentException si {@code cantidadEntradas <= 0} o {@code precioTotal < 0}.
     */
    public TiqueteMultiple(int cantidadEntradas, double precioTotal) {
        super(true);
        if (cantidadEntradas <= 0) {
            throw new IllegalArgumentException("La cantidad de entradas debe ser positiva");
        }
        if (precioTotal < 0) {
            throw new IllegalArgumentException("El precio total debe ser positivo");
        }
        this.cantidadEntradas = cantidadEntradas;
        this.precioTotal = precioTotal;
    }

    public int getCantidadEntradas() {
        return cantidadEntradas;
    }

    public void setCantidadEntradas(int cantidadEntradas) {
        if (cantidadEntradas <= 0) {
            throw new IllegalArgumentException("La cantidad de entradas debe ser positiva");
        }
        this.cantidadEntradas = cantidadEntradas;
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
    /**
     * Calcula el precio unitario por entrada dentro del paquete múltiple.
     * <p>
     * Fórmula: {@code precioTotal / cantidadEntradas}.
     *
     * @return precio unitario de cada entrada.
     * @throws ArithmeticException si {@code cantidadEntradas} fuera 0 (no debería ocurrir por validaciones previas).
     */
    public double calcularPrecioUnitario() {
        return precioTotal / cantidadEntradas;
    }
}