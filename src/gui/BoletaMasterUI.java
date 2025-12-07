package gui;

import Cliente.Administrador;
import Cliente.Cliente;
import Cliente.Organizador;
import eventos.Evento;
import manager.BoletaMasterSystem;
import marketPlace.ContraOferta;
import marketPlace.EstadoContraOferta;
import marketPlace.OfertaMarketPlace;
import tiquetes.DatosImpresion;
import tiquetes.Tiquete;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Entrada principal de la interfaz gráfica Swing para BoletaMaster.
 */
public class BoletaMasterUI extends JPanel {

    private final BoletaMasterSystem sistema;
    private final CardLayout cards;
    private final JPanel cardPanel;

    public BoletaMasterUI(BoletaMasterSystem sistema) {
        super(new BorderLayout());
        this.sistema = Objects.requireNonNull(sistema, "sistema");
        this.cards = new CardLayout();
        this.cardPanel = new JPanel(cards);
        construirUI();
    }

    private void construirUI() {
        LoginPanel loginPanel = new LoginPanel(new java.util.ArrayList<>(sistema.getTodosLosEventos()));
        JPanel placeholder = new JPanel();

        cardPanel.add(loginPanel, "login");
        cardPanel.add(placeholder, "home");

        loginPanel.setOnLogin((rol, login, password) -> manejarLogin(rol, login, password));

        add(cardPanel, BorderLayout.CENTER);
    }

    private void manejarLogin(RolSeleccionado rol, String login, String password) {
        switch (rol) {
            case CLIENTE -> sistema.autenticarCliente(login, password).ifPresentOrElse(
                    cliente -> mostrarPanelCliente(cliente),
                    () -> mostrarError("Credenciales de cliente inválidas"));
            case ADMIN -> sistema.autenticarAdministrador(login, password).ifPresentOrElse(
                    admin -> mostrarPanelAdministrador(admin),
                    () -> mostrarError("Credenciales de administrador inválidas"));
            case ORGANIZADOR -> sistema.autenticarOrganizador(login, password).ifPresentOrElse(
                    organizador -> mostrarPanelOrganizador(organizador),
                    () -> mostrarError("Credenciales de organizador inválidas"));
            default -> mostrarError("Rol no soportado");
        }
    }

    private void mostrarPanelCliente(Cliente cliente) {
        ClientePanel panel = new ClientePanel(sistema, cliente, this::volverAlLogin);
        cardPanel.add(panel, "cliente");
        cards.show(cardPanel, "cliente");
    }

    private void mostrarPanelAdministrador(Administrador admin) {
        AdministradorPanel panel = new AdministradorPanel(sistema, admin, this::volverAlLogin);
        cardPanel.add(panel, "admin");
        cards.show(cardPanel, "admin");
    }

    private void mostrarPanelOrganizador(Organizador organizador) {
        OrganizadorPanel panel = new OrganizadorPanel(sistema, organizador, this::volverAlLogin);
        cardPanel.add(panel, "organizador");
        cards.show(cardPanel, "organizador");
    }

    private void volverAlLogin() {
        cards.show(cardPanel, "login");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Path dataDir = Paths.get("data");
            BoletaMasterSystem sistema = BoletaMasterSystem.desdeDirectorio(dataDir.toString());
            sistema.cargarDatos();

            javax.swing.JFrame frame = new javax.swing.JFrame("BoletaMaster - GUI");
            frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            BoletaMasterUI ui = new BoletaMasterUI(sistema);
            frame.setContentPane(ui);
            frame.setSize(new Dimension(1100, 700));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    sistema.guardarDatos();
                }
            });
        });
    }
}

class TicketPreviewDialog {
    public static void mostrar(JPanel parent, DatosImpresion datos) {
        javax.swing.JDialog dialog = new javax.swing.JDialog(
                SwingUtilities.getWindowAncestor(parent),
                "Imprimir BoletaMaster",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setMinimumSize(new Dimension(1000, 620));

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBorder(new EmptyBorder(10, 10, 10, 10));

        PosterPanel poster = new PosterPanel(datos);
        poster.setPreferredSize(new Dimension(450, 480));
        cuerpo.add(poster, BorderLayout.WEST);

        JPanel detalle = new JPanel(new GridBagLayout());
        detalle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(255, 215, 0)),
                new EmptyBorder(12, 12, 12, 12)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        detalle.add(new JLabel("Evento:"), gbc);
        gbc.gridx = 1;
        detalle.add(new JLabel(datos.nombreEvento + " - ID " + datos.idEvento), gbc);

        gbc.gridx = 0; gbc.gridy++;
        detalle.add(new JLabel("ID Tiquete:"), gbc);
        gbc.gridx = 1;
        detalle.add(new JLabel(String.valueOf(datos.idTiquete)), gbc);

        gbc.gridx = 0; gbc.gridy++;
        detalle.add(new JLabel("Localidad:"), gbc);
        gbc.gridx = 1;
        detalle.add(new JLabel(datos.localidad), gbc);

        gbc.gridx = 0; gbc.gridy++;
        detalle.add(new JLabel("Fecha del evento:"), gbc);
        gbc.gridx = 1;
        detalle.add(new JLabel(datos.fechaEvento), gbc);

        gbc.gridx = 0; gbc.gridy++;
        detalle.add(new JLabel("Fecha de impresión:"), gbc);
        gbc.gridx = 1;
        detalle.add(new JLabel(datos.fechaImpresion), gbc);

        gbc.gridx = 0; gbc.gridy++;
        detalle.add(new JLabel("Precio total:"), gbc);
        gbc.gridx = 1;
        detalle.add(new JLabel(String.format("$%.2f", datos.precioTotal)), gbc);

        BufferedImage qrImg = QRUtils.generarQR(datos.payloadQR, 6, 2);
        JLabel qrLabel = new JLabel(new ImageIcon(qrImg));
        qrLabel.setBorder(BorderFactory.createTitledBorder("QR listo para escanear"));
        qrLabel.setPreferredSize(new Dimension(qrImg.getWidth() + 40, qrImg.getHeight() + 40));

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.add(detalle, BorderLayout.CENTER);
        derecha.add(qrLabel, BorderLayout.EAST);

        cuerpo.add(derecha, BorderLayout.CENTER);
        dialog.add(cuerpo, BorderLayout.CENTER);

        JTextArea textoPlano = new JTextArea(datos.comoTexto());
        textoPlano.setEditable(false);
        dialog.add(new JScrollPane(textoPlano), BorderLayout.SOUTH);

        JButton cerrar = new JButton("Cerrar vista previa");
        cerrar.addActionListener(e -> dialog.dispose());
        dialog.add(cerrar, BorderLayout.NORTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}

class BannerPanel extends JPanel {

    private final List<Evento> eventos;
    private final List<String> artistas;
    private final AvatarPreviewPanel preview;
    private final RotatingLogo logo;
    private final TileCarousel carousel;
    private final Map<String, ImageIcon> avatares;

    BannerPanel(List<Evento> eventosDisponibles) {
        this.eventos = (eventosDisponibles == null || eventosDisponibles.isEmpty())
                ? List.of()
                : eventosDisponibles;
        this.artistas = List.of("Shakira", "Karol G", "Juanes", "Fonseca", "Morat", "Carlos Vives");
        setOpaque(false);
        setPreferredSize(new Dimension(680, 540));
        setLayout(new BorderLayout(12, 8));

        preview = new AvatarPreviewPanel();
        logo = new RotatingLogo();
        carousel = new TileCarousel(extraerNombresEventos(), artistas);
        avatares = crearAvatares();

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 18, 10, 18));

        JLabel titulo = new JLabel("BOLETAMASTER · Tu boletería oficial para conciertos, festivales y teatros", JLabel.LEFT);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 26f));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 10, 10, 10));

        JPanel headerBadge = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                var g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 214, 102, 210), getWidth(), getHeight(), new Color(255, 129, 91, 210));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
            }
        };
        headerBadge.setOpaque(false);
        headerBadge.setBorder(new EmptyBorder(12, 14, 12, 14));
        headerBadge.add(titulo, BorderLayout.CENTER);

        header.add(headerBadge, BorderLayout.CENTER);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(12, 18, 12, 18));

        JPanel pistaCarrusel = new JPanel(new BorderLayout());
        pistaCarrusel.setOpaque(false);
        pistaCarrusel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 90), 1, true),
                new EmptyBorder(14, 14, 14, 14)));
        carousel.setPreferredSize(new Dimension(560, 280));
        carousel.setHoverListener(this::actualizarPreviewDesdeCarousel);
        pistaCarrusel.add(carousel, BorderLayout.CENTER);

        contenido.add(header, BorderLayout.NORTH);
        contenido.add(pistaCarrusel, BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);
        add(preview, BorderLayout.EAST);
        add(logo, BorderLayout.SOUTH);
    }

    private List<String> extraerNombresEventos() {
        if (eventos.isEmpty()) {
            return List.of("Concierto Andes", "Festival Pacífico", "Teatro Central", "Derby Andino");
        }
        return eventos.stream()
                .map(Evento::getNombre)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    private ImageIcon crearAvatar(String nombre, Color base) {
        int size = 96;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        var g2 = (java.awt.Graphics2D) img.getGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, base, size, size, base.brighter());
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, size, size, 20, 20);
        g2.setColor(new Color(255, 255, 255, 210));
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 28f));
        String iniciales = nombre.length() <= 2 ? nombre.toUpperCase() : nombre.substring(0, 2).toUpperCase();
        FontMetrics fm = g2.getFontMetrics();
        int tx = (size - fm.stringWidth(iniciales)) / 2;
        int ty = (size + fm.getAscent()) / 2 - 6;
        g2.drawString(iniciales, tx, ty);
        g2.setColor(new Color(0, 0, 0, 60));
        g2.drawRoundRect(2, 2, size - 4, size - 4, 18, 18);
        g2.dispose();
        return new ImageIcon(img);
    }

    private Map<String, ImageIcon> crearAvatares() {
        java.util.Map<String, ImageIcon> mapa = new java.util.HashMap<>();
        List<String> nombresEventos = extraerNombresEventos();
        Color baseEventos = new Color(245, 199, 83);
        Color baseArtistas = new Color(111, 203, 255);
        nombresEventos.forEach(ev -> mapa.put(ev, crearAvatar(ev, baseEventos.darker())));
        artistas.forEach(ar -> mapa.put(ar, crearAvatar(ar, baseArtistas.darker())));
        return mapa;
    }

    private void actualizarPreviewDesdeCarousel(String item) {
        if (item == null) {
            preview.restaurarMensaje();
        } else {
            Evento evento = buscarEvento(item);
            if (evento != null) {
                String capacidad = evento.getCapacidadMaxima() > 0
                        ? Integer.toString(evento.getCapacidadMaxima())
                        : "cap. pendiente";

                preview.actualizar(item, avatares.get(item),
                        String.format("Vendidos: %d / %s (%.0f%%)", evento.getVendidos(),
                                capacidad, evento.getPorcentajeVenta()),
                        String.format("Recaudo estimado: $%,.0f", calcularRecaudo(evento)),
                        String.format("Fecha: %s · %s", evento.getFecha(), evento.getHora()));
            } else {
                preview.actualizar(item, avatares.get(item),
                        "Artista destacado en BoletaMaster",
                        "Explora próximos conciertos y festivales",
                        "Pasa el cursor para descubrir más");
            }
        }
    }

    private Evento buscarEvento(String nombre) {
        if (nombre == null) {
            return null;
        }
        return eventos.stream()
                .filter(e -> nombre.equalsIgnoreCase(e.getNombre()))
                .findFirst()
                .orElse(null);
    }

    private double calcularRecaudo(Evento evento) {
        if (evento == null) {
            return 0;
        }
        return evento.getTiquetes().stream()
                .mapToDouble(t -> t.getPrecio() + t.getCargoServicio() + t.getCargoEmision())
                .sum();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        var g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(12, 18, 48), getWidth(), getHeight(), new Color(46, 109, 172)));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
        g2.setComposite(AlphaComposite.SrcOver.derive(0.08f));
        var rnd = new Random(11);
        for (int i = 0; i < 18; i++) {
            int w = 90 + rnd.nextInt(120);
            int h = 70 + rnd.nextInt(140);
            int x = rnd.nextInt(Math.max(1, getWidth() - w));
            int y = rnd.nextInt(Math.max(1, getHeight() - h));
            g2.setPaint(new GradientPaint(x, y,
                    new Color(245, 71, 119, 140),
                    x + w, y + h,
                    new Color(255, 193, 7, 140)));
            g2.fillRoundRect(x, y, w, h, 22, 22);
        }
        g2.dispose();
    }
}

class AvatarPreviewPanel extends JPanel {
    private final JLabel titulo;
    private final JLabel imagen;
    private final JLabel detalle1;
    private final JLabel detalle2;
    private final JLabel detalle3;

    AvatarPreviewPanel() {
        super(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(180, 420));
        titulo = new JLabel("Hover para ver el artista/evento", JLabel.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 13f));

        imagen = new JLabel();
        imagen.setHorizontalAlignment(JLabel.CENTER);
        imagen.setVerticalAlignment(JLabel.CENTER);
        imagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 60)),
                new EmptyBorder(14, 10, 14, 10)));

        detalle1 = crearDetalleLabel();
        detalle2 = crearDetalleLabel();
        detalle3 = crearDetalleLabel();

        JPanel detallePanel = new JPanel();
        detallePanel.setOpaque(false);
        detallePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.CENTER;
        detallePanel.add(detalle1, gbc);
        gbc.gridy++;
        detallePanel.add(detalle2, gbc);
        gbc.gridy++;
        detallePanel.add(detalle3, gbc);

        add(titulo, BorderLayout.NORTH);
        add(imagen, BorderLayout.CENTER);
        add(detallePanel, BorderLayout.SOUTH);

        restaurarMensaje();
    }

    void actualizar(String nombre, ImageIcon icon, String linea1, String linea2, String linea3) {
        titulo.setText(nombre);
        imagen.setIcon(icon);
        detalle1.setText(linea1);
        detalle2.setText(linea2);
        detalle3.setText(linea3);
    }

    void restaurarMensaje() {
        titulo.setText("Hover para ver el artista/evento");
        imagen.setIcon(null);
        detalle1.setText("Eventos disponibles · Artistas destacados");
        detalle2.setText("Pasa el cursor por los mosaicos para ver stats");
        detalle3.setText("Compra, imprime y comparte con BoletaMaster");
    }

    private JLabel crearDetalleLabel() {
        JLabel lbl = new JLabel("", JLabel.CENTER);
        lbl.setForeground(new Color(230, 240, 255));
        lbl.setFont(lbl.getFont().deriveFont(java.awt.Font.PLAIN, 11.5f));
        return lbl;
    }
}

class RotatingLogo extends JPanel {
    private double angulo = 0;
    private final javax.swing.Timer timer;

    RotatingLogo() {
        setOpaque(false);
        setPreferredSize(new Dimension(200, 120));
        timer = new javax.swing.Timer(70, e -> {
            angulo += Math.toRadians(3);
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        var g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int r = Math.min(getWidth(), getHeight()) / 2 - 14;

        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        g2.setStroke(new java.awt.BasicStroke(2f));
        g2.setColor(new Color(245, 199, 83, 200));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);

        for (int i = 0; i < 5; i++) {
            double a = angulo + i * (Math.PI * 2 / 5);
            int px = cx + (int) (Math.cos(a) * r);
            int py = cy + (int) (Math.sin(a) * r);
            g2.setColor(i % 2 == 0 ? new Color(245, 71, 119) : new Color(111, 203, 255));
            g2.fillOval(px - 8, py - 8, 16, 16);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 18f));
        String texto = "BoletaMaster";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto, cx - fm.stringWidth(texto) / 2, cy + fm.getAscent() / 2);
        g2.dispose();
    }
}

class TileCarousel extends JPanel {
    private final List<String> items;
    private final javax.swing.Timer timer;
    private Consumer<String> hoverListener;
    private final java.util.List<TileButton> tiles = new java.util.ArrayList<>();
    private int pagina = 0;

    TileCarousel(List<String> eventos, List<String> artistas) {
        setOpaque(false);
        setLayout(new java.awt.GridLayout(2, 2, 12, 12));
        this.items = prepararItems(eventos, artistas);

        for (int i = 0; i < 4; i++) {
            TileButton btn = new TileButton();
            btn.setFont(btn.getFont().deriveFont(java.awt.Font.BOLD, 16f));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    notificarHover((String) btn.getClientProperty("item"));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    notificarHover(null);
                }
            });
            tiles.add(btn);
            add(btn);
        }

        timer = new javax.swing.Timer(1700, e -> avanzar());
        timer.start();
        actualizarTiles();
    }

    void setHoverListener(Consumer<String> listener) {
        this.hoverListener = listener;
    }

    private void avanzar() {
        int paginasTotales = (int) Math.ceil(items.size() / 4.0);
        pagina = (pagina + 1) % Math.max(1, paginasTotales);
        actualizarTiles();
    }

    private void actualizarTiles() {
        for (int i = 0; i < tiles.size(); i++) {
            int idx = (pagina * 4 + i) % items.size();
            String texto = items.get(idx);
            Color color = generarColor(idx);
            TileButton btn = tiles.get(i);
            btn.setColor(color);
            btn.setText(texto.toUpperCase());
            btn.putClientProperty("item", texto);
        }
        revalidate();
        repaint();
    }

    private Color generarColor(int index) {
        float hue = (index * 0.11f) % 1.0f;
        float saturation = 0.70f;
        float brightness = 0.95f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    private void notificarHover(String texto) {
        if (hoverListener != null) {
            hoverListener.accept(texto);
        }
    }

    private static List<String> prepararItems(List<String> eventos, List<String> artistas) {
        List<String> lista = new java.util.ArrayList<>();
        if (eventos != null) {
            lista.addAll(eventos);
        }
        if (artistas != null) {
            lista.addAll(artistas);
        }
        if (lista.isEmpty()) {
            lista.addAll(List.of("BoletaMaster", "Conciertos", "Festivales", "Teatro"));
        }
        while (lista.size() < 8) {
            lista.addAll(new java.util.ArrayList<>(lista));
        }
        return lista;
    }
}

class TileButton extends JButton {
    private Color color = new Color(255, 193, 7);

    TileButton() {
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }

    void setColor(Color color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        var g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, color, getWidth(), getHeight(), color.brighter());
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
        g2.setColor(new Color(255, 255, 255, 60));
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 22, 22);
        g2.dispose();
        super.paintComponent(g);
    }
}

class PosterPanel extends JPanel {

    private final DatosImpresion datos;

    PosterPanel(DatosImpresion datos) {
        this.datos = datos;
        setPreferredSize(new Dimension(450, 480));
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        var g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint fondo = new GradientPaint(0, 0, new Color(10, 0, 36),
                getWidth(), getHeight(), new Color(91, 35, 121));
        g2.setPaint(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

        // Poster ilustrado sin JPG/PNG externos
        g2.setColor(new Color(255, 193, 7, 230));
        g2.fillRoundRect(32, 36, getWidth() - 64, 120, 18, 18);
        g2.setColor(new Color(245, 71, 119, 200));
        g2.fillRoundRect(52, 178, getWidth() - 104, 86, 18, 18);
        g2.setColor(new Color(74, 217, 217, 180));
        g2.fillRoundRect(42, 280, getWidth() - 84, 140, 18, 18);

        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 24f));
        g2.drawString("BoletaMaster presenta", 54, 82);

        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 20f));
        drawWrapped(g2, datos.nombreEvento, 54, 112, getWidth() - 108, 22);

        g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN, 14f));
        drawWrapped(g2, "Tu acceso para " + datos.fechaEvento + " · Localidad " + datos.localidad,
                54, 148, getWidth() - 108, 18);

        drawChipList(g2, 64, 210, getWidth() - 128, new String[]{
                "ID evento " + datos.idEvento,
                "Tiquete #" + datos.idTiquete,
                "Localidad: " + datos.localidad,
                "Fecha: " + datos.fechaEvento,
                "Precio: $" + String.format("%.0f", datos.precioTotal),
                "Emisión: " + datos.fechaImpresion
        });

        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 16f));
        g2.drawString("Escanea el QR para validar tu entrada", 64, getHeight() - 56);

        g2.dispose();
    }

    private void drawWrapped(java.awt.Graphics2D g2, String texto, int x, int y, int width, int lineHeight) {
        FontMetrics fm = g2.getFontMetrics();
        String[] palabras = texto.split(" ");
        StringBuilder linea = new StringBuilder();
        int cursorY = y;
        for (String palabra : palabras) {
            String candidata = linea.length() == 0 ? palabra : linea + " " + palabra;
            if (fm.stringWidth(candidata) > width) {
                g2.drawString(linea.toString(), x, cursorY);
                linea = new StringBuilder(palabra);
                cursorY += lineHeight;
            } else {
                linea = new StringBuilder(candidata);
            }
        }
        if (!linea.isEmpty()) {
            g2.drawString(linea.toString(), x, cursorY);
        }
    }

    private void drawChipList(java.awt.Graphics2D g2, int startX, int startY, int maxWidth, String[] chips) {
        int x = startX;
        int y = startY;
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 14f));
        for (String chip : chips) {
            int chipW = g2.getFontMetrics().stringWidth(chip) + 24;
            if (x + chipW > startX + maxWidth) {
                x = startX;
                y += 34;
            }
            g2.setColor(new Color(255, 255, 255, 160));
            g2.fillRoundRect(x, y, chipW, 26, 12, 12);
            g2.setColor(new Color(35, 54, 96));
            g2.drawString(chip, x + 12, y + 18);
            x += chipW + 10;
        }
    }
}

enum RolSeleccionado { CLIENTE, ADMIN, ORGANIZADOR }

interface LoginHandler {
    void onLogin(RolSeleccionado rol, String login, String password);
}

class LoginPanel extends JPanel {

    private LoginHandler handler;

    private final List<Evento> eventos;

    public LoginPanel(List<Evento> eventos) {
        super(new BorderLayout());
        this.eventos = eventos == null ? List.of() : eventos;
        construir();
    }

    private void construir() {
        setBackground(new java.awt.Color(243, 245, 252));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        formulario.setBorder(new EmptyBorder(30, 20, 30, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        javax.swing.JLabel titulo = new javax.swing.JLabel("BoletaMaster · Empresa dedicada a la venta de tiquetes");
        titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 17f));
        titulo.setForeground(new java.awt.Color(35, 54, 96));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formulario.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel lblRol = new javax.swing.JLabel("Rol:");
        lblRol.setForeground(new java.awt.Color(35, 54, 96));
        formulario.add(lblRol, gbc);

        JComboBox<RolSeleccionado> roles = new JComboBox<>(RolSeleccionado.values());
        gbc.gridx = 1;
        formulario.add(roles, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblUser = new javax.swing.JLabel("Usuario:");
        lblUser.setForeground(new java.awt.Color(35, 54, 96));
        formulario.add(lblUser, gbc);

        JTextField loginField = new JTextField(20);
        gbc.gridx = 1;
        formulario.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPass = new javax.swing.JLabel("Contraseña:");
        lblPass.setForeground(new java.awt.Color(35, 54, 96));
        formulario.add(lblPass, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formulario.add(passwordField, gbc);

        JButton ingresar = new JButton("Ingresar a BoletaMaster");
        ingresar.setBackground(new java.awt.Color(45, 136, 255));
        ingresar.setForeground(java.awt.Color.WHITE);
        ingresar.setFocusPainted(false);
        ingresar.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        ingresar.setFont(ingresar.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formulario.add(ingresar, gbc);

        ingresar.addActionListener(e -> {
            if (handler != null) {
                handler.onLogin((RolSeleccionado) roles.getSelectedItem(),
                        loginField.getText().trim(),
                        new String(passwordField.getPassword()));
            }
        });

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 12, 16, 12),
                BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(220, 225, 237))));

        JLabel subtitulo = new JLabel("Inicio de sesión rápido");
        subtitulo.setFont(subtitulo.getFont().deriveFont(java.awt.Font.PLAIN, 13f));
        subtitulo.setForeground(new java.awt.Color(92, 105, 128));
        contenedor.add(subtitulo, BorderLayout.NORTH);
        contenedor.add(formulario, BorderLayout.CENTER);

        BannerPanel banner = new BannerPanel(eventos);
        banner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(banner, BorderLayout.WEST);
        add(contenedor, BorderLayout.CENTER);
    }

    public void setOnLogin(LoginHandler handler) {
        this.handler = handler;
    }
}

class ClientePanel extends JPanel {

    private final BoletaMasterSystem sistema;
    private final Cliente cliente;
    private final Runnable onLogout;
    private final JList<Tiquete> listaTiquetes;
    private final JList<OfertaMarketPlace> listaOfertasActivas;
    private final JList<OfertaMarketPlace> listaOfertasPropias;
    private final JList<ContraOferta> listaContraPendientes;

    ClientePanel(BoletaMasterSystem sistema, Cliente cliente, Runnable onLogout) {
        super(new BorderLayout());
        this.sistema = sistema;
        this.cliente = cliente;
        this.onLogout = onLogout;
        this.listaTiquetes = new JList<>();
        this.listaOfertasActivas = new JList<>();
        this.listaOfertasPropias = new JList<>();
        this.listaContraPendientes = new JList<>();
        construir();
        refrescarTodo();
    }

    private void construir() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Mis tiquetes", crearPanelTiquetes());
        tabs.addTab("Marketplace", crearPanelMarketplace());

        add(tabs, BorderLayout.CENTER);
        add(crearBarraSuperior("Cliente: " + cliente.getLogin()), BorderLayout.NORTH);
    }

    private JPanel crearBarraSuperior(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        javax.swing.JLabel label = new javax.swing.JLabel(titulo);
        JButton salir = new JButton("Cerrar sesión");
        salir.addActionListener(e -> onLogout.run());
        panel.add(label, BorderLayout.WEST);
        panel.add(salir, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelTiquetes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        listaTiquetes.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(formatearTiquete(value));
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });
        panel.add(new JScrollPane(listaTiquetes), BorderLayout.CENTER);

        JButton imprimir = new JButton("Imprimir seleccionado");
        imprimir.addActionListener(e -> imprimirSeleccionado());

        JPanel acciones = new JPanel();
        acciones.add(imprimir);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelMarketplace() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Ofertas activas", crearPanelOfertasActivas());
        tabs.addTab("Mis publicaciones", crearPanelOfertasPropias());
        tabs.addTab("Contraofertas recibidas", crearPanelContraofertas());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelOfertasActivas() {
        JPanel panel = new JPanel(new BorderLayout());
        listaOfertasActivas.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(formatearOferta(value));
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });
        panel.add(new JScrollPane(listaOfertasActivas), BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton actualizar = new JButton("Actualizar");
        JButton comprar = new JButton("Comprar");
        JButton contraofertar = new JButton("Contraofertar");
        acciones.add(actualizar);
        acciones.add(comprar);
        acciones.add(contraofertar);

        actualizar.addActionListener(e -> refrescarOfertas());
        comprar.addActionListener(e -> comprarSeleccionada());
        contraofertar.addActionListener(e -> contraofertarSeleccionada());

        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelOfertasPropias() {
        JPanel panel = new JPanel(new BorderLayout());
        listaOfertasPropias.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(formatearOferta(value));
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });
        panel.add(new JScrollPane(listaOfertasPropias), BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton publicar = new JButton("Publicar oferta");
        JButton cancelar = new JButton("Cancelar oferta seleccionada");
        acciones.add(publicar);
        acciones.add(cancelar);

        publicar.addActionListener(e -> publicarNuevaOferta());
        cancelar.addActionListener(e -> cancelarOfertaSeleccionada());

        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelContraofertas() {
        JPanel panel = new JPanel(new BorderLayout());
        listaContraPendientes.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(formatearContraoferta(value));
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });
        panel.add(new JScrollPane(listaContraPendientes), BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton aceptar = new JButton("Aceptar");
        JButton rechazar = new JButton("Rechazar");
        JButton refrescar = new JButton("Actualizar");
        acciones.add(aceptar);
        acciones.add(rechazar);
        acciones.add(refrescar);

        aceptar.addActionListener(e -> responderContraoferta(true));
        rechazar.addActionListener(e -> responderContraoferta(false));
        refrescar.addActionListener(e -> refrescarContraofertas());

        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private void refrescarTodo() {
        refrescarTiquetes();
        refrescarOfertas();
        refrescarOfertasPropias();
        refrescarContraofertas();
    }

    private void refrescarTiquetes() {
        List<Tiquete> tiquetes = sistema.obtenerTiquetesCliente(cliente);
        listaTiquetes.setListData(tiquetes.toArray(Tiquete[]::new));
    }

    private void refrescarOfertas() {
        List<OfertaMarketPlace> ofertas = sistema.obtenerOfertasActivas().stream()
                .filter(o -> !o.getVendedor().equals(cliente))
                .collect(Collectors.toList());
        listaOfertasActivas.setListData(ofertas.toArray(OfertaMarketPlace[]::new));
    }

    private void refrescarOfertasPropias() {
        List<OfertaMarketPlace> ofertas = sistema.obtenerOfertasPorVendedor(cliente);
        listaOfertasPropias.setListData(ofertas.toArray(OfertaMarketPlace[]::new));
    }

    private void refrescarContraofertas() {
        Map<OfertaMarketPlace, List<ContraOferta>> pendientes = sistema.contraofertasPendientes(cliente);
        List<ContraOferta> todas = pendientes.values().stream().flatMap(List::stream).toList();
        listaContraPendientes.setListData(todas.toArray(ContraOferta[]::new));
    }

    private void imprimirSeleccionado() {
        Tiquete tiquete = listaTiquetes.getSelectedValue();
        if (tiquete == null) {
            mostrarInfo("Seleccione un tiquete para imprimir");
            return;
        }
        try {
            DatosImpresion datos = tiquete.imprimirConDatos();
            TicketPreviewDialog.mostrar(this, datos);
            sistema.guardarDatos();
            refrescarTiquetes();
        } catch (Exception e) {
            mostrarError("No se pudo imprimir: " + e.getMessage());
        }
    }

    private void publicarNuevaOferta() {
        String ids = JOptionPane.showInputDialog(this, "IDs de tiquetes (separados por coma)");
        if (ids == null || ids.isBlank()) {
            return;
        }
        String precioStr = JOptionPane.showInputDialog(this, "Precio inicial de la oferta");
        if (precioStr == null || precioStr.isBlank()) {
            return;
        }
        try {
            double precio = Double.parseDouble(precioStr.trim());
            List<Integer> tiquetesIds = parsearEnteros(ids);
            OfertaMarketPlace oferta = sistema.publicarOferta(cliente, tiquetesIds, precio);
            mostrarInfo("Oferta creada: " + oferta.getId());
            refrescarOfertasPropias();
            refrescarOfertas();
        } catch (Exception e) {
            mostrarError("No se pudo publicar la oferta: " + e.getMessage());
        }
    }

    private void cancelarOfertaSeleccionada() {
        OfertaMarketPlace oferta = listaOfertasPropias.getSelectedValue();
        if (oferta == null) {
            mostrarInfo("Seleccione una oferta");
            return;
        }
        try {
            sistema.cancelarOfertaPorVendedor(cliente, oferta.getId());
            refrescarOfertasPropias();
            refrescarOfertas();
        } catch (Exception e) {
            mostrarError("No se pudo cancelar: " + e.getMessage());
        }
    }

    private void comprarSeleccionada() {
        OfertaMarketPlace oferta = listaOfertasActivas.getSelectedValue();
        if (oferta == null) {
            mostrarInfo("Seleccione una oferta a comprar");
            return;
        }
        try {
            sistema.comprarOferta(cliente, oferta.getId());
            mostrarInfo("Compra realizada");
            refrescarTodo();
        } catch (Exception e) {
            mostrarError("No se pudo comprar: " + e.getMessage());
        }
    }

    private void contraofertarSeleccionada() {
        OfertaMarketPlace oferta = listaOfertasActivas.getSelectedValue();
        if (oferta == null) {
            mostrarInfo("Seleccione una oferta para contraofertar");
            return;
        }
        String montoStr = JOptionPane.showInputDialog(this, "Valor de la contraoferta");
        if (montoStr == null || montoStr.isBlank()) {
            return;
        }
        try {
            double monto = Double.parseDouble(montoStr.trim());
            sistema.crearContraoferta(cliente, oferta.getId(), monto);
            mostrarInfo("Contraoferta enviada");
            refrescarContraofertas();
        } catch (Exception e) {
            mostrarError("No se pudo enviar: " + e.getMessage());
        }
    }

    private void responderContraoferta(boolean aceptar) {
        ContraOferta seleccion = listaContraPendientes.getSelectedValue();
        if (seleccion == null) {
            mostrarInfo("Seleccione una contraoferta");
            return;
        }
        Optional<OfertaMarketPlace> ofertaOrigen = sistema.obtenerTodasLasOfertas().stream()
                .filter(o -> o.getContraofertas().contains(seleccion)).findFirst();
        if (ofertaOrigen.isEmpty()) {
            mostrarError("No se encontró la oferta asociada");
            return;
        }
        try {
            if (aceptar) {
                sistema.aceptarContraoferta(cliente, ofertaOrigen.get().getId(), seleccion.getId());
            } else {
                sistema.rechazarContraoferta(cliente, ofertaOrigen.get().getId(), seleccion.getId());
            }
            refrescarTodo();
        } catch (Exception e) {
            mostrarError("Operación no completada: " + e.getMessage());
        }
    }

    private List<Integer> parsearEnteros(String linea) {
        return List.of(linea.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    private String formatearTiquete(Tiquete t) {
        String evento = t.getEvento() == null ? "(sin evento)" : t.getEvento().getNombre();
        return String.format("ID %d | Evento: %s | Impreso: %s", t.getIdTiquete(), evento, t.estaImpreso());
    }

    private String formatearOferta(OfertaMarketPlace o) {
        String eventos = o.getTiquetes().stream()
                .map(t -> t.getEvento() == null ? "(sin evento)" : t.getEvento().getNombre())
                .distinct()
                .collect(Collectors.joining(", "));
        return String.format("%s | Vendedor: %s | Precio: %.2f | Estado: %s | Eventos: %s",
                o.getId(), o.getVendedor().getLogin(), o.getPrecioInicial(), o.getEstado(), eventos);
    }

    private String formatearContraoferta(ContraOferta c) {
        String estado = c.getEstado() == EstadoContraOferta.PENDIENTE ? "Pendiente" : c.getEstado().name();
        return String.format("%s | Comprador: %s | Monto: %.2f | Estado: %s", c.getId(), c.getComprador().getLogin(), c.getMonto(), estado);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarTextoLargo(String titulo, String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setCaretPosition(0);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 300));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}

class OrganizadorPanel extends JPanel {

    OrganizadorPanel(BoletaMasterSystem sistema, Organizador organizador, Runnable onLogout) {
        super(new BorderLayout());
        add(crearBarraSuperior("Organizador: " + organizador.getLogin(), onLogout), BorderLayout.NORTH);

        List<Evento> eventos = sistema.getEventosOrganizador(organizador).stream().toList();
        JList<Evento> lista = new JList<>(eventos.toArray(Evento[]::new));
        lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            String descripcion = String.format("%s (%s) - %s %s", value.getNombre(), value.getIdEvento(), value.getFecha(), value.getHora());
            javax.swing.JLabel label = new javax.swing.JLabel(descripcion);
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });
        add(new JScrollPane(lista), BorderLayout.CENTER);
    }

    private JPanel crearBarraSuperior(String titulo, Runnable onLogout) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        javax.swing.JLabel label = new javax.swing.JLabel(titulo);
        JButton salir = new JButton("Cerrar sesión");
        salir.addActionListener(e -> onLogout.run());
        panel.add(label, BorderLayout.WEST);
        panel.add(salir, BorderLayout.EAST);
        return panel;
    }
}

class AdministradorPanel extends JPanel {

    private final BoletaMasterSystem sistema;
    private final Administrador admin;
    private final JList<OfertaMarketPlace> listaOfertas;

    AdministradorPanel(BoletaMasterSystem sistema, Administrador admin, Runnable onLogout) {
        super(new BorderLayout());
        this.sistema = sistema;
        this.admin = admin;
        this.listaOfertas = new JList<>();
        construir(onLogout);
        refrescar();
    }

    private void construir(Runnable onLogout) {
        add(crearBarraSuperior("Administrador: " + admin.getLogin(), onLogout), BorderLayout.NORTH);

        listaOfertas.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(formatearOferta(value));
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });
        add(new JScrollPane(listaOfertas), BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton cancelar = new JButton("Cancelar oferta seleccionada");
        JButton refrescar = new JButton("Actualizar");
        acciones.add(cancelar);
        acciones.add(refrescar);

        cancelar.addActionListener(e -> cancelarSeleccionada());
        refrescar.addActionListener(e -> refrescar());

        add(acciones, BorderLayout.SOUTH);
    }

    private JPanel crearBarraSuperior(String titulo, Runnable onLogout) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        javax.swing.JLabel label = new javax.swing.JLabel(titulo);
        JButton salir = new JButton("Cerrar sesión");
        salir.addActionListener(e -> onLogout.run());
        panel.add(label, BorderLayout.WEST);
        panel.add(salir, BorderLayout.EAST);
        return panel;
    }

    private void refrescar() {
        List<OfertaMarketPlace> ofertas = sistema.obtenerTodasLasOfertas();
        listaOfertas.setListData(ofertas.toArray(OfertaMarketPlace[]::new));
    }

    private void cancelarSeleccionada() {
        OfertaMarketPlace oferta = listaOfertas.getSelectedValue();
        if (oferta == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una oferta", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            sistema.cancelarOfertaPorAdministrador(admin, oferta.getId());
            refrescar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cancelar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatearOferta(OfertaMarketPlace oferta) {
        String eventos = oferta.getTiquetes().stream()
                .map(t -> t.getEvento() == null ? "(sin evento)" : t.getEvento().getNombre())
                .distinct()
                .collect(Collectors.joining(", "));
        return String.format("%s | Vendedor: %s | Precio: %.2f | Estado: %s | Eventos: %s",
                oferta.getId(), oferta.getVendedor().getLogin(), oferta.getPrecioInicial(), oferta.getEstado(), eventos);
    }
}
