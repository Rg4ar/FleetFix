package Frontend.FleetFix;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana principal del sistema tras iniciar sesión.
 * Contiene el sidebar de navegación y un área central donde se cargan las páginas.
 */
public class DashboardFrame extends JFrame {

    private JPanel contentArea; // Área donde se renderiza la página activa
    private String usuario;
    private NavButton btnActivo; // Referencia al botón de navegación actualmente seleccionado

    public DashboardFrame(String usuario) {
        this.usuario = usuario;
        setTitle("FleetFix");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Tema.BG_MAIN);
        root.add(buildSidebar(), BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Tema.BG_MAIN);
        root.add(contentArea, BorderLayout.CENTER);

        setContentPane(root);
        showPage(new PaginaDashboard()); // Página inicial al abrir el dashboard
    }

    /**
     * Construye el panel lateral con logo, botones de navegación e info del usuario.
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Tema.BG_SURFACE);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, Tema.BORDER));

        // Logo en la parte superior
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel logo = new JLabel("<html>Fleet<span style='color:#e8ff47'>Fix</span></html>");
        logo.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 22f));
        logo.setForeground(Tema.TEXT_PRIMARY);
        logoPanel.add(logo);
        sidebar.add(logoPanel);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(Tema.BORDER);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(12));

        // Botones de navegación
        NavButton btnDash      = new NavButton("Dashboard",    "⊞");
        NavButton btnTickets   = new NavButton("Tickets",      "≡");
        NavButton btnNuevo     = new NavButton("Nuevo ticket", "+");
        NavButton btnVehiculos = new NavButton("Vehiculos",    "◈");

        btnDash.addActionListener(e      -> { setActive(btnDash);      showPage(new PaginaDashboard()); });
        btnTickets.addActionListener(e   -> { setActive(btnTickets);   showPage(new PaginaTickets(usuario)); });
        btnNuevo.addActionListener(e     -> { setActive(btnNuevo);     showPage(new PaginaNuevoTicket(this, btnTickets, usuario)); });
        btnVehiculos.addActionListener(e -> { setActive(btnVehiculos); showPage(new PaginaVehiculos()); });

        sidebar.add(btnDash);
        sidebar.add(btnTickets);
        sidebar.add(btnNuevo);
        sidebar.add(btnVehiculos);
        sidebar.add(Box.createVerticalGlue()); // Empuja el panel de usuario hacia abajo

        // Panel inferior con avatar e información del usuario
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setOpaque(false);
        userPanel.setBorder(new EmptyBorder(12, 12, 8, 12));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Avatar circular con las iniciales del usuario
        String initials = usuario.length() >= 2 ? usuario.substring(0, 2).toUpperCase() : usuario.toUpperCase();
        JLabel avatar = new JLabel(initials, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 12f));
        avatar.setForeground(new Color(0x0d0f14));

        // Nombre y rol del usuario
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(Character.toUpperCase(usuario.charAt(0)) + usuario.substring(1));
        nameLabel.setFont(Tema.FONT_SANS.deriveFont(13f));
        nameLabel.setForeground(Tema.TEXT_PRIMARY);
        JLabel roleLabel = new JLabel(usuario.equals("admin") ? "administrador" : "usuario");
        roleLabel.setFont(Tema.FONT_MONO.deriveFont(11f));
        roleLabel.setForeground(Tema.TEXT_MUTED);
        namePanel.add(nameLabel);
        namePanel.add(roleLabel);

        userPanel.add(avatar, BorderLayout.WEST);
        userPanel.add(namePanel, BorderLayout.CENTER);
        sidebar.add(userPanel);

        // Botón de cerrar sesión
        JButton btnLogout = new JButton("Cerrar sesion") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 82, 82, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(getModel().isRollover() ? Tema.DANGER : Tema.TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnLogout.setFont(Tema.FONT_SANS.deriveFont(13f));
        btnLogout.setOpaque(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnLogout.setBorder(new EmptyBorder(8, 12, 12, 12));
        btnLogout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });
        sidebar.add(btnLogout);

        setActive(btnDash); // Dashboard activo por defecto
        return sidebar;
    }

    /**
     * Marca el botón dado como activo y desmarca el anterior.
     */
    public void setActive(NavButton btn) {
        if (btnActivo != null) btnActivo.setActivo(false);
        btnActivo = btn;
        btn.setActivo(true);
    }

    /**
     * Reemplaza el contenido del área central con la página indicada.
     */
    public void showPage(JPanel page) {
        contentArea.removeAll();
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    public String getUsuario() {
        return usuario;
    }
}
