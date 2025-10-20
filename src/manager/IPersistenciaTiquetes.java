package manager;

import java.util.List;
import Cliente.Usuario;
import eventos.Evento;
import tiquetes.Tiquete;
/**
 * Contrato de persistencia para tiquetes.
 * <p>
 * Permite cargar y salvar tiquetes, reconstruyendo referencias a usuarios y eventos.
 */
public interface IPersistenciaTiquetes {
	/**
	   * Carga tiquetes desde un archivo y vincula sus referencias.
	   *
	   * @param archivo  ruta/identificador del archivo de origen.
	   * @param usuarios lista de usuarios existentes (para asociar propietario).
	   * @param eventos  lista de eventos existentes (para asociar evento/localidad).
	   * @return lista de tiquetes cargados.
	   * @throws RuntimeException si ocurre un error de lectura o de mapeo.
	   */
  List<Tiquete> cargarTiquetes(String archivo,
                               List<Usuario> usuarios,
                               List<Evento> eventos);
  /**
   * Persiste la lista de tiquetes en el archivo indicado.
   *
   * @param archivo  ruta/identificador del archivo destino.
   * @param tiquetes lista de tiquetes a guardar.
   * @throws RuntimeException si ocurre un error de escritura o de serialización.
   */
  void salvarTiquetes(String archivo, List<Tiquete> tiquetes);
}
