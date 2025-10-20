package manager;

import Cliente.*;
import eventos.*;
import tiquetes.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.*;
import java.util.*;
/**
 * Demo de persistencia para BoletaMaster.
 * <p>
 * Crea datos de ejemplo (usuarios, venue/localidades, evento, oferta, tiquetes y paquetes)
 * y los persiste en archivos JSON dentro de la carpeta {@code /data}.
 * <p>
 * Propósito: servir como escenario de prueba/manual para validar lectura/escritura
 * y el ensamblaje básico del modelo (sin pasarela de pago ni reglas complejas).
 */
public class MainPersistenciaDemo {
	
	/**
	 * Punto de entrada del demo de persistencia.
	 * <p>
	 * Comportamiento:
	 * <ol>
	 *   <li>Garantiza existencia de la carpeta {@code data/}.</li>
	 *   <li>Instancia objetos de dominio (Administrador, Cliente, Organizador, Venue,
	 *       Localidades, Evento, Oferta, Tiquetes y Paquetes).</li>
	 *   <li>Serializa y guarda:
	 *       <ul>
	 *         <li>{@code data/usuarios.json}</li>
	 *         <li>{@code data/eventos.json}</li>
	 *         <li>{@code data/tiquetes.json}</li>
	 *         <li>{@code data/paquetes.json}</li>
	 *       </ul>
	 *   </li>
	 *   <li>Imprime en consola las rutas generadas.</li>
	 * </ol>
	 *
	 * @param args argumentos de línea de comandos (no utilizados).
	 */

    public static void main(String[] args) {
       
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();

        final String USUARIOS_JSON = "data/usuarios.json";
        final String EVENTOS_JSON  = "data/eventos.json";
        final String TIQUETES_JSON = "data/tiquetes.json";
        final String PAQUETES_JSON = "data/paquetes.json";

        
        Administrador admin = new Administrador(
                500_000.0, "ADM001", new ArrayList<>(),
                "ronny", "1234", "Juan Admin", 2_000_000.0
        );

        Cliente cliente = new Cliente(
                "cli01", "abc123", "Pedro Cliente", 300_000.0, "CLI001"
        );
        

        Organizador organizador = new Organizador(
                "org01", "pass", "Laura Organizadora", 1_000_000.0, "ORG001", new Evento[0]
        );

        List<Usuario> usuarios = Arrays.asList(admin, cliente, organizador);

        
        Localidad vip = new Localidad(null, new ArrayList<>(), "VIP", 150_000.0, true, 100);
        Localidad general = new Localidad(null, new ArrayList<>(), "GENERAL", 90_000.0, false, 500);

        Venue venue = new Venue("VEN001", "Teatro Los Alpes", "Bogotá",
                2000, new ArrayList<>(Arrays.asList(vip, general)));

       
        Evento evento = new Evento(
                admin, "EV001", "Concierto Los Alpes",
                LocalDate.of(2025, 12, 25),
                LocalTime.of(20, 0),
                "activo", TipoEvento.CONCIERTO,
                venue, null, organizador, new ArrayList<>()
        );

        Oferta oferta = new Oferta(
                vip, evento, 15.0,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );

        
        TiqueteBasico t1 = new TiqueteBasico(
                cliente, 101, 120_000.0, 5_000.0, 2_000.0,
                "emitido", vip, evento, 15, true
        );

        cliente.getTiquetes().add(t1);
        evento.getTiquetes().add(t1);

        // 6️⃣ Crear paquetes
        TiqueteMultiple multiple = new TiqueteMultiple(3, 250_000.0);
        TiqueteTemporada temporada = new TiqueteTemporada(5, 700_000.0);
        PaqueteDeluxe deluxe = new PaqueteDeluxe(
                Arrays.asList("Backstage", "Camiseta exclusiva"),
                Arrays.asList(t1)
        );

        List<Tiquete> tiquetes = Arrays.asList(t1);
        List<PaqueteTiquetes> paquetes = Arrays.asList(multiple, temporada, deluxe);
        List<Evento> eventos = Arrays.asList(evento);

       
        System.out.println("Guardando usuarios...");
        JSONObject jUsuarios = new JSONObject();

        JSONObject jAdmin = new JSONObject();
        jAdmin.put("login", admin.getLogin());
        jAdmin.put("nombre", admin.getNombre());
        jAdmin.put("idAdministrador", "ADM001");
        jAdmin.put("saldo", 2_000_000.0);

        JSONObject jCliente = new JSONObject();
        jCliente.put("login", cliente.getLogin());
        jCliente.put("nombre", cliente.getNombre());
        jCliente.put("idCliente", "CLI001");
        jCliente.put("saldo", 300_000.0);

        JSONObject jOrg = new JSONObject();
        jOrg.put("login", organizador.getLogin());
        jOrg.put("nombre", organizador.getNombre());
        jOrg.put("idOrganizador", "ORG001");
        jOrg.put("saldo", 1_000_000.0);

        jUsuarios.put("administrador", jAdmin);
        jUsuarios.put("clientes", new JSONArray(Arrays.asList(jCliente)));
        jUsuarios.put("organizadores", new JSONArray(Arrays.asList(jOrg)));

        JsonFiles.write(java.nio.file.Paths.get(USUARIOS_JSON), jUsuarios.toString(2));

        
        System.out.println("Guardando eventos...");
        JSONArray arrEventos = new JSONArray();

        JSONObject jEv = new JSONObject();
        jEv.put("idEvento", "EV001");
        jEv.put("nombre", "Concierto Los Alpes");
        jEv.put("fecha", "2025-12-25");
        jEv.put("hora", "20:00:00");
        jEv.put("estado", "activo");
        jEv.put("tipoEvento", "CONCIERTO");
        jEv.put("administradorLogin", "admin01");
        jEv.put("organizadorLogin", "org01");

        JSONObject jVenue = new JSONObject();
        jVenue.put("idVenue", "VEN001");
        jVenue.put("nombre", "Teatro Los Alpes");
        jVenue.put("ubicacion", "Bogotá");
        jVenue.put("capacidadMaxima", 2000);

        JSONArray jLocs = new JSONArray();
        jLocs.put(new JSONObject()
                .put("nombre", "VIP").put("precioBase", 150000)
                .put("numerada", true).put("numeroAsientos", 100));
        jLocs.put(new JSONObject()
                .put("nombre", "GENERAL").put("precioBase", 90000)
                .put("numerada", false).put("numeroAsientos", 500));
        jVenue.put("localidades", jLocs);
        jEv.put("venue", jVenue);

        JSONObject jOferta = new JSONObject();
        jOferta.put("porcentaje", 15.0);
        jOferta.put("inicio", oferta.getInicio().toString());
        jOferta.put("fin", oferta.getFin().toString());
        jEv.put("oferta", jOferta);

        JSONArray jTiqs = new JSONArray();
        jTiqs.put(101);
        jEv.put("tiquetes", jTiqs);

        arrEventos.put(jEv);
        JsonFiles.write(java.nio.file.Paths.get(EVENTOS_JSON), arrEventos.toString(2));

        
        System.out.println("Guardando tiquetes y paquetes...");
        PersistenciaTiquetesJson p = new PersistenciaTiquetesJson();
        p.salvarTiquetesSimples(TIQUETES_JSON, tiquetes);
        p.salvarPaquetes(PAQUETES_JSON, paquetes);

        System.out.println("\n✅ Archivos generados en /data:");
        System.out.println(" - " + USUARIOS_JSON);
        System.out.println(" - " + EVENTOS_JSON);
        System.out.println(" - " + TIQUETES_JSON);
        System.out.println(" - " + PAQUETES_JSON);

       
    }
}
