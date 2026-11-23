package vista;

import modelo.Carpeta;
import modelo.Ejercicio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/** Pantalla principal: encabezado + carpetas + ejercicios. */
public class PanelDashboard extends JPanel {
    private final VistaListener acciones;
    private final JPanel contenido = new JPanel();

    public PanelDashboard(VistaListener acciones, String institucion, String materia, String estudiante) {
        super(new BorderLayout());
        this.acciones = acciones;

        // Encabezado
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                int w = getWidth(), h = getHeight();
                g.setPaint(new GradientPaint(0, 0, Estilo.AZUL_OSC, w, 0, Estilo.AZUL));
                g.fillRect(0, 0, w, h);
                g.setColor(Estilo.ROJO);
                g.fillRect(0, h - 6, w, 4);
                g.setColor(Estilo.DORADO);
                g.fillRect(0, h - 2, w, 2);
                g.dispose();
            }
        };
        header.setBorder(new EmptyBorder(14, 28, 20, 28));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        izq.setOpaque(false);
        izq.add(new LogoView(50, true));
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(Estilo.etiqueta(materia, 18, Font.BOLD, Color.WHITE));
        textos.add(Box.createVerticalStrut(3));
        textos.add(Estilo.etiqueta(institucion + "  ·  " + estudiante, 12, Font.PLAIN, Estilo.DORADO_C));
        izq.add(textos);
        header.add(izq, BorderLayout.WEST);

        Boton salir = new Boton("Salir", new Color(255, 255, 255, 32), new Color(255, 255, 255, 70), Color.WHITE);
        salir.setBorder(new EmptyBorder(9, 24, 9, 24));
        salir.addActionListener(e -> acciones.salir());
        JPanel der = new JPanel(new GridBagLayout());
        der.setOpaque(false);
        der.add(salir);
        header.add(der, BorderLayout.EAST);

        // Cuerpo
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(24, 40, 40, 40));

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setBackground(Estilo.FONDO);
        envoltorio.add(contenido, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(envoltorio);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Estilo.FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    /** Dibuja el contenido de la carpeta indicada. */
    public void mostrar(Carpeta actual, Carpeta raiz) {
        contenido.removeAll();
        contenido.add(migas(actual, raiz));
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(encabezado(actual));
        contenido.add(Box.createVerticalStrut(24));

        if (!actual.getHijos().isEmpty()) {
            contenido.add(tituloSeccion("Carpetas"));
            contenido.add(Box.createVerticalStrut(14));
            contenido.add(grilla(actual));
            contenido.add(Box.createVerticalStrut(30));
        }

        contenido.add(tituloSeccion("Ejercicios"));
        contenido.add(Box.createVerticalStrut(14));
        if (actual.getEjercicios().isEmpty()) {
            contenido.add(vacio());
        } else {
            for (Ejercicio e : actual.getEjercicios()) {
                contenido.add(new FilaEjercicio(e, acciones));
                contenido.add(Box.createVerticalStrut(12));
            }
        }
        contenido.revalidate();
        contenido.repaint();
    }

    private JLabel tituloSeccion(String t) {
        JLabel l = Estilo.etiqueta(t.toUpperCase(), 13, Font.BOLD, Estilo.GRIS);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JComponent migas(Carpeta actual, Carpeta raiz) {
        List<Carpeta> ruta = actual.getRuta();
        Ancho p = new Ancho(new FlowLayout(FlowLayout.LEFT, 8, 0));
        for (int i = 0; i < ruta.size(); i++) {
            Carpeta c = ruta.get(i);
            boolean ultima = i == ruta.size() - 1;
            String txt = (c == raiz) ? "Inicio" : c.getNombre();
            JLabel l = Estilo.etiqueta(txt, 13, ultima ? Font.BOLD : Font.PLAIN,
                    ultima ? Estilo.AZUL : Estilo.ROJO);
            if (!ultima) {
                l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                l.addMouseListener(new MouseAdapter() {
                    @Override public void mouseClicked(MouseEvent e) { acciones.abrirCarpeta(c); }
                });
            }
            p.add(l);
            if (!ultima) p.add(Estilo.etiqueta("›", 14, Font.PLAIN, Estilo.GRIS));
        }
        return p;
    }

    private JComponent encabezado(Carpeta actual) {
        Ancho p = new Ancho(new BorderLayout());
        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(Estilo.etiqueta(actual.getNombre(), 30, Font.BOLD, Estilo.AZUL));
        t.add(Box.createVerticalStrut(4));
        int tot = actual.total();
        t.add(Estilo.etiqueta(tot + (tot == 1 ? " ejercicio en total" : " ejercicios en total"),
                13, Font.PLAIN, Estilo.GRIS));
        p.add(t, BorderLayout.WEST);

        JPanel b = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        b.setOpaque(false);
        Boton nc = new Boton("+ Carpeta", Estilo.AZUL, Estilo.AZUL_OSC, Color.WHITE);
        Boton ne = new Boton("+ Ejercicio", Estilo.ROJO, Estilo.ROJO_OSC, Color.WHITE);
        nc.addActionListener(e -> acciones.nuevaCarpeta());
        ne.addActionListener(e -> acciones.nuevoEjercicio());
        b.add(nc);
        b.add(ne);
        JPanel c = new JPanel(new GridBagLayout());
        c.setOpaque(false);
        c.add(b);
        p.add(c, BorderLayout.EAST);
        return p;
    }

    private JComponent grilla(Carpeta actual) {
        Ancho g = new Ancho(new GridLayout(0, 3, 22, 22));
        for (Carpeta c : actual.getHijos()) g.add(new TarjetaCarpeta(c, acciones));
        int relleno = (3 - actual.getHijos().size() % 3) % 3;
        for (int i = 0; i < relleno; i++) {
            JPanel v = new JPanel();
            v.setOpaque(false);
            g.add(v);
        }
        return g;
    }

    private JComponent vacio() {
        Ancho p = new Ancho(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                Estilo.calidad(g);
                g.setColor(new Color(0xC9CEE0));
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f,
                        new float[]{7f, 7f}, 0f));
                g.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 22, 22));
                g.dispose();
            }
        };
        p.setPreferredSize(new Dimension(100, 100));
        p.add(Estilo.etiqueta("Aún no hay ejercicios aquí. Usa «+ Ejercicio» para agregar el primero.",
                14, Font.PLAIN, Estilo.GRIS));
        return p;
    }
}
