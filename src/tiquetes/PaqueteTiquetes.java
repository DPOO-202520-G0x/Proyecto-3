package tiquetes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class PaqueteTiquetes {
    private final List<Tiquete> tiquetes;
    private boolean transferible;

    protected PaqueteTiquetes(boolean transferible) {
        this.transferible = transferible;
        this.tiquetes = new ArrayList<>();
    }

    public List<Tiquete> getTiquetes() {
        return Collections.unmodifiableList(tiquetes);
    }

    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(Objects.requireNonNull(tiquete, "El tiquete es obligatorio"));
    }

    protected void limpiarTiquetes() {
        tiquetes.clear();
    }

    public boolean esTransferible() {
        return transferible;
    }

    protected void setTransferible(boolean transferible) {
        this.transferible = transferible;
    }
}