package manager;

import java.util.List;
import Cliente.Administrador;
import Cliente.Organizador;
import eventos.Evento;
import eventos.Venue;

public interface IPersistenciaEventos {
  List<Evento> cargarEventos(String archivo,
                             Administrador admin,
                             List<Organizador> organizadores,
                             List<Venue> venues);

  void salvarEventos(String archivo, List<Evento> eventos);
}
