package manager;

import java.util.*;
import org.json.*;

import eventos.Localidad;
import eventos.Venue;
/**
 * Implementación JSON de {@link manager.IPersistenciaVenues}.
 * <p>
 * Permite cargar venues y localidades desde archivos separados y guardarlos
 * nuevamente en JSON (venues y localidades por separado).
 */
public class PersistenciaVenuesJson implements IPersistenciaVenues {

  private static String locId(Venue v, Localidad l) {
    return v.getIdVenue() + "::" + l.getNombre();
  }

  /**
   * Carga venues y sus localidades desde dos archivos JSON.
   *
   * @param archVenues      archivo con la definición de venues.
   * @param archLocalidades archivo con la definición de localidades por venue.
   * @return lista de venues con sus localidades asociadas.
   * @throws RuntimeException si ocurre un error de lectura/mapeo JSON.
   */
  @Override
  public List<Venue> cargarVenuesYLocalidades(String archVenues, String archLocalidades) {
    Map<String, Venue> venues = new LinkedHashMap<>();

    // Venues
    JSONArray jv = new JSONArray(JsonFiles.read(PathResolver.of(archVenues)));
    for (int i=0;i<jv.length();i++) {
      JSONObject o = jv.getJSONObject(i);
      Venue v = new Venue(
          o.getString("idVenue"),
          o.getString("nombre"),
          o.getString("ubicacion"),
          o.getInt("capacidadMaxima"),
          new ArrayList<Localidad>()
      );
      venues.put(v.getIdVenue(), v);
    }

    // Localidades
    JSONArray jl = new JSONArray(JsonFiles.read(PathResolver.of(archLocalidades)));
    for (int i=0;i<jl.length();i++) {
      JSONObject o = jl.getJSONObject(i);
      Venue v = venues.get(o.getString("venueId"));
      Localidad l = new Localidad(
          null, new ArrayList<>(),
          o.getString("nombre"),
          o.getDouble("precioBase"),
          o.getBoolean("numerada"),
          o.getInt("numeroAsientos")
      );
      v.agregarLocalidad(l);
    }
    return new ArrayList<>(venues.values());
  }
  /**
   * Serializa y guarda venues en un archivo JSON.
   *
   * @param archivo ruta del archivo destino.
   * @param venues  venues a persistir (incluye ids de localidades como referencias).
   * @throws RuntimeException si ocurre un error de escritura/serialización.
   */
  @Override
  public void salvarVenues(String archivo, List<Venue> venues) {
    JSONArray arr = new JSONArray();
    for (Venue v : venues) {
      JSONObject o = new JSONObject();
      o.put("idVenue", v.getIdVenue());
      o.put("nombre", v.getNombre());
      o.put("ubicacion", v.getUbicacion());
      o.put("capacidadMaxima", v.getCapacidadMaxima());

      
      JSONArray locIds = new JSONArray();
      for (Localidad l : v.getLocalidades()) locIds.put(locId(v,l));
      o.put("localidadesIds", locIds);

      arr.put(o);
    }
    JsonFiles.write(PathResolver.of(archivo), arr.toString(2));
  }
  /**
   * Serializa y guarda las localidades de los venues en un archivo JSON.
   *
   * @param archivo ruta del archivo destino.
   * @param venues  lista de venues de donde se extraen sus localidades.
   * @throws RuntimeException si ocurre un error de escritura/serialización.
   */
  @Override
  public void salvarLocalidades(String archivo, List<Venue> venues) {
    JSONArray arr = new JSONArray();
    for (Venue v : venues) {
      for (Localidad l : v.getLocalidades()) {
        JSONObject o = new JSONObject();
        o.put("id",       locId(v,l));
        o.put("venueId",  v.getIdVenue());
        o.put("nombre",   l.getNombre());
        o.put("precioBase", l.getPrecioBase());
        o.put("numerada", l.isNumerada());
        o.put("numeroAsientos", l.getNumeroAsientos());
        arr.put(o);
      }
    }
    JsonFiles.write(PathResolver.of(archivo), arr.toString(2));
  }
}
