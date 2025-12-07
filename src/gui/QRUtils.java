package gui;

import util.qrcodegen.QrCode;
import util.qrcodegen.QrSegment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public final class QRUtils {
    private QRUtils() {}

    public static BufferedImage generarQR(String contenido, int escala, int borde) {
        QrCode qr = QrCode.encodeSegments(List.of(QrSegment.makeBytes(contenido.getBytes())), QrCode.Ecc.MEDIUM);
        int tamaño = (qr.size + borde * 2) * escala;
        BufferedImage imagen = new BufferedImage(tamaño, tamaño, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, tamaño, tamaño);
        g.setColor(Color.BLACK);
        for (int y = 0; y < qr.size; y++) {
            for (int x = 0; x < qr.size; x++) {
                if (qr.getModule(x, y)) {
                    g.fillRect((x + borde) * escala, (y + borde) * escala, escala, escala);
                }
            }
        }
        g.dispose();
        return imagen;
    }

}
