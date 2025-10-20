package manager;

import java.util.List;
import Cliente.Administrador;
import Cliente.Organizador;
import eventos.Evento;
import eventos.Venue;
/**
 * Contrato de persistencia para eventos.
 * <p>
 * Permite cargar y salvar eventos desde/hacia un medio externo (p. ej., JSON).
 * La carga requiere contexto de administrador, organizadores y venues para
 * reconstruir referencias.
 */
public interface IPersistenciaEventos {
	
	/**
	   * Carga eventos desde un archivo y reconstruye sus referencias.
	   *
	   * @param archivo       ruta/identificador del archivo de origen.
	   * @param admin         administrador del sistema (contexto).
	   * @param organizadores lista de organizadores existentes (para vincular dueños).
	   * @param venues        lista de venues existentes (para asociar lugar del evento).
	   * @return lista de eventos cargados.
	   * @throws RuntimeException si ocurre un error de lectura o de mapeo.
	   */
  List<Evento> cargarEventos(String archivo,
                             Administrador admin,
                             List<Organizador> organizadores,
                             List<Venue> venues);
  /**
   * Persiste la lista de eventos en el archivo indicado.
   *
   * @param archivo ruta/identificador del archivo destino.
   * @param eventos lista de eventos a guardar.
   * @throws RuntimeException si ocurre un error de escritura o de serialización.
   */
  void salvarEventos(String archivo, List<Evento> eventos);
}
