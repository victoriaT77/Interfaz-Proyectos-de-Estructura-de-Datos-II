package vista;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/** Dibuja el logo de la EPN escalado, opcionalmente sobre un chip blanco. */
public class LogoView extends JComponent {
    private final int alto;
    private final boolean chip;

    public LogoView(int alto, boolean chip) {
        this.alto = alto;
        this.chip = chip;
        setOpaque(false);
    }

    private int pad() { return chip ? 6 : 0; }

    private double ratio() {
        return Estilo.LOGO == null ? 1.0 : (double) Estilo.LOGO.getWidth() / Estilo.LOGO.getHeight();
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension((int) (alto * ratio()) + 2 * pad(), alto + 2 * pad());
    }
    @Override public Dimension getMinimumSize() { return getPreferredSize(); }
    @Override public Dimension getMaximumSize() { return getPreferredSize(); }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilo.calidad(g);
        if (chip) {
            g.setColor(Color.WHITE);
            g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
        }
        if (Estilo.LOGO != null) {
            g.drawImage(Estilo.LOGO, pad(), pad(), (int) (alto * ratio()), alto, null);
        } else {
            g.setColor(Estilo.ROJO);
            g.fillRoundRect(pad(), pad(), alto, alto, 16, 16);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI", Font.BOLD, alto / 3));
            FontMetrics fm = g.getFontMetrics();
            g.drawString("EPN", pad() + (alto - fm.stringWidth("EPN")) / 2, pad() + alto / 2 + fm.getAscent() / 3);
        }
        g.dispose();
    }
}
