package Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tiquetes.Tiquete;

public class Cliente extends Usuario {
    private final String idCliente;
    private final List<Tiquete> tiquetes;

    public Cliente(String login, String password, String nombre, double saldo, String idCliente) {
        super(login, password, nombre, saldo);
        this.idCliente = Objects.requireNonNull(idCliente, "El identificador del cliente es obligatorio");
        this.tiquetes = new ArrayList<>();
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        throw new UnsupportedOperationException("El identificador del cliente es inmutable");
    }

    public ArrayList<Tiquete> getTiquetes() {
        return new ArrayList<>(tiquetes);
    }

    public void setTiquetes(ArrayList<Tiquete> tiquetesNuevos) {
        this.tiquetes.clear();
        if (tiquetesNuevos != null) {
            this.tiquetes.addAll(tiquetesNuevos);
        }
    }

    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }

    public boolean eliminarTiquete(Tiquete tiquete) {
        return tiquetes.remove(tiquete);
    }
}