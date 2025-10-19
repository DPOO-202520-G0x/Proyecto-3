package manager;

import java.util.*;
import org.json.*;

import Cliente.Administrador;
import Cliente.Organizador;
import eventos.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class PersistenciaEventosJson implements IPersistenciaEventos {

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
