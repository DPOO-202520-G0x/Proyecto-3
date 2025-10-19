package manager;

import java.util.List;
import Cliente.Usuario;
import eventos.Evento;
import tiquetes.Tiquete;

public interface IPersistenciaTiquetes {
  List<Tiquete> cargarTiquetes(String archivo,
                               List<Usuario> usuarios,
                               List<Evento> eventos);

  void salvarTiquetes(String archivo, List<Tiquete> tiquetes);
}
