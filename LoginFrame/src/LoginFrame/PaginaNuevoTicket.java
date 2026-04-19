package Frontend.FleetFix;

import Backend.FleetFix.TicketDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Página con el formulario para crear un nuevo ticket de falla.
 * Permite seleccionar el tipo de vehículo, nivel de prioridad,
 * título y descripción del problema.
 */
public class PaginaNuevoTicket extends JPanel {

    private JComboBox<String> cboTipoVehiculo;
    private JComboBox<String> cboNivel;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private DashboardFrame parent;   // Referencia al frame principal para navegar entre páginas
    private NavButton btnTickets;    // Botón de Tickets para marcarlo activo tras crear el ticket
    private String usuario;

    // Tipos de vehículo disponibles para seleccionar
    private static final String[] TIPOS_VEHICULO = {
        "SUV", "Sedan", "Coupe", "Pickup", "Deportivo",
        "Camioneta", "Furgoneta", "Camion", "Microbus", "Otro"
    };

    public PaginaNuevoTicket(DashboardFrame parent, NavButton btnTickets, String usuario) {
        this.parent = parent;
        this.btnTickets = btnTickets;
        this.usuario = usuario;
        setLayout(new BorderLayout());
        setBackground(Tema.BG_MAIN);
        setBorder(new EmptyBorder(32, 32, 32, 32));
        initUI();
    }

    private void initUI() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        // Encabezado
        JLabel title = new JLabel("Nuevo ticket");
        title.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 26f));
        title.setForeground(Tema.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("// reportar falla de vehiculo");
        sub.setFont(Tema.FONT_MONO.deriveFont(12f));
        sub.setForeground(Tema.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(title);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(sub);
        wrapper.add(Box.createVerticalStrut(28));

        // Card contenedor del formulario
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.setColor(Tema.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(28, 32, 28, 32));
        card.setMaximumSize(new Dimension(640, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 1: Tipo de vehículo | Nivel de prioridad
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.insets = new Insets(0, 0, 4, 8);
        card.add(makeLabel("TIPO DE VEHICULO"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 8, 4, 0);
        card.add(makeLabel("NIVEL DE PRIORIDAD"), gbc);

        cboTipoVehiculo = makeCombo(TIPOS_VEHICULO);
        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 16, 8);
        card.add(cboTipoVehiculo, gbc);

        cboNivel = makeCombo(new String[]{"Bajo", "Medio", "Alto", "Critico"});
        gbc.gridx = 1; gbc.insets = new Insets(0, 8, 16, 0);
        card.add(cboNivel, gbc);

        // Fila 2: Título del ticket
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 4, 0);
        card.add(makeLabel("TITULO"), gbc);
        txtTitulo = makeTextField("Ej: Falla en sistema de frenos");
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 16, 0);
        card.add(txtTitulo, gbc);

        // Fila 3: Descripción del problema
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 4, 0);
        card.add(makeLabel("DESCRIPCION"), gbc);

        txtDescripcion = new JTextArea(5, 0);
        txtDescripcion.setFont(Tema.FONT_SANS.deriveFont(14f));
        txtDescripcion.setForeground(Tema.TEXT_PRIMARY);
        txtDescripcion.setCaretColor(Tema.TEXT_PRIMARY);
        txtDescripcion.setBackground(Tema.BG_SURFACE);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescripcion);
        descScroll.setBorder(new CompoundBorder(
            new LineBorder(Tema.BORDER, 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 20, 0);
        card.add(descScroll, gbc);

        // Fila 4: Botones de acción
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btns.setOpaque(false);
        JButton btnCrear = makeAccentButton("Crear ticket");
        btnCrear.addActionListener(e -> crearTicket());
        JButton btnCancelar = makeSecButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            // Volver a la lista de tickets sin crear nada
            parent.setActive(btnTickets);
            parent.showPage(new PaginaTickets(usuario));
        });
        btns.add(btnCrear);
        btns.add(Box.createHorizontalStrut(10));
        btns.add(btnCancelar);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 0, 0);
        card.add(btns, gbc);

        wrapper.add(card);
        add(wrapper, BorderLayout.NORTH);
    }

    /**
     * Valida los campos y crea el ticket en la base de datos.
     * Si tiene éxito, redirige a la página de tickets.
     */
    private void crearTicket() {
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String tipoVehiculo = (String) cboTipoVehiculo.getSelectedItem();

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // El índice del combo + 1 coincide con el id_nivel en la BD (1=Bajo, 2=Medio, 3=Alto, 4=Critico)
        int idNivel = cboNivel.getSelectedIndex() + 1;

        TicketDAO dao = new TicketDAO();
        if (dao.crearTicket(tipoVehiculo, idNivel, titulo, descripcion)) {
            JOptionPane.showMessageDialog(this, "Ticket creado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            txtTitulo.setText("");
            txtDescripcion.setText("");
            parent.setActive(btnTickets);
            parent.showPage(new PaginaTickets(usuario));
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo crear el ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos auxiliares de construcción de UI ---

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Tema.FONT_MONO.deriveFont(11f));
        l.setForeground(Tema.TEXT_MUTED);
        return l;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(Tema.FONT_SANS.deriveFont(14f));
        f.setForeground(Tema.TEXT_PRIMARY);
        f.setCaretColor(Tema.TEXT_PRIMARY);
        f.setBackground(Tema.BG_SURFACE);
        f.setBorder(new CompoundBorder(new LineBorder(Tema.BORDER, 1, true), new EmptyBorder(10, 14, 10, 14)));
        f.setPreferredSize(new Dimension(0, 40));
        return f;
    }

    private JComboBox<String> makeCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(Tema.FONT_SANS.deriveFont(14f));
        c.setForeground(Color.BLACK);
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(0, 40));
        c.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object v,
                    int idx, boolean sel, boolean foc) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, v, idx, sel, foc);
                l.setBackground(sel ? new Color(0x2E74B5) : Color.WHITE);
                l.setForeground(sel ? Color.WHITE : Color.BLACK);
                l.setFont(Tema.FONT_SANS.deriveFont(14f));
                l.setBorder(new EmptyBorder(6, 12, 6, 12));
                return l;
            }
        });
        return c;
    }

    /** Botón primario con fondo amarillo (acento). */
    private JButton makeAccentButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? Tema.ACCENT.darker() : Tema.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 14f));
        b.setForeground(new Color(0x0d0f14));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(130, 38));
        return b;
    }

    /** Botón secundario con borde y fondo transparente. */
    private JButton makeSecButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(Tema.BG_CARD);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(Tema.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(Tema.FONT_SANS.deriveFont(14f));
        b.setForeground(Tema.TEXT_PRIMARY);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(100, 38));
        return b;
    }
}
