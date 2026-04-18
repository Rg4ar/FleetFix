package Frontend.FleetFix;

import Backend.FleetFix.TicketDAO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class PaginaVehiculos extends JPanel {

    private static final Map<String, String> ICONOS = new LinkedHashMap<>();
    static {
        ICONOS.put("SUV",       "🚙");
        ICONOS.put("Sedan",     "🚗");
        ICONOS.put("Coupe",     "🏎️");
        ICONOS.put("Pickup",    "🛻");
        ICONOS.put("Deportivo", "🏎️");
        ICONOS.put("Camioneta", "🚐");
        ICONOS.put("Furgoneta", "🚌");
        ICONOS.put("Camion",    "🚛");
        ICONOS.put("Microbus",  "🚍");
        ICONOS.put("Otro",      "🚘");
    }

    public PaginaVehiculos() {
        setLayout(new BorderLayout());
        setBackground(Tema.BG_MAIN);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Vehiculos");
        title.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 26f));
        title.setForeground(Tema.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("// flota registrada");
        sub.setFont(Tema.FONT_MONO.deriveFont(12f));
        sub.setForeground(Tema.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(title);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(sub);
        wrapper.add(Box.createVerticalStrut(28));

        Map<String, Integer> conteo = obtenerVehiculosDesdeTickets();

        if (conteo.isEmpty()) {
            JLabel empty = new JLabel("No hay tickets registrados aun. Crea un ticket para ver los vehiculos aqui.");
            empty.setFont(Tema.FONT_SANS.deriveFont(14f));
            empty.setForeground(Tema.TEXT_MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapper.add(empty);
        } else {
            JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
            grid.setOpaque(false);
            grid.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
                String tipo = entry.getKey();
                int cantidad = entry.getValue();
                String icono = ICONOS.getOrDefault(tipo, "🚘");
                grid.add(makeVehCard(tipo, icono, cantidad));
            }
            wrapper.add(grid);
        }

        add(wrapper, BorderLayout.NORTH);
    }

    private Map<String, Integer> obtenerVehiculosDesdeTickets() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        try {
            TicketDAO dao = new TicketDAO();
            DefaultTableModel model = dao.obtenerTodos();
            for (int i = 0; i < model.getRowCount(); i++) {
                String tipo = (String) model.getValueAt(i, 2);
                conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener vehiculos: " + e.getMessage());
        }
        return conteo;
    }

    private JPanel makeVehCard(String tipo, String icono, int cantidad) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.setColor(Tema.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel ico = new JLabel(icono, SwingConstants.LEFT);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        ico.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(ico);
        card.add(Box.createVerticalStrut(14));

        JLabel lblTipo = new JLabel(tipo);
        lblTipo.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 17f));
        lblTipo.setForeground(Tema.TEXT_PRIMARY);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTipo);
        card.add(Box.createVerticalStrut(6));

        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusRow.setOpaque(false);
        statusRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.ACCENT2);
                g2.fillOval(0, 5, 8, 8);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(14, 18));

        JLabel lblTickets = new JLabel(cantidad + (cantidad == 1 ? " ticket registrado" : " tickets registrados"));
        lblTickets.setFont(Tema.FONT_SANS.deriveFont(12f));
        lblTickets.setForeground(Tema.TEXT_MUTED);

        statusRow.add(dot);
        statusRow.add(lblTickets);
        card.add(statusRow);

        return card;
    }
}
