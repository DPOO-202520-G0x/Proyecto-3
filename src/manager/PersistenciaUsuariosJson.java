package manager;

import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;




import Cliente.Administrador;
import Cliente.Cliente;
import Cliente.Organizador;

import eventos.Evento;
import eventos.TipoEvento;
import eventos.Venue;
import tiquetes.Tiquete;
/**
 * Implementación JSON de {@link manager.IPersistenciaUsuarios}.
 * <p>
 * Gestiona la carga/guardado de administrador, clientes y organizadores,
 * incluyendo cargos por tipo de evento y relaciones básicas.
 */
public class PersistenciaUsuariosJson implements IPersistenciaUsuarios {
	/**
	 * Carga usuarios desde un archivo JSON y puebla las colecciones provistas.
	 *
	 * @param archivo       ruta del archivo JSON.
	 * @param admin         instancia de administrador a inicializar/actualizar.
	 * @param clientes      lista destino para clientes cargados (se limpia y repuebla).
	 * @param organizadores lista destino para organizadores cargados (se limpia y repuebla).
	 * @throws RuntimeException si ocurre un error de lectura/mapeo JSON.
	 */
  @Override
  public void cargarUsuarios(String archivo,
                             Administrador admin,
                             List<Cliente> clientes,
                             List<Organizador> organizadores) {
	String raw = JsonFiles.read(Paths.get(archivo));
    JSONObject root = new JSONObject(raw);

    
    JSONObject a = root.getJSONObject("administrador");
    admin.setLogin(a.getString("login"));
    admin.setPassword(a.getString("password"));
    admin.setNombre(a.getString("nombre"));
    admin.setSaldo(a.getDouble("saldo"));
    admin.setIdAdministrador(a.getString("idAdministrador"));

    
    JSONObject cargos = a.getJSONObject("cargosServicio");
    for (String k : cargos.keySet()) {
      TipoEvento te = TipoEvento.valueOf(k);
      admin.fijarCargoServicio(te, cargos.getDouble(k));
    }
    admin.fijarCargoEmision(a.getDouble("cargoEmision"), null);

    
    clientes.clear();
    JSONArray cs = root.getJSONArray("clientes");
    for (int i=0;i<cs.length();i++) {
      JSONObject jc = cs.getJSONObject(i);
      Cliente c = new Cliente(jc.getString("idCliente"),jc.getString("login"),jc.getString("password"),jc.getDouble("saldo"),jc.getString("nombre"));
      
      clientes.add(c);
    }

 
 
    organizadores.clear();

    if (root.has("organizadores")) {
        JSONArray os = root.getJSONArray("organizadores");

        for (int i = 0; i < os.length(); i++) {
            JSONObject jo = os.getJSONObject(i);

           
            eventos.Evento[] eventosOrg = new eventos.Evento[0];

            Organizador org = new Organizador(
                    jo.getString("idOrganizador"),
                    jo.getString("login"),
                    jo.getString("password"),
                    jo.getDouble("saldo"),
                    jo.getString("nombre"),
                    eventosOrg
            );

            organizadores.add(org);
        
    }
    }  }
  /**
   * Serializa y guarda administrador, clientes y organizadores a un archivo JSON.
   *
   * @param archivo       ruta del archivo destino.
   * @param admin         administrador a persistir (incluye cargos y venues aprobados).
   * @param clientes      clientes a persistir (incluye ids de tiquetes).
   * @param organizadores organizadores a persistir (incluye ids de eventos y cortesías).
   * @throws RuntimeException si ocurre un error de escritura/serialización.
   */
  @Override
  public void salvarUsuarios(String archivo,
                             Administrador admin,
                             List<Cliente> clientes,
                             List<Organizador> organizadores) {

    JSONObject root = new JSONObject();

    
    JSONObject a = new JSONObject();
    a.put("login",    admin.getLogin());
    a.put("password", admin.getPassword());
    a.put("nombre",   admin.getNombre());
    a.put("saldo",    admin.getSaldo());
    a.put("idAdministrador", admin.getIdAdministrador());

    JSONObject cargos = new JSONObject();
    for (TipoEvento te : TipoEvento.values()) {
      cargos.put(te.name(), admin.getCargoServicio(te));
    }
    a.put("cargosServicio", cargos);
    a.put("cargoEmision", admin.getCargoEmision());

  
    JSONArray aprov = new JSONArray();
    for (Venue v : admin.getVenuesAprobados()) aprov.put(v.getIdVenue());
    a.put("venuesAprobados", aprov);

    root.put("administrador", a);

    
    JSONArray cs = new JSONArray();
    for (Cliente c : clientes) {
      JSONObject jc = new JSONObject();
      jc.put("login", c.getLogin());
      jc.put("password", c.getPassword());
      jc.put("nombre", c.getNombre());
      jc.put("saldo", c.getSaldo());
      jc.put("idCliente", c.getIdCliente());

      JSONArray tt = new JSONArray();
      for (Tiquete t : c.getTiquetes()) tt.put(t.getIdTiquete());
      jc.put("tiquetes", tt);
      cs.put(jc);
    }
    root.put("clientes", cs);

   
    JSONArray os = new JSONArray();
    for (Organizador o : organizadores) {
      JSONObject jo = new JSONObject();
      jo.put("login", o.getLogin());
      jo.put("password", o.getPassword());
      jo.put("nombre", o.getNombre());
      jo.put("saldo", o.getSaldo());
      jo.put("idOrganizador", o.getIdOrganizador());

      JSONArray evs = new JSONArray();
      for (Evento e : o.getEventos()) evs.put(e.getIdEvento());
      jo.put("eventos", evs);

      JSONArray cort = new JSONArray();
      for (Tiquete t : o.getCortesias()) cort.put(t.getIdTiquete());
      jo.put("cortesias", cort);

      os.put(jo);
    }
    root.put("organizadores", os);

    JsonFiles.write(Paths.get(archivo), root.toString(2));
  }
}
