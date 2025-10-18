package eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tiquetes.Tiquete;

public class Localidad {
    private String nombre;
    private double precioBase;
    private boolean numerada;
    private int numeroAsientos;
    private final List<Tiquete> tiquetes;
    private Oferta oferta;

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

    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }

    public void fijarPrecio(double precio) {
        setPrecioBase(precio);
    }
}