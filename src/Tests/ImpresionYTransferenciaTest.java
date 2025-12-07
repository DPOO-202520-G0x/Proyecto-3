package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Cliente.Administrador;
import Cliente.Cliente;
import eventos.Evento;
import eventos.Localidad;
import eventos.TipoEvento;
import manager.BoletaMasterState;
import manager.MarketplaceService;
import tiquetes.TiqueteBasico;

class ImpresionYTransferenciaTest {

    private Evento nuevoEventoDummy() {
        Administrador admin = new Administrador(0.0, "ADMIN-TEST", new ArrayList<>(), "admin_test", "pass", "Administrador de prueba", 0.0);
        return new Evento(
                admin,
                "E-TEST",
                "Evento de prueba",
                LocalDate.now().plusDays(5),
                LocalTime.NOON,
                "Programado",
                TipoEvento.CONCIERTO,
                null,
                null,
                null,
                new ArrayList<>()
        );
    }

    private Localidad nuevaLocalidadDummy() {
        return new Localidad(null, new ArrayList<>(), "General", 50_000.0, false, 0);
    }

    @org.junit.jupiter.api.Test
    void imprimirMarcaUnaSolaVez() {
        Cliente cliente = new Cliente("cli_demo", "1234", "Cliente Demo", 0.0, "CLI-DEMO");
        var tiquete = new TiqueteBasico(cliente, 1001, 50_000.0, 0.0, 0.0,
                "Emitido", nuevaLocalidadDummy(), nuevoEventoDummy(), null, false);

        String impresion = tiquete.imprimir();

        assertTrue(tiquete.estaImpreso(), "Debe marcarse como impreso");
        assertNotNull(tiquete.getFechaImpresion(), "Debe guardar la fecha de primera impresión");
        assertTrue(impresion.contains("QR-DATA"), "La representación debe contener el bloque de QR");
        LocalDateTime primeraFecha = tiquete.getFechaImpresion();

        IllegalStateException error = assertThrows(IllegalStateException.class, tiquete::imprimir);
        assertEquals("El tiquete ya fue impreso", error.getMessage());
        assertEquals(primeraFecha, tiquete.getFechaImpresion(), "La fecha no debe cambiar en reimpresión");
    }

    @org.junit.jupiter.api.Test
    void impresionBloqueaTransferenciaYReventa() {
        Cliente vendedor = new Cliente("cli_v", "1234", "Vendedor", 0.0, "CLI-V");
        Cliente comprador = new Cliente("cli_c", "1234", "Comprador", 0.0, "CLI-C");
        var tiquete = new TiqueteBasico(vendedor, 2002, 80_000.0, 0.0, 0.0,
                "Emitido", nuevaLocalidadDummy(), nuevoEventoDummy(), null, false);

        // Marca como impreso
        tiquete.imprimir();

        // Transferencia debe fallar porque el tiquete está impreso
        assertFalse(vendedor.transferirTiquete(comprador, 2002, "1234"),
                "No debe transferir un tiquete impreso");

        // Configura un marketplace mínimo con el mismo tiquete
        BoletaMasterState state = new BoletaMasterState();
        state.getClientesPorLogin().put(vendedor.getLogin(), vendedor);
        state.getTiquetesPorId().put(tiquete.getIdTiquete(), tiquete);
        MarketplaceService marketplace = new MarketplaceService(state);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () ->
                marketplace.publicarOferta(vendedor, List.of(tiquete.getIdTiquete()), 10_000.0)
        );
        assertTrue(error.getMessage().contains("impreso"),
                "La publicación debe rechazar tiquetes impresos");
    }
}
