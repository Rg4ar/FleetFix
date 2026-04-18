package Frontend.FleetFix;

import Backend.FleetFix.VehiculoDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class PaginaVehiculos extends JPanel {

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

        VehiculoDAO dao = new VehiculoDAO();
        List<String[]> vehiculos = dao.obtenerTodos();

        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] iconos = {"🚙","🛻","🚗","🏗️","🚐","🚕"};
        for (int i = 0; i < vehiculos.size(); i++) {
            String[] v = vehiculos.get(i);
            grid.add(makeVehCard(v[1], v[2], v[3], iconos[i % iconos.length]));
        }

        wrapper.add(grid);
        add(wrapper, BorderLayout.NORTH);
    }

    private JPanel makeVehCard(String placa, String modelo, String estado, String icono) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_CARD);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.setColor(Tema.BORDER);
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20,20,20,20));

        JLabel ico = new JLabel(icono);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        ico.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(ico);
        card.add(Box.createVerticalStrut(14));

        JLabel lblPlaca = new JLabel(placa);
        lblPlaca.setFont(Tema.FONT_MONO.deriveFont(12f));
        lblPlaca.setForeground(Tema.ACCENT);
        lblPlaca.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblModelo = new JLabel(modelo);
        lblModelo.setFont(Tema.FONT_SANS.deriveFont(15f));
        lblModelo.setForeground(Tema.TEXT_PRIMARY);
        lblModelo.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblPlaca);
        card.add(Box.createVerticalStrut(2));
        card.add(lblModelo);
        card.add(Box.createVerticalStrut(10));

        boolean ok = estado.equals("Operativo");
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusRow.setOpaque(false);
        statusRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ok ? Tema.SUCCESS : Tema.WARN);
                g2.fillOval(0, 5, 8, 8);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(14, 18));

        JLabel lblEstado = new JLabel(ok ? "Operativo" : "Revision pendiente");
        lblEstado.setFont(Tema.FONT_SANS.deriveFont(12f));
        lblEstado.setForeground(Tema.TEXT_MUTED);

        statusRow.add(dot);
        statusRow.add(lblEstado);
        card.add(statusRow);
        return card;
    }
}
