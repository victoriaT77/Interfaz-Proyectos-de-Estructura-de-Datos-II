package vista;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/** Botón redondeado con efecto hover. */
public class Boton extends JButton {
    private final Color base, hover;
    private boolean encima;

    public Boton(String texto, Color base, Color hover, Color fg) {
        super(texto);
        this.base = base;
        this.hover = hover;
        setForeground(fg);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(11, 28, 11, 28));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
        });
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilo.calidad(g);
        g.setColor(encima ? hover : base);
        g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
        g.dispose();
        super.paintComponent(g0);
    }
}
