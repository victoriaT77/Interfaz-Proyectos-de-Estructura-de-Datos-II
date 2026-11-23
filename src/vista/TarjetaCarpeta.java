package vista;

import modelo.Carpeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/** Tarjeta visual de una carpeta. */
public class TarjetaCarpeta extends JPanel {
    private final Carpeta carpeta;
    private boolean hover;

    public TarjetaCarpeta(Carpeta carpeta, VistaListener acciones) {
        this.carpeta = carpeta;
        setOpaque(false);
        setPreferredSize(new Dimension(260, 185));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPopupMenu menu = new JPopupMenu();
        JMenuItem ren = new JMenuItem("Renombrar");
        JMenuItem del = new JMenuItem("Eliminar");
        ren.addActionListener(e -> acciones.renombrarCarpeta(carpeta));
        del.addActionListener(e -> acciones.eliminarCarpeta(carpeta));
        menu.add(ren);
        menu.add(del);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) menu.show(TarjetaCarpeta.this, e.getX(), e.getY());
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) menu.show(TarjetaCarpeta.this, e.getX(), e.getY());
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) acciones.abrirCarpeta(carpeta);
            }
        });
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilo.calidad(g);
        int w = getWidth() - 8, h = getHeight() - 10;
        Estilo.sombra(g, 0, 0, w, h, 24, hover ? 12 : 6);
        g.setColor(Color.WHITE);
        g.fill(new RoundRectangle2D.Float(0, 0, w, h, 24, 24));
        if (hover) {
            g.setColor(Estilo.ROJO);
            g.setStroke(new BasicStroke(2f));
            g.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 24, 24));
        }

        // Icono de carpeta
        int ix = 24, iy = 24;
        g.setColor(new Color(0xE09A00));
        g.fill(new RoundRectangle2D.Float(ix, iy, 36, 16, 8, 8));
        g.setPaint(new GradientPaint(0, iy + 10, Estilo.DORADO_C, 0, iy + 56, Estilo.DORADO));
        g.fill(new RoundRectangle2D.Float(ix, iy + 8, 70, 50, 12, 12));
        g.setColor(new Color(255, 255, 255, 70));
        g.fill(new RoundRectangle2D.Float(ix, iy + 18, 70, 40, 12, 12));

        // Insignia con cantidad
        int n = carpeta.total();
        String ns = String.valueOf(n);
        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fb = g.getFontMetrics();
        int bw = Math.max(28, fb.stringWidth(ns) + 18);
        int bx = w - bw - 20, by = 24;
        g.setColor(n > 0 ? Estilo.ROJO : new Color(0xD5D9E8));
        g.fill(new RoundRectangle2D.Float(bx, by, bw, 26, 26, 26));
        g.setColor(n > 0 ? Color.WHITE : Estilo.GRIS);
        g.drawString(ns, bx + (bw - fb.stringWidth(ns)) / 2, by + 18);

        // Nombre
        g.setFont(new Font("Segoe UI", Font.BOLD, 17));
        g.setColor(Estilo.AZUL);
        FontMetrics fm = g.getFontMetrics();
        int y = 116;
        for (String l : Estilo.envolver(carpeta.getNombre(), fm, w - 44, 2)) {
            g.drawString(l, 24, y);
            y += fm.getHeight() - 2;
        }

        // Detalle
        int sub = carpeta.getHijos().size();
        String det = (sub == 0 ? "" : sub + (sub == 1 ? " subcarpeta · " : " subcarpetas · "))
                + n + (n == 1 ? " ejercicio" : " ejercicios");
        g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.setColor(Estilo.GRIS);
        g.drawString(det, 24, h - 18);
        g.dispose();
    }
}
