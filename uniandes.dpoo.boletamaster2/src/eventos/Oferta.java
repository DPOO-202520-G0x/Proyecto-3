package eventos;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Oferta {

    private final Localidad localidad;
    private final Evento evento;
    private final double porcentaje;          // 0..100 (ej. 10 = 10%)
    private final LocalDateTime inicio;       
    private final LocalDateTime fin;          

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

    public boolean activa(LocalDateTime now) {
        return (now.equals(inicio) || now.isAfter(inicio))
            && (now.equals(fin)    || now.isBefore(fin));
    }

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
