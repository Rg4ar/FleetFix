package Frontend.FleetFix;

import Backend.FleetFix.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Ventana de inicio de sesión del sistema FleetFix.
 * Es la pantalla de entrada; valida credenciales y abre el dashboard.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblError; // Muestra mensajes de error de autenticación

    public LoginFrame() {
        setTitle("FleetFix");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 500);
        setLocationRelativeTo(null);
        setUndecorated(true); // Sin barra de título nativa
        setShape(new RoundRectangle2D.Double(0, 0, 420, 500, 20, 20)); // Bordes redondeados
        initUI();
    }

    private void initUI() {
        // Panel raíz con fondo personalizado redondeado
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        root.setOpaque(false);

        // Barra superior con botón de cerrar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 40));
        topBar.setBorder(new EmptyBorder(10, 16, 0, 16));

        JLabel btnClose = new JLabel("X");
        btnClose.setForeground(Tema.TEXT_MUTED);
        btnClose.setFont(Tema.FONT_MONO.deriveFont(13f));
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { System.exit(0); }
            public void mouseEntered(MouseEvent e) { btnClose.setForeground(Tema.DANGER); }
            public void mouseExited(MouseEvent e)  { btnClose.setForeground(Tema.TEXT_MUTED); }
        });
        topBar.add(btnClose, BorderLayout.EAST);

        // Permite arrastrar la ventana desde la barra superior
        final Point[] drag = {null};
        topBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { drag[0] = e.getPoint(); }
        });
        topBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - drag[0].x, loc.y + e.getY() - drag[0].y);
            }
        });
        root.add(topBar, BorderLayout.NORTH);

        // Panel central con el formulario de login
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(10, 44, 36, 44));

        // Logo y subtítulo
        JLabel logo = new JLabel("<html>Fleet<span style='color:#e8ff47'>Fix</span></html>");
        logo.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 32f));
        logo.setForeground(Tema.TEXT_PRIMARY);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("// sistema de gestion de flota");
        sub.setFont(Tema.FONT_MONO.deriveFont(12f));
        sub.setForeground(Tema.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(logo);
        center.add(Box.createVerticalStrut(4));
        center.add(sub);
        center.add(Box.createVerticalStrut(36));

        // Campo de usuario
        center.add(makeLabel("USUARIO"));
        center.add(Box.createVerticalStrut(6));
        txtUsuario = makeTextField();
        center.add(txtUsuario);
        center.add(Box.createVerticalStrut(16));

        // Campo de contraseña
        center.add(makeLabel("CONTRASENA"));
        center.add(Box.createVerticalStrut(6));
        txtPassword = makePasswordField();
        center.add(txtPassword);
        center.add(Box.createVerticalStrut(8));

        // Hint de credenciales disponibles
        JLabel hint = new JLabel("CodeStudents");
        hint.setFont(Tema.FONT_MONO.deriveFont(10f));
        hint.setForeground(Tema.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(hint);
        center.add(Box.createVerticalStrut(16));

        // Etiqueta de error (vacía hasta que falle el login)
        lblError = new JLabel(" ");
        lblError.setFont(Tema.FONT_MONO.deriveFont(12f));
        lblError.setForeground(Tema.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(lblError);
        center.add(Box.createVerticalStrut(6));

        // Botón de inicio de sesión
        JButton btnLogin = makeAccentButton("Iniciar sesion");
        btnLogin.addActionListener(e -> doLogin());
        center.add(btnLogin);

        root.add(center, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnLogin); // Enter también dispara el login
        setContentPane(root);
    }

    /**
     * Valida las credenciales con la base de datos.
     * Si son correctas, abre el dashboard; si no, muestra un mensaje de error.
     */
    private void doLogin() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());
        UsuarioDAO dao = new UsuarioDAO();
        if (dao.validarLogin(user, pass)) {
            dispose();
            new DashboardFrame(user).setVisible(true);
        } else {
            lblError.setText("Usuario o contrasena incorrectos");
            txtPassword.setText("");
        }
    }

    // --- Métodos auxiliares de construcción de UI ---

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Tema.FONT_MONO.deriveFont(11f));
        l.setForeground(Tema.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /** Campo de texto con borde redondeado y resaltado al tener foco. */
    private JTextField makeTextField() {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_SURFACE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(isFocusOwner() ? Tema.ACCENT2 : Tema.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(10, 14, 10, 14));
        f.setFont(Tema.FONT_SANS.deriveFont(14f));
        f.setForeground(Tema.TEXT_PRIMARY);
        f.setCaretColor(Tema.TEXT_PRIMARY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    /** Campo de contraseña con el mismo estilo que makeTextField(). */
    private JPasswordField makePasswordField() {
        JPasswordField f = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_SURFACE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(isFocusOwner() ? Tema.ACCENT2 : Tema.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(10, 14, 10, 14));
        f.setFont(Tema.FONT_SANS.deriveFont(14f));
        f.setForeground(Tema.TEXT_PRIMARY);
        f.setCaretColor(Tema.TEXT_PRIMARY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    /** Botón principal con fondo amarillo (color de acento del tema). */
    private JButton makeAccentButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? Tema.ACCENT.darker() : Tema.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 15f));
        b.setForeground(new Color(0x0d0f14));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    /** Punto de entrada de la aplicación. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}
