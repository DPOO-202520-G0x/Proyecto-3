package tiquetes;

public class TiqueteMultiple extends PaqueteTiquetes {
    private int cantidadEntradas;
    private double precioTotal;

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

    public double calcularPrecioUnitario() {
        return precioTotal / cantidadEntradas;
    }
}