package vista;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Paleta de la EPN y utilidades de dibujo compartidas por la vista. */
public final class Estilo {
    private Estilo() { }

    public static final Color ROJO     = new Color(0xC8102E);
    public static final Color ROJO_OSC = new Color(0xA00C24);
    public static final Color AZUL     = new Color(0x14235A);
    public static final Color AZUL_OSC = new Color(0x0A1440);
    public static final Color DORADO   = new Color(0xF2A900);
    public static final Color DORADO_C = new Color(0xFFCB4D);
    public static final Color FONDO    = new Color(0xF3F4F9);
    public static final Color GRIS     = new Color(0x5A6385);

    public static final BufferedImage LOGO = cargarLogo();

    private static BufferedImage cargarLogo() {
        try {
            java.net.URL u = Estilo.class.getResource("/logo.png");
            if (u != null) return ImageIO.read(u);
            File f = new File("logo.png");
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) { }
        return null;
    }

    public static void calidad(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static void sombra(Graphics2D g, int x, int y, int w, int h, int arc, int capas) {
        for (int i = capas; i > 0; i--) {
            g.setColor(new Color(10, 20, 60, 9));
            g.fill(new RoundRectangle2D.Float(x - i + 1, y - i + 5, w + 2 * i, h + 2 * i, arc + i, arc + i));
        }
    }

    public static List<String> envolver(String t, FontMetrics fm, int w, int max) {
        List<String> out = new ArrayList<>();
        String linea = "";
        for (String p : t.split(" ")) {
            String prueba = linea.isEmpty() ? p : linea + " " + p;
            if (fm.stringWidth(prueba) <= w) linea = prueba;
            else { if (!linea.isEmpty()) out.add(linea); linea = p; }
        }
        if (!linea.isEmpty()) out.add(linea);
        if (out.size() > max) {
            out = new ArrayList<>(out.subList(0, max));
            out.set(max - 1, out.get(max - 1) + "…");
        }
        return out;
    }

    public static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static JLabel etiqueta(String t, int tam, int estilo, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", estilo, tam));
        l.setForeground(c);
        return l;
    }
}
