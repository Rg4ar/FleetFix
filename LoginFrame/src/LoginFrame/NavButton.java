package Frontend.FleetFix;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class NavButton extends JButton {
    private boolean activo = false;

    public NavButton(String texto, String icono) {
        super(icono + "  " + texto);
        setFont(Tema.FONT_SANS.deriveFont(14f));
        setForeground(Tema.TEXT_MUTED);
        setOpaque(false); setContentAreaFilled(false);
        setBorderPainted(false); setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        setForeground(activo ? Tema.ACCENT : Tema.TEXT_MUTED);
        repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (activo) {
            g2.setColor(Tema.BG_CARD);
            g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 10, 10);
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(255,255,255,8));
            g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 10, 10);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
