package manager;

/**
 * Fábrica central de componentes de persistencia para BoletaMaster.
 * <p>
 * Según el tipo solicitado (por ahora: {@value #JSON}), retorna implementaciones
 * concretas de persistencia para usuarios, eventos, venues y tiquetes.
 * Si el tipo no está soportado, lanza {@link IllegalArgumentException}.
 */
public class CentralPersistencia {
  public static final String JSON  = "JSON";
  public static final String PLAIN = "PlainText"; 

  /**
   * Retorna la persistencia de usuarios según el tipo solicitado.
   *
   * @param tipo identificador de formato/tecnología (p. ej., {@value #JSON}).
   * @return implementación de {@code IPersistenciaUsuarios}.
   * @throws IllegalArgumentException si el tipo no está soportado.
   */
  public IPersistenciaUsuarios getPersistenciaUsuarios(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return new PersistenciaUsuariosJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }
  /**
   * Retorna la persistencia de eventos según el tipo solicitado.
   *
   * @param tipo identificador de formato/tecnología (p. ej., {@value #JSON}).
   * @return implementación de {@code IPersistenciaEventos}.
   * @throws IllegalArgumentException si el tipo no está soportado.
   */
  public IPersistenciaEventos getPersistenciaEventos(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return new PersistenciaEventosJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }
  /**
   * Retorna la persistencia de venues según el tipo solicitado.
   *
   * @param tipo identificador de formato/tecnología (p. ej., {@value #JSON}).
   * @return implementación de {@code IPersistenciaVenues}.
   * @throws IllegalArgumentException si el tipo no está soportado.
   */
  public IPersistenciaVenues getPersistenciaVenues(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return new PersistenciaVenuesJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }
  /**
   * Retorna la persistencia de tiquetes según el tipo solicitado.
   *
   * @param tipo identificador de formato/tecnología (p. ej., {@value #JSON}).
   * @return implementación de {@code IPersistenciaTiquetes}.
   * @throws IllegalArgumentException si el tipo no está soportado.
   */
  public IPersistenciaTiquetes getPersistenciaTiquetes(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return (IPersistenciaTiquetes) new PersistenciaTiquetesJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }
}
