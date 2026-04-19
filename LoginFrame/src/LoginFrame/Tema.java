package Frontend.FleetFix;

import java.awt.*;

/**
 * Clase de constantes visuales del sistema.
 * Define colores, fuentes y estilos reutilizables en toda la interfaz.
 */
public class Tema {

    // --- Colores de fondo ---
    public static final Color BG_MAIN    = new Color(0x0d0f14); // Fondo principal (más oscuro)
    public static final Color BG_SURFACE = new Color(0x141720); // Fondo de superficie (sidebar)
    public static final Color BG_CARD    = new Color(0x1a1e2a); // Fondo de tarjetas y paneles

    public static final Color BORDER = new Color(0x2a2e3a); // Color de bordes

    // --- Colores de texto ---
    public static final Color TEXT_PRIMARY = new Color(0xf0f2f5); // Texto principal (blanco suave)
    public static final Color TEXT_MUTED   = new Color(0x7a8099); // Texto secundario (gris)

    // --- Colores de acento y estado ---
    public static final Color ACCENT  = new Color(0xe8ff47); // Amarillo neón (acción principal)
    public static final Color ACCENT2 = new Color(0x47b4ff); // Azul (info / abierto)
    public static final Color DANGER  = new Color(0xff5252); // Rojo (error / crítico)
    public static final Color WARN    = new Color(0xffaa2c); // Naranja (advertencia / en progreso)
    public static final Color SUCCESS = new Color(0x4cffaa); // Verde (éxito / resuelto)

    // --- Fuentes ---
    public static final Font FONT_SANS; // Fuente general de la UI
    public static final Font FONT_MONO; // Fuente monoespaciada para etiquetas y código

    static {
        FONT_SANS = new Font("Segoe UI", Font.PLAIN, 14);

        // Intenta cargar JetBrains Mono; si no está disponible, usa Consolas
        Font mono = new Font("JetBrains Mono", Font.PLAIN, 12);
        FONT_MONO = mono.getFamily().equals("Dialog")
                ? new Font("Consolas", Font.PLAIN, 12)
                : mono;
    }
}
