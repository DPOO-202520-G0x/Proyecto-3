package manager;

import java.util.List;
import eventos.Venue;

public interface IPersistenciaVenues {
  List<Venue> cargarVenuesYLocalidades(String archVenues, String archLocalidades);
  void salvarVenues(String archivo, List<Venue> venues);
  void salvarLocalidades(String archivo, List<Venue> venues);
}
