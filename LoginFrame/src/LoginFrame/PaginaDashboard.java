package Frontend.FleetFix;

import Backend.FleetFix.TicketDAO;
import Backend.FleetFix.VehiculoDAO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class PaginaDashboard extends JPanel {

    public PaginaDashboard() {
        setLayout(new BorderLayout());
        setBackground(Tema.BG_MAIN);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        TicketDAO ticketDAO = new TicketDAO();
        VehiculoDAO vehDAO  = new VehiculoDAO();

        int abiertos   = ticketDAO.contarPorEstado("Abierto");
        int enProgreso = ticketDAO.contarPorEstado("En progreso");
        int criticos   = ticketDAO.contarCriticos();
        int vehiculos  = vehDAO.contarActivos();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(makeHeader("Dashboard", "// resumen de operaciones"));
        content.add(Box.createVerticalStrut(24));

        // Tarjetas métricas
        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(makeStatCard("Tickets abiertos",  String.valueOf(abiertos),   Tema.WARN));
        stats.add(makeStatCard("En progreso",        String.valueOf(enProgreso), Tema.ACCENT2));
        stats.add(makeStatCard("Criticos",           String.valueOf(criticos),   Tema.DANGER));
        stats.add(makeStatCard("Vehiculos activos",  String.valueOf(vehiculos),  Tema.SUCCESS));
        content.add(stats);
        content.add(Box.createVerticalStrut(28));

        // Tabla reciente
        JPanel secHead = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        secHead.setOpaque(false);
        secHead.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel secTitle = new JLabel("Tickets recientes");
        secTitle.setFont(Tema.FONT_SANS.deriveFont(16f));
        secTitle.setForeground(Tema.TEXT_PRIMARY);
        secHead.add(secTitle);
        content.add(secHead);
        content.add(Box.createVerticalStrut(12));

        DefaultTableModel model = ticketDAO.obtenerTodos();
        // Mostrar solo los primeros 5
        while (model.getRowCount() > 5) model.removeRow(5);

        JTable table = buildTable(model);
        JScrollPane scroll = wrapTable(table);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(scroll);

        add(content, BorderLayout.CENTER);
    }

    private JPanel makeHeader(String title, String sub) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = new JLabel(title);
        t.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 26f));
        t.setForeground(Tema.TEXT_PRIMARY);
        JLabel s = new JLabel(sub);
        s.setFont(Tema.FONT_MONO.deriveFont(12f));
        s.setForeground(Tema.TEXT_MUTED);
        p.add(t); p.add(Box.createVerticalStrut(4)); p.add(s);
        return p;
    }

    private JPanel makeStatCard(String label, String value, Color color) {
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
        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(Tema.FONT_MONO.deriveFont(10f));
        lbl.setForeground(Tema.TEXT_MUTED);
        JLabel num = new JLabel(value);
        num.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 34f));
        num.setForeground(color);
        card.add(lbl); card.add(Box.createVerticalStrut(6)); card.add(num);
        return card;
    }

    static JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(Tema.BG_CARD);
        table.setForeground(Tema.TEXT_PRIMARY);
        table.setFont(Tema.FONT_SANS.deriveFont(13f));
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(Tema.BORDER);
        table.setSelectionBackground(new Color(71,180,255,30));
        table.setSelectionForeground(Tema.TEXT_PRIMARY);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setFocusable(false);
        table.getTableHeader().setBackground(Tema.BG_SURFACE);
        table.getTableHeader().setForeground(Tema.TEXT_MUTED);
        table.getTableHeader().setFont(Tema.FONT_MONO.deriveFont(10f));
        table.getTableHeader().setBorder(new MatteBorder(0,0,1,0,Tema.BORDER));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                l.setBackground(sel ? new Color(71,180,255,30) : Tema.BG_CARD);
                l.setOpaque(true);
                l.setBorder(new EmptyBorder(0,12,0,12));
                String val = v == null ? "" : v.toString();
                switch (val) {
                    case "Abierto":     l.setForeground(Tema.ACCENT2); break;
                    case "En progreso": l.setForeground(Tema.WARN); break;
                    case "Cerrado":     l.setForeground(Tema.SUCCESS); break;
                    case "Critico":     l.setForeground(Tema.DANGER); break;
                    case "Alto":        l.setForeground(new Color(0xff8c42)); break;
                    case "Medio":       l.setForeground(Tema.WARN); break;
                    case "Bajo":        l.setForeground(Tema.SUCCESS); break;
                    case "Si":          l.setForeground(Tema.DANGER); break;
                    case "No":          l.setForeground(Tema.TEXT_MUTED); break;
                    default:            l.setForeground(Tema.TEXT_PRIMARY); break;
                }
                return l;
            }
        };

        DefaultTableCellRenderer baseRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                l.setBackground(sel ? new Color(71,180,255,30) : Tema.BG_CARD);
                l.setForeground(col == 0 ? Tema.TEXT_MUTED : Tema.TEXT_PRIMARY);
                l.setFont(col == 0 ? Tema.FONT_MONO.deriveFont(11f) : Tema.FONT_SANS.deriveFont(13f));
                l.setOpaque(true);
                l.setBorder(new EmptyBorder(0,12,0,12));
                return l;
            }
        };

        for (int i = 0; i < model.getColumnCount(); i++) {
            String col = model.getColumnName(i);
            if (col.equals("Estado") || col.equals("Nivel") || col.equals("Critico")) {
                table.getColumnModel().getColumn(i).setCellRenderer(colorRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(baseRenderer);
            }
        }
        return table;
    }

    static JScrollPane wrapTable(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(Tema.BG_CARD);
        scroll.getViewport().setBackground(Tema.BG_CARD);
        scroll.setBorder(new LineBorder(Tema.BORDER, 1));
        return scroll;
    }
}
