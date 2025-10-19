package manager;

public class CentralPersistencia {
  public static final String JSON  = "JSON";
  public static final String PLAIN = "PlainText"; 

  public IPersistenciaUsuarios getPersistenciaUsuarios(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return new PersistenciaUsuariosJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }

  public IPersistenciaEventos getPersistenciaEventos(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return new PersistenciaEventosJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }

  public IPersistenciaVenues getPersistenciaVenues(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return new PersistenciaVenuesJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }

  public IPersistenciaTiquetes getPersistenciaTiquetes(String tipo) {
    if (JSON.equalsIgnoreCase(tipo)) return (IPersistenciaTiquetes) new PersistenciaTiquetesJson();
    throw new IllegalArgumentException("Tipo no soportado: "+tipo);
  }
}
