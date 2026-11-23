package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/** Pantalla de ingreso. */
public class PanelLogin extends JPanel {

    public PanelLogin(VistaListener acciones, String institucion, String materia, String estudiante) {
        super(new GridBagLayout());

        JPanel tarjeta = new JPanel() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                Estilo.calidad(g);
                int x = 20, y = 20, w = getWidth() - 40, h = getHeight() - 40;
                Estilo.sombra(g, x, y, w, h, 30, 14);
                RoundRectangle2D rr = new RoundRectangle2D.Float(x, y, w, h, 30, 30);
                g.setColor(Color.WHITE);
                g.fill(rr);
                g.setClip(rr);
                g.setColor(Estilo.ROJO);
                g.fillRect(x, y, w, 9);
                g.setColor(Estilo.DORADO);
                g.fillRect(x, y + 9, w, 3);
                g.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(60, 60, 50, 60));
        tarjeta.setPreferredSize(new Dimension(580, 560));

        LogoView logo = new LogoView(120, false);
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel l1 = Estilo.etiqueta(institucion, 22, Font.BOLD, Estilo.AZUL);
        JLabel l2 = Estilo.etiqueta(materia, 15, Font.PLAIN, Estilo.GRIS);
        JLabel l3 = Estilo.etiqueta(estudiante, 18, Font.BOLD, Estilo.ROJO);
        for (JLabel l : new JLabel[]{l1, l2, l3}) l.setAlignmentX(CENTER_ALIGNMENT);

        JPanel barra = new JPanel();
        barra.setBackground(Estilo.DORADO);
        Dimension d = new Dimension(64, 4);
        barra.setPreferredSize(d);
        barra.setMaximumSize(d);
        barra.setMinimumSize(d);
        barra.setAlignmentX(CENTER_ALIGNMENT);

        Boton ingresar = new Boton("INGRESAR", Estilo.ROJO, Estilo.ROJO_OSC, Color.WHITE);
        ingresar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ingresar.setBorder(new EmptyBorder(14, 56, 14, 56));
        ingresar.setAlignmentX(CENTER_ALIGNMENT);
        ingresar.addActionListener(e -> acciones.ingresar());

        tarjeta.add(logo);
        tarjeta.add(Box.createVerticalStrut(22));
        tarjeta.add(l1);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(barra);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(l2);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(l3);
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(ingresar);

        add(tarjeta);
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilo.calidad(g);
        int w = getWidth(), h = getHeight();
        g.setPaint(new GradientPaint(0, 0, Estilo.AZUL_OSC, w, h, Estilo.AZUL));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(242, 169, 0, 28));
        g.fillOval(-120, h - 360, 520, 520);
        g.setColor(new Color(200, 16, 46, 40));
        g.fillOval(w - 380, -180, 560, 560);
        g.setColor(new Color(255, 255, 255, 10));
        g.fillOval(w - 220, h - 240, 340, 340);
        g.setColor(Estilo.ROJO);
        g.fillRect(0, h - 10, w, 6);
        g.setColor(Estilo.DORADO);
        g.fillRect(0, h - 4, w, 4);
        g.dispose();
    }
}
