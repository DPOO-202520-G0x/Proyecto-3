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
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        LoginPanel loginPanel = new LoginPanel();
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
            Path dataDir = Path.of("data");
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

        PosterPanel poster = new PosterPanel();
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
    BannerPanel() {
        setPreferredSize(new Dimension(520, 400));
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        var g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        java.awt.GradientPaint grad = new java.awt.GradientPaint(0, 0, new java.awt.Color(30, 61, 127),
                getWidth(), getHeight(), new java.awt.Color(116, 199, 255));
        g2.setPaint(grad);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);

        g2.setColor(new java.awt.Color(255, 255, 255, 180));
        g2.fillRoundRect(40, 40, getWidth() - 80, getHeight() - 80, 24, 24);

        g2.setColor(new java.awt.Color(16, 38, 91));
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 32f));
        g2.drawString("BoletaMaster", 70, 120);

        g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN, 18f));
        g2.drawString("Empresa dedicada a la venta de tiquetes", 70, 155);
        g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN, 15f));
        g2.drawString("Eventos en vivo, deportes y cultura con acceso inmediato.", 70, 185);

        g2.setColor(new java.awt.Color(247, 198, 62));
        g2.fillRoundRect(70, 230, getWidth() - 140, 110, 20, 20);
        g2.setColor(new java.awt.Color(51, 25, 7));
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 20f));
        g2.drawString("Artista destacado: Estrella Invitada", 90, 270);
        g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN, 14f));
        g2.drawString("¡Compra y administra tus tiquetes desde una misma ventana!", 90, 300);

        g2.setColor(new java.awt.Color(255, 255, 255, 160));
        for (int i = 0; i < 8; i++) {
            int x = 80 + i * 50;
            int y = 60 + (i % 2 == 0 ? 0 : 18);
            g2.fillOval(x, y, 12, 12);
        }

        g2.dispose();
    }
}

class PosterPanel extends JPanel {
    PosterPanel() {
        setPreferredSize(new Dimension(450, 480));
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        var g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        java.awt.GradientPaint fondo = new java.awt.GradientPaint(0, 0, new java.awt.Color(13, 12, 50),
                getWidth(), getHeight(), new java.awt.Color(88, 31, 94));
        g2.setPaint(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

        g2.setColor(new java.awt.Color(255, 193, 7, 200));
        g2.fillRoundRect(30, 40, getWidth() - 60, getHeight() - 80, 22, 22);

        g2.setColor(new java.awt.Color(33, 25, 10));
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 26f));
        g2.drawString("Vista previa BoletaMaster", 50, 90);

        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 20f));
        g2.drawString("Artista invitado: Legend Live", 50, 130);

        g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN, 15f));
        g2.drawString("Escucha los éxitos y accede al show con tu QR único.", 50, 160);

        g2.setColor(new java.awt.Color(0, 0, 0, 30));
        for (int i = 0; i < 6; i++) {
            int baseY = 190 + i * 40;
            g2.fillRoundRect(50, baseY, getWidth() - 120, 26, 16, 16);
        }

        g2.setColor(new java.awt.Color(255, 255, 255, 120));
        for (int i = 0; i < 9; i++) {
            int x = 70 + (i * 40) % (getWidth() - 140);
            int y = 220 + (i * 35) % (getHeight() - 200);
            int size = 30 + (i % 3) * 8;
            g2.fillOval(x, y, size, size);
        }

        g2.setColor(new java.awt.Color(33, 25, 10));
        g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 16f));
        g2.drawString("Escanea el QR para validar tu entrada", 50, getHeight() - 60);

        g2.dispose();
    }
}

enum RolSeleccionado { CLIENTE, ADMIN, ORGANIZADOR }

interface LoginHandler {
    void onLogin(RolSeleccionado rol, String login, String password);
}

class LoginPanel extends JPanel {

    private LoginHandler handler;

    public LoginPanel() {
        super(new BorderLayout());
        construir();
    }

    private void construir() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        javax.swing.JLabel titulo = new javax.swing.JLabel("BoletaMaster · Empresa dedicada a la venta de tiquetes");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formulario.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        formulario.add(new javax.swing.JLabel("Rol:"), gbc);

        JComboBox<RolSeleccionado> roles = new JComboBox<>(RolSeleccionado.values());
        gbc.gridx = 1;
        formulario.add(roles, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new javax.swing.JLabel("Usuario:"), gbc);

        JTextField loginField = new JTextField(20);
        gbc.gridx = 1;
        formulario.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(new javax.swing.JLabel("Contraseña:"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formulario.add(passwordField, gbc);

        JButton ingresar = new JButton("Ingresar a BoletaMaster");
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

        BannerPanel banner = new BannerPanel();
        banner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(banner, BorderLayout.WEST);
        add(formulario, BorderLayout.CENTER);
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
