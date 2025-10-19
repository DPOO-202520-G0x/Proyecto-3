package manager;

import java.util.*;
import org.json.*;

import eventos.Localidad;
import eventos.Venue;

public class PersistenciaVenuesJson implements IPersistenciaVenues {

  private static String locId(Venue v, Localidad l) {
    return v.getIdVenue() + "::" + l.getNombre();
  }

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

  @Override
  public void salvarVenues(String archivo, List<Venue> venues) {
    JSONArray arr = new JSONArray();
    for (Venue v : venues) {
      JSONObject o = new JSONObject();
      o.put("idVenue", v.getIdVenue());
      o.put("nombre", v.getNombre());
      o.put("ubicacion", v.getUbicacion());
      o.put("capacidadMaxima", v.getCapacidadMaxima());

      // ids de localidades
      JSONArray locIds = new JSONArray();
      for (Localidad l : v.getLocalidades()) locIds.put(locId(v,l));
      o.put("localidadesIds", locIds);

      arr.put(o);
    }
    JsonFiles.write(PathResolver.of(archivo), arr.toString(2));
  }

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
