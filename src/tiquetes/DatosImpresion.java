package tiquetes;

/**
 * Contenedor inmutable con los datos necesarios para mostrar/imprimir un tiquete
 * y generar su código QR.
 */
public class DatosImpresion {
    public final int idTiquete;
    public final String nombreEvento;
    public final String idEvento;
    public final String fechaEvento;
    public final String fechaImpresion;
    public final String localidad;
    public final double precioTotal;
    public final String payloadQR;

    public DatosImpresion(int idTiquete, String nombreEvento, String idEvento, String fechaEvento,
                          String fechaImpresion, String localidad, double precioTotal, String payloadQR) {
        this.idTiquete = idTiquete;
        this.nombreEvento = nombreEvento;
        this.idEvento = idEvento;
        this.fechaEvento = fechaEvento;
        this.fechaImpresion = fechaImpresion;
        this.localidad = localidad;
        this.precioTotal = precioTotal;
        this.payloadQR = payloadQR;
    }

    public String comoTexto() {
        return new StringBuilder()
                .append("=== BOLETA MASTER ===\n")
                .append("Evento: ").append(nombreEvento).append(" (").append(idEvento).append(")\n")
                .append("Localidad: ").append(localidad).append("\n")
                .append("ID Tiquete: ").append(idTiquete).append("\n")
                .append("Fecha evento: ").append(fechaEvento).append("\n")
                .append("Fecha impresión: ").append(fechaImpresion).append("\n")
                .append("Precio total: $").append(String.format("%.2f", precioTotal)).append("\n")
                .append("QR-DATA: ").append(payloadQR).append("\n")
                .toString();
    }
}
