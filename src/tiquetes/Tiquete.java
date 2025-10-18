package tiquetes;

import java.util.Objects;

import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;

public abstract class Tiquete {
    private int idTiquete;
    private double precio;
    private double cargoServicio;
    private double cargoEmision;
    private String estado;
    private Localidad localidad;
    private Evento evento;
    private Cliente cliente;

    protected Tiquete(Cliente cliente, int idTiquete, double precio, double cargoServicio, double cargoEmision,
            String estado, Localidad localidad, Evento evento) {
        this.cliente = cliente;
        this.idTiquete = idTiquete;
        this.precio = precio;
        this.cargoServicio = cargoServicio;
        this.cargoEmision = cargoEmision;
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.localidad = Objects.requireNonNull(localidad, "La localidad es obligatoria");
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

    public double calcularValorTotal() {
        return precio + cargoServicio + cargoEmision;
    }
}