package manager;

import java.util.List;
import eventos.Venue;
/**
 * Contrato de persistencia para venues y sus localidades.
 * <p>
 * Permite cargar venues (junto con sus localidades) y persistirlos
 * en archivos separados si es necesario.
 */
public interface IPersistenciaVenues {
	/**
	   * Carga venues y sus localidades desde dos archivos.
	   *
	   * @param archVenues      archivo con la definición de venues.
	   * @param archLocalidades archivo con la definición de localidades por venue.
	   * @return lista de venues cargados con sus localidades asociadas.
	   * @throws RuntimeException si ocurre un error de lectura o de mapeo.
	   */
  List<Venue> cargarVenuesYLocalidades(String archVenues, String archLocalidades);
  /**
   * Persiste la lista de venues en el archivo indicado.
   *
   * @param archivo ruta/identificador del archivo destino.
   * @param venues  lista de venues a guardar.
   * @throws RuntimeException si ocurre un error de escritura o de serialización.
   */
  void salvarVenues(String archivo, List<Venue> venues);
  /**
   * Persiste las localidades de los venues en el archivo indicado.
   *
   * @param archivo ruta/identificador del archivo destino.
   * @param venues  lista de venues cuyas localidades se guardarán.
   * @throws RuntimeException si ocurre un error de escritura o de serialización.
   */
  void salvarLocalidades(String archivo, List<Venue> venues);
}
