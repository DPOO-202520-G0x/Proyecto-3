package manager;

import java.util.List;
import Cliente.Administrador;
import Cliente.Cliente;
import Cliente.Organizador;
/**
 * Contrato de persistencia para usuarios del sistema.
 * <p>
 * Maneja carga y guardado de administrador, clientes y organizadores.
 */
public interface IPersistenciaUsuarios {
	/**
	   * Carga usuarios desde un archivo, rellenando las colecciones provistas.
	   *
	   * @param archivo       ruta/identificador del archivo de origen.
	   * @param admin         instancia de administrador a inicializar/actualizar.
	   * @param clientes      lista a poblar con clientes cargados.
	   * @param organizadores lista a poblar con organizadores cargados.
	   * @throws RuntimeException si ocurre un error de lectura o de mapeo.
	   */
  void cargarUsuarios(String archivo,
                      Administrador admin,
                      List<Cliente> clientes,
                      List<Organizador> organizadores);
  /**
   * Persiste administrador, clientes y organizadores en el archivo indicado.
   *
   * @param archivo       ruta/identificador del archivo destino.
   * @param admin         administrador a guardar.
   * @param clientes      lista de clientes a guardar.
   * @param organizadores lista de organizadores a guardar.
   * @throws RuntimeException si ocurre un error de escritura o de serialización.
   */
  void salvarUsuarios(String archivo,
                      Administrador admin,
                      List<Cliente> clientes,
                      List<Organizador> organizadores);
}
