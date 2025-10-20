package manager;

import java.util.*;
import org.json.*;

import Cliente.Administrador;
import Cliente.Organizador;
import eventos.*;

import java.time.LocalDate;
import java.time.LocalTime;
/**
 * Implementación JSON de {@link manager.IPersistenciaEventos}.
 * <p>
 * Carga y guarda eventos en archivos JSON, reconstruyendo referencias a {@code Venue}
 * y asociando un organizador por defecto cuando aplique.
 */
public class PersistenciaEventosJson implements IPersistenciaEventos {

	/**
	 * Carga eventos desde un archivo JSON y reconstruye sus referencias.
	 *
	 * @param archivo       ruta del archivo JSON con los eventos.
	 * @param admin         administrador del sistema (contexto requerido por el dominio).
	 * @param organizadores lista de organizadores existentes para vincular (se usa el primero como default si aplica).
	 * @param venues        lista de venues existentes para asociar por id.
	 * @return lista de eventos cargados.
	 * @throws RuntimeException si ocurre un error de lectura/mapeo JSON.
	 */
  @Override
  public List<Evento> cargarEventos(String archivo,
                                    Administrador admin,
                                    List<Organizador> organizadores,
                                    List<Venue> venues) {
    Map<String, Venue> vIndex = new HashMap<>();
    for (Venue v : venues) vIndex.put(v.getIdVenue(), v);

    
    Organizador defaultOrg = (organizadores.isEmpty()? null : organizadores.get(0));

    List<Evento> eventos = new ArrayList<>();
    JSONArray arr = new JSONArray(JsonFiles.read(PathResolver.of(archivo)));
    for (int i=0;i<arr.length();i++) {
      JSONObject o = arr.getJSONObject(i);

      Venue v = o.isNull("venueId") ? null : vIndex.get(o.getString("venueId"));

      Evento e = new Evento(
          admin,
          o.getString("idEvento"),
          o.getString("nombre"),
          LocalDate.parse(o.getString("fecha")),
          LocalTime.parse(o.getString("hora")),
          o.getString("estado"),
          TipoEvento.valueOf(o.getString("tipoEvento")),
          v,                   
          null,                
          defaultOrg,
          new ArrayList<tiquetes.Tiquete>()
      );

      if (v != null) v.registrarEvento(e);
      if (defaultOrg != null) defaultOrg.registrarEvento(e);

      eventos.add(e);
    }
    return eventos;
  }
  /**
   * Persiste la lista de eventos a un archivo JSON.
   *
   * @param archivo ruta del archivo destino.
   * @param eventos eventos a serializar.
   * @throws RuntimeException si ocurre un error de escritura/serialización.
   */
  @Override
  public void salvarEventos(String archivo, List<Evento> eventos) {
    JSONArray arr = new JSONArray();
    for (Evento e : eventos) {
      JSONObject je = new JSONObject();
      je.put("idEvento", e.getIdEvento());
      je.put("nombre", e.getNombre());
      je.put("fecha", e.getFecha().toString());
      je.put("hora", e.getHora().toString());
      je.put("estado", e.getEstado());
      je.put("tipoEvento", e.getTipoEvento().name());
      je.put("venueId", e.getVenue() == null ? JSONObject.NULL : e.getVenue().getIdVenue());

      
      arr.put(je);
    }
    JsonFiles.write(PathResolver.of(archivo), arr.toString(2));
  }
}
