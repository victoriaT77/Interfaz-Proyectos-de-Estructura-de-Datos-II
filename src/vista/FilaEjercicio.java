package vista;

import modelo.Ejercicio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/** Fila visual de un ejercicio. */
public class FilaEjercicio extends Ancho {

    public FilaEjercicio(Ejercicio e, VistaListener acciones) {
        super(new BorderLayout(14, 0));
        setBorder(new EmptyBorder(16, 26, 16, 18));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(Estilo.etiqueta(e.getTitulo(), 16, Font.BOLD, Estilo.AZUL));
        String desc = e.getDescripcion();
        if (desc != null && !desc.isEmpty()) {
            textos.add(Box.createVerticalStrut(4));
            JLabel d = new JLabel("<html><body style='width:520px'>"
                    + Estilo.esc(desc).replace("\n", "<br>") + "</body></html>");
            d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            d.setForeground(Estilo.GRIS);
            textos.add(d);
        }
        add(textos, BorderLayout.CENTER);

        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acc.setOpaque(false);
        if (e.getArchivo() != null) {
            Boton abrir = new Boton("Abrir", Estilo.AZUL, Estilo.AZUL_OSC, Color.WHITE);
            abrir.setBorder(new EmptyBorder(7, 18, 7, 18));
            abrir.setFont(new Font("Segoe UI", Font.BOLD, 12));
            abrir.addActionListener(ev -> acciones.abrirArchivo(e));
            acc.add(abrir);
        }
        Boton borrar = new Boton("✕", new Color(0xEEF0F7), new Color(0xF9D6DC), Estilo.ROJO);
        borrar.setBorder(new EmptyBorder(7, 12, 7, 12));
        borrar.addActionListener(ev -> acciones.eliminarEjercicio(e));
        acc.add(borrar);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(acc);
        add(centro, BorderLayout.EAST);
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilo.calidad(g);
        int w = getWidth() - 4, h = getHeight() - 6;
        Estilo.sombra(g, 0, 0, w, h, 18, 5);
        RoundRectangle2D rr = new RoundRectangle2D.Float(0, 0, w, h, 18, 18);
        g.setColor(Color.WHITE);
        g.fill(rr);
        g.setClip(rr);
        g.setColor(Estilo.DORADO);
        g.fillRect(0, 0, 7, h);
        g.dispose();
    }
}
