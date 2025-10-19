package manager;

import java.util.*;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import Cliente.Usuario;
import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import tiquetes.Tiquete;
import tiquetes.TiqueteBasico;

import tiquetes.PaqueteTiquetes;
import tiquetes.TiqueteMultiple;
import tiquetes.TiqueteTemporada;
import tiquetes.PaqueteDeluxe;

public class PersistenciaTiquetesJson {

	private static Map<String, Usuario> indexUsuarios(List<Usuario> usuarios) {
	    Map<String, Usuario> m = new HashMap<>();
	    if (usuarios != null) {
	        for (Usuario u : usuarios) {
	            if (u != null && u.getLogin() != null)
	                m.put(u.getLogin(), u);
	        }
	    }
	    return m;
	}

	private static Map<String, Evento> indexEventos(List<Evento> eventos) {
	    Map<String, Evento> m = new HashMap<>();
	    if (eventos != null) {
	        for (Evento e : eventos) {
	            if (e != null && e.getIdEvento() != null)
	                m.put(e.getIdEvento(), e);
	        }
	    }
	    return m;
	}
	
	public List<PaqueteTiquetes> cargarPaquetes(
	        String archivo,
	        List<Usuario> usuarios,
	        List<Evento> eventos,
	        Map<Integer, Tiquete> simplesPorId   
	) {
	    Map<String, Usuario> uIndex = indexUsuarios(usuarios);
	    Map<String, Evento>  eIndex = indexEventos(eventos);

	    List<PaqueteTiquetes> result = new ArrayList<>();
	    String raw = JsonFiles.read(java.nio.file.Paths.get(archivo));
	    if (raw == null || raw.isBlank()) return result;

	    JSONArray arr = new JSONArray(raw);

	    for (int i = 0; i < arr.length(); i++) {
	        JSONObject jt = arr.getJSONObject(i);
	        String tipo = jt.optString("tipo", "OTRO");

	   
	        if (!"MULTIPLE".equals(tipo) && !"TEMPORADA".equals(tipo) && !"DELUXE".equals(tipo)) {
	            continue;
	        }

	        
	        Usuario propietario = jt.isNull("propietarioLogin") ? null : uIndex.get(jt.getString("propietarioLogin"));
	        Evento  evento      = jt.isNull("eventoId")         ? null : eIndex.get(jt.getString("eventoId"));

	        PaqueteTiquetes p;

	        switch (tipo) {
	            case "MULTIPLE": {
	                
	                TiqueteMultiple tm = new TiqueteMultiple(
	                        jt.getInt("cantidadEntradas"),
	                        jt.getDouble("precioTotal")
	                );
	                p = tm;
	                break;
	            }

	            case "TEMPORADA": {
	                
	                TiqueteTemporada tp = new TiqueteTemporada(
	                        jt.getInt("cantidadEventos"),
	                        jt.getDouble("precioTotal")
	                );
	                p = tp;
	                break;
	            }

	            case "DELUXE": {
	                
	                List<String> beneficios = new ArrayList<>();
	                JSONArray jBenef = jt.optJSONArray("beneficios");
	                if (jBenef != null) {
	                    for (int k = 0; k < jBenef.length(); k++) {
	                        beneficios.add(jBenef.getString(k));
	                    }
	                }

	               
	                List<Tiquete> incluidos = new ArrayList<>();
	                JSONArray jIncluidos = jt.optJSONArray("tiquetesIncluidos");
	                if (jIncluidos != null && simplesPorId != null) {
	                    for (int k = 0; k < jIncluidos.length(); k++) {
	                        
	                        int idT = jIncluidos.getInt(k);
	                        Tiquete simple = simplesPorId.get(idT);
	                        if (simple != null) incluidos.add(simple);
	                    }
	                }
	               
	                PaqueteDeluxe pd = new PaqueteDeluxe(beneficios, incluidos);
	                p = pd;
	                break;
	            }

	            default:
	                continue;
	        }

	       
	        try {
	            if (propietario instanceof Cliente c) {
	                
	            }
	            if (evento != null) {
	                
	            }
	        } catch (Throwable ignored) {}

	        result.add(p);

	        
	        try {
	            if (propietario instanceof Cliente c && c.getTiquetes() != null) {
	                c.getTiquetes().addAll((Collection<? extends Tiquete>) p); 
	            }
	            if (evento != null && evento.getTiquetes() != null) {
	                evento.getTiquetes().addAll((Collection<? extends Tiquete>) p);
	            }
	        } catch (Throwable ignored) {}
	    }

	    return result;
	}
public void salvarPaquetes(String archivo, List<PaqueteTiquetes> paquetes) {
    JSONArray arr = new JSONArray();

    for (PaqueteTiquetes p : paquetes) {
        JSONObject jt = new JSONObject();

        
       
        if (p instanceof TiqueteMultiple tm) {
            jt.put("tipo", "MULTIPLE");
            jt.put("cantidadEntradas", tm.getCantidadEntradas()); 
            jt.put("precioTotal", tm.getPrecioTotal());

            

        } else if (p instanceof TiqueteTemporada tp) {
            jt.put("tipo", "TEMPORADA");
            jt.put("cantidadEventos", tp.getCantidadEventos());
            jt.put("precioTotal", tp.getPrecioTotal());

        } else if (p instanceof PaqueteDeluxe pd) {
            jt.put("tipo", "DELUXE");

            
            org.json.JSONArray jBenef = new org.json.JSONArray();
            try {
                for (String b : pd.getBeneficios()) jBenef.put(b);     
            } catch (Throwable ignored) {}
            jt.put("beneficios", jBenef);

            org.json.JSONArray jIncluidos = new org.json.JSONArray();
            try {
                
                for (Tiquete t : pd.getTiquetes()) {       
                    jIncluidos.put(t.getIdTiquete());
                }
            } catch (Throwable ignored) {}
            jt.put("tiquetesIncluidos", jIncluidos);
        } else {
            jt.put("tipo", "OTRO_PAQUETE");
        }

        arr.put(jt);
    }

    JsonFiles.write(java.nio.file.Paths.get(archivo), arr.toString(2)); }


 public List<Tiquete> cargarTiquetesSimples(String archivo,
                                            List<Usuario> usuarios,
                                            List<Evento> eventos) {
     Map<String, Usuario> uIndex = indexUsuarios(usuarios);
     Map<String, Evento>  eIndex = indexEventos(eventos);

     List<Tiquete> result = new ArrayList<>();
     String raw = JsonFiles.read(Paths.get(archivo));
     if (raw == null || raw.isBlank()) return result;

     JSONArray arr = new JSONArray(raw);

     for (int i = 0; i < arr.length(); i++) {
         JSONObject jt = arr.getJSONObject(i);
         String tipo = jt.optString("tipo", "OTRO");

         if (!"BASICO".equals(tipo) && !"OTRO".equals(tipo)) continue;

         Usuario propietario = jt.isNull("propietarioLogin") ? null : uIndex.get(jt.getString("propietarioLogin"));
         Evento  evento      = jt.isNull("eventoId") ? null : eIndex.get(jt.getString("eventoId"));

         Localidad localidad = null;
         if (evento != null && evento.getVenue() != null) {
             for (Localidad l : evento.getVenue().getLocalidades()) {
                 if (l.getNombre().equals(jt.optString("idLocalidad", ""))) {
                     localidad = l;
                     break;
                 }
             }
         }

         Tiquete t = null;

         if ("BASICO".equals(tipo)) {
             TiqueteBasico tb = new TiqueteBasico(
                 (Cliente) propietario,
                 jt.getInt("idTiquete"),
                 jt.getDouble("precio"),
                 jt.getDouble("cargoServicio"),
                 jt.getDouble("cargoEmision"),
                 jt.getString("estado"),
                 localidad,
                 evento,
                 jt.optInt("numeroAsiento", 0),
                 jt.optBoolean("localidadNumerada", localidad != null && localidad.isNumerada())
             );
             t = tb;
         }

         if (t != null) {
             result.add(t);
             if (propietario instanceof Cliente c && c.getTiquetes() != null) c.getTiquetes().add(t);
             if (evento != null && evento.getTiquetes() != null) evento.getTiquetes().add(t);
         }
     }

     return result;
 }

 public void salvarTiquetesSimples(String archivo, List<Tiquete> tiquetes) {
     JSONArray arr = new JSONArray();

     for (Tiquete t : tiquetes) {
         JSONObject jt = new JSONObject();
         jt.put("idTiquete", t.getIdTiquete());
         jt.put("precio", t.getPrecio());
         jt.put("cargoServicio", t.getCargoServicio());
         jt.put("cargoEmision", t.getCargoEmision());
         jt.put("estado", t.getEstado());
        
         jt.put("eventoId", (t.getEvento()==null) ? JSONObject.NULL : t.getEvento().getIdEvento());

         String tipoOut = (t instanceof TiqueteBasico) ? "BASICO" : "OTRO";
         jt.put("tipo", tipoOut);

         if (t instanceof TiqueteBasico tb) {
             jt.put("numeroAsiento", tb.getNumeroAsiento());
             jt.put("localidadNumerada", tb.isLocalidadNumerada());
             if (t.getEvento()!=null && t.getEvento().getVenue()!=null && t.getLocalidad()!=null) {
                 String locId = t.getEvento().getVenue().getIdVenue() + "::" + t.getLocalidad().getNombre();
                 jt.put("idLocalidad", locId);
             } else {
                 jt.put("idLocalidad", JSONObject.NULL);
             }
         }

         arr.put(jt);
     }

     JsonFiles.write(Paths.get(archivo), arr.toString(2));
 

}}

