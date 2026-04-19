package Frontend.FleetFix;

import Backend.FleetFix.TicketDAO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Página que lista todos los tickets registrados en el sistema.
 * Si el usuario es admin, permite cambiar el estado de cualquier ticket.
 */
public class PaginaTickets extends JPanel {

    private String usuario;
    private JTable table;
    private DefaultTableModel model;

    public PaginaTickets(String usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());
        setBackground(Tema.BG_MAIN);
        setBorder(new EmptyBorder(32, 32, 32, 32));
        initUI();
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        // Encabezado con título y botón de acción (solo para admin)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 24, 0));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Tickets");
        title.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 26f));
        title.setForeground(Tema.TEXT_PRIMARY);
        JLabel sub = new JLabel("// todos los reportes de fallas");
        sub.setFont(Tema.FONT_MONO.deriveFont(12f));
        sub.setForeground(Tema.TEXT_MUTED);
        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(sub);
        header.add(titles, BorderLayout.WEST);

        // Botón "Cambiar estado" visible solo para el administrador
        if (usuario.equals("admin")) {
            JButton btnCambiar = new JButton("Cambiar estado") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isPressed() ? Tema.ACCENT.darker() : Tema.ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnCambiar.setFont(Tema.FONT_SANS.deriveFont(Font.BOLD, 13f));
            btnCambiar.setForeground(new Color(0x0d0f14));
            btnCambiar.setOpaque(false);
            btnCambiar.setContentAreaFilled(false);
            btnCambiar.setBorderPainted(false);
            btnCambiar.setFocusPainted(false);
            btnCambiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnCambiar.setPreferredSize(new Dimension(150, 36));
            btnCambiar.addActionListener(e -> cambiarEstado());
            header.add(btnCambiar, BorderLayout.EAST);
        }

        content.add(header, BorderLayout.NORTH);

        // Cargar y mostrar todos los tickets en la tabla
        TicketDAO dao = new TicketDAO();
        model = dao.obtenerTodos();
        table = PaginaDashboard.buildTable(model);
        JScrollPane scroll = PaginaDashboard.wrapTable(table);
        content.add(scroll, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    /**
     * Abre un diálogo para cambiar el estado del ticket seleccionado.
     * Actualiza la tabla y la base de datos si el estado cambia.
     */
    private void cambiarEstado() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un ticket de la tabla primero.",
                "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idTicket = (int) model.getValueAt(fila, 0);
        String estadoActual = (String) model.getValueAt(fila, 4);

        String[] opciones = {"Abierto", "En progreso", "Resuelto", "Cerrado"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona el nuevo estado para el ticket #" + idTicket + ":",
            "Cambiar estado",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            estadoActual
        );

        // Solo actualizar si se eligió un estado diferente al actual
        if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {
            TicketDAO dao = new TicketDAO();
            if (dao.cambiarEstado(idTicket, nuevoEstado)) {
                model.setValueAt(nuevoEstado, fila, 4); // Actualizar la tabla visualmente
                table.repaint();
                JOptionPane.showMessageDialog(this,
                    "Estado actualizado a: " + nuevoEstado,
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el estado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
