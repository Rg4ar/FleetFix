package Frontend.FleetFix;

import java.awt.*;

public class Tema {
    public static final Color BG_MAIN    = new Color(0x0d0f14);
    public static final Color BG_SURFACE = new Color(0x141720);
    public static final Color BG_CARD    = new Color(0x1a1e2a);
    public static final Color BORDER     = new Color(0x2a2e3a);

    public static final Color TEXT_PRIMARY = new Color(0xf0f2f5);
    public static final Color TEXT_MUTED   = new Color(0x7a8099);

    public static final Color ACCENT  = new Color(0xe8ff47);
    public static final Color ACCENT2 = new Color(0x47b4ff);
    public static final Color DANGER  = new Color(0xff5252);
    public static final Color WARN    = new Color(0xffaa2c);
    public static final Color SUCCESS = new Color(0x4cffaa);

    public static final Font FONT_SANS;
    public static final Font FONT_MONO;

    static {
        FONT_SANS = new Font("Segoe UI", Font.PLAIN, 14);
        Font mono = new Font("JetBrains Mono", Font.PLAIN, 12);
        FONT_MONO = mono.getFamily().equals("Dialog") ? new Font("Consolas", Font.PLAIN, 12) : mono;
    }
}
