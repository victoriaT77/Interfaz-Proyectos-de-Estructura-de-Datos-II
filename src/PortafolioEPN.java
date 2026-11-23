import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Portafolio de proyectos - Escuela Politécnica Nacional
 * Estructura de Datos y Algoritmos II - Victoria Torres
 *
 * Compilar:  javac PortafolioEPN.java
 * Ejecutar:  java PortafolioEPN
 * (el archivo logo.png debe estar en la misma carpeta)
 */
public class PortafolioEPN extends JFrame {

    // ===================== Paleta EPN =====================
    static final Color ROJO     = new Color(0xC8102E);
    static final Color ROJO_OSC = new Color(0xA00C24);
    static final Color AZUL     = new Color(0x14235A);
    static final Color AZUL_OSC = new Color(0x0A1440);
    static final Color DORADO   = new Color(0xF2A900);
    static final Color DORADO_C = new Color(0xFFCB4D);
    static final Color FONDO    = new Color(0xF3F4F9);
    static final Color GRIS     = new Color(0x5A6385);

    static final String DATOS = "portafolio.dat";
    static final BufferedImage LOGO = cargarLogo();

    static BufferedImage cargarLogo() {
        try {
            java.net.URL u = PortafolioEPN.class.getResource("/logo.png");
            if (u != null) return ImageIO.read(u);
            File f = new File("logo.png");
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) { }
        return null;
    }

    // ===================== Modelo =====================
    static class Ejercicio implements Serializable {
        String titulo, descripcion;
        File archivo;
        Ejercicio(String t, String d, File a) { titulo = t; descripcion = d; archivo = a; }
    }

    static class Carpeta implements Serializable {
        String nombre;
        Carpeta padre;
        List<Carpeta> hijos = new ArrayList<>();
        List<Ejercicio> ejercicios = new ArrayList<>();
        Carpeta(String n) { nombre = n; }
        Carpeta add(String n) { Carpeta c = new Carpeta(n); c.padre = this; hijos.add(c); return c; }
        int total() {
            int t = ejercicios.size();
            for (Carpeta h : hijos) t += h.total();
            return t;
        }
    }

    static Carpeta crearEstructura() {
        Carpeta r = new Carpeta("Proyectos del semestre");
        r.add("Eficiencia Algorítmica");
        r.add("Análisis de Complejidad");
        r.add("P · NP · NP-Hard · NP-Completo");
        Carpeta g = r.add("Grafos");
        g.add("Orden Topológico");
        g.add("TSP – Viajante");
        g.add("Prim");
        g.add("Kahn");
        g.add("Kruskal");
        g.add("Dijkstra");
        return r;
    }

    static Carpeta cargar() {
        File f = new File(DATOS);
        if (f.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
                return (Carpeta) in.readObject();
            } catch (Exception ignored) { }
        }
        return crearEstructura();
    }

    void guardar() {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(DATOS))) {
            o.writeObject(raiz);
        } catch (Exception ignored) { }
    }

    // ===================== Utilidades de dibujo =====================
    static void calidad(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    static void sombra(Graphics2D g, int x, int y, int w, int h, int arc, int capas) {
        for (int i = capas; i > 0; i--) {
            g.setColor(new Color(10, 20, 60, 9));
            g.fill(new RoundRectangle2D.Float(x - i + 1, y - i + 5, w + 2 * i, h + 2 * i, arc + i, arc + i));
        }
    }

    static List<String> envolver(String t, FontMetrics fm, int w, int max) {
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

    static String esc(String s) { return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }

    // ===================== Componentes propios =====================
    static class LogoView extends JComponent {
        final int h; final boolean chip;
        LogoView(int h, boolean chip) { this.h = h; this.chip = chip; setOpaque(false); }
        int pad() { return chip ? 6 : 0; }
        double ratio() { return LOGO == null ? 1.0 : (double) LOGO.getWidth() / LOGO.getHeight(); }
        @Override public Dimension getPreferredSize() {
            return new Dimension((int) (h * ratio()) + 2 * pad(), h + 2 * pad());
        }
        @Override public Dimension getMinimumSize() { return getPreferredSize(); }
        @Override public Dimension getMaximumSize() { return getPreferredSize(); }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            calidad(g);
            if (chip) {
                g.setColor(Color.WHITE);
                g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
            }
            if (LOGO != null) {
                g.drawImage(LOGO, pad(), pad(), (int) (h * ratio()), h, null);
            } else {
                g.setColor(ROJO);
                g.fillRoundRect(pad(), pad(), h, h, 16, 16);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Segoe UI", Font.BOLD, h / 3));
                FontMetrics fm = g.getFontMetrics();
                g.drawString("EPN", pad() + (h - fm.stringWidth("EPN")) / 2, pad() + h / 2 + fm.getAscent() / 3);
            }
            g.dispose();
        }
    }

    static class Boton extends JButton {
        final Color base, hov; boolean over;
        Boton(String t, Color base, Color hov, Color fg) {
            super(t);
            this.base = base; this.hov = hov;
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(11, 28, 11, 28));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { over = true; repaint(); }
                public void mouseExited(MouseEvent e) { over = false; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            calidad(g);
            g.setColor(over ? hov : base);
            g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
            g.dispose();
            super.paintComponent(g0);
        }
    }

    /** Panel que se estira a lo ancho dentro de un BoxLayout vertical. */
    static class Ancho extends JPanel {
        Ancho(LayoutManager l) { super(l); setOpaque(false); setAlignmentX(LEFT_ALIGNMENT); }
        @Override public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }
    }

    // ===================== Estado =====================
    final CardLayout cartas = new CardLayout();
    final JPanel raizUI = new JPanel(cartas);
    final JPanel contenido = new JPanel();
    Carpeta raiz = cargar();
    Carpeta actual = raiz;

    // ===================== Ventana =====================
    public PortafolioEPN() {
        setTitle("Portafolio · Escuela Politécnica Nacional");
        if (LOGO != null) setIconImage(LOGO);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1140, 760);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);

        raizUI.add(crearLogin(), "login");
        raizUI.add(crearDashboard(), "dash");
        setContentPane(raizUI);
        cartas.show(raizUI, "login");
    }

    // ===================== Pantalla de ingreso =====================
    JPanel crearLogin() {
        JPanel fondo = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                calidad(g);
                int w = getWidth(), h = getHeight();
                g.setPaint(new GradientPaint(0, 0, AZUL_OSC, w, h, AZUL));
                g.fillRect(0, 0, w, h);
                g.setColor(new Color(242, 169, 0, 28));
                g.fillOval(-120, h - 360, 520, 520);
                g.setColor(new Color(200, 16, 46, 40));
                g.fillOval(w - 380, -180, 560, 560);
                g.setColor(new Color(255, 255, 255, 10));
                g.fillOval(w - 220, h - 240, 340, 340);
                g.setColor(ROJO);
                g.fillRect(0, h - 10, w, 6);
                g.setColor(DORADO);
                g.fillRect(0, h - 4, w, 4);
                g.dispose();
            }
        };

        JPanel tarjeta = new JPanel() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                calidad(g);
                int x = 20, y = 20, w = getWidth() - 40, h = getHeight() - 40;
                sombra(g, x, y, w, h, 30, 14);
                RoundRectangle2D rr = new RoundRectangle2D.Float(x, y, w, h, 30, 30);
                g.setColor(Color.WHITE);
                g.fill(rr);
                g.setClip(rr);
                g.setColor(ROJO);
                g.fillRect(x, y, w, 9);
                g.setColor(DORADO);
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

        JLabel l1 = etiqueta("ESCUELA POLITÉCNICA NACIONAL", 22, Font.BOLD, AZUL);
        JLabel l2 = etiqueta("ESTRUCTURA DE DATOS Y ALGORITMOS II", 15, Font.PLAIN, GRIS);
        JLabel l3 = etiqueta("VICTORIA TORRES", 18, Font.BOLD, ROJO);
        for (JLabel l : new JLabel[]{l1, l2, l3}) l.setAlignmentX(CENTER_ALIGNMENT);

        JPanel barra = new JPanel();
        barra.setBackground(DORADO);
        Dimension d = new Dimension(64, 4);
        barra.setPreferredSize(d); barra.setMaximumSize(d); barra.setMinimumSize(d);
        barra.setAlignmentX(CENTER_ALIGNMENT);

        Boton ingresar = new Boton("INGRESAR", ROJO, ROJO_OSC, Color.WHITE);
        ingresar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ingresar.setBorder(new EmptyBorder(14, 56, 14, 56));
        ingresar.setAlignmentX(CENTER_ALIGNMENT);
        ingresar.addActionListener(e -> { abrir(raiz); cartas.show(raizUI, "dash"); });

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

        fondo.add(tarjeta);
        return fondo;
    }

    static JLabel etiqueta(String t, int tam, int estilo, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", estilo, tam));
        l.setForeground(c);
        return l;
    }

    // ===================== Dashboard =====================
    JPanel crearDashboard() {
        JPanel dash = new JPanel(new BorderLayout());

        // Encabezado
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                int w = getWidth(), h = getHeight();
                g.setPaint(new GradientPaint(0, 0, AZUL_OSC, w, 0, AZUL));
                g.fillRect(0, 0, w, h);
                g.setColor(ROJO);
                g.fillRect(0, h - 6, w, 4);
                g.setColor(DORADO);
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
        textos.add(etiqueta("ESTRUCTURA DE DATOS Y ALGORITMOS II", 18, Font.BOLD, Color.WHITE));
        textos.add(Box.createVerticalStrut(3));
        textos.add(etiqueta("Escuela Politécnica Nacional  ·  Victoria Torres", 12, Font.PLAIN, DORADO_C));
        izq.add(textos);
        header.add(izq, BorderLayout.WEST);

        Boton salir = new Boton("Salir", new Color(255, 255, 255, 32), new Color(255, 255, 255, 70), Color.WHITE);
        salir.setBorder(new EmptyBorder(9, 24, 9, 24));
        salir.addActionListener(e -> cartas.show(raizUI, "login"));
        JPanel der = new JPanel(new GridBagLayout());
        der.setOpaque(false);
        der.add(salir);
        header.add(der, BorderLayout.EAST);

        // Cuerpo
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(24, 40, 40, 40));

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setBackground(FONDO);
        envoltorio.add(contenido, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(envoltorio);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        dash.add(header, BorderLayout.NORTH);
        dash.add(scroll, BorderLayout.CENTER);
        return dash;
    }

    void abrir(Carpeta c) { actual = c; refrescar(); }

    void refrescar() {
        contenido.removeAll();
        contenido.add(migas());
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(encabezado());
        contenido.add(Box.createVerticalStrut(24));

        if (!actual.hijos.isEmpty()) {
            contenido.add(tituloSeccion("Carpetas"));
            contenido.add(Box.createVerticalStrut(14));
            contenido.add(grilla());
            contenido.add(Box.createVerticalStrut(30));
        }

        contenido.add(tituloSeccion("Ejercicios"));
        contenido.add(Box.createVerticalStrut(14));
        if (actual.ejercicios.isEmpty()) {
            contenido.add(vacio());
        } else {
            for (Ejercicio e : actual.ejercicios) {
                contenido.add(new FilaEjercicio(e));
                contenido.add(Box.createVerticalStrut(12));
            }
        }
        contenido.revalidate();
        contenido.repaint();
        guardar();
    }

    JLabel tituloSeccion(String t) {
        JLabel l = etiqueta(t.toUpperCase(), 13, Font.BOLD, GRIS);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    JComponent migas() {
        List<Carpeta> ruta = new ArrayList<>();
        for (Carpeta c = actual; c != null; c = c.padre) ruta.add(0, c);
        Ancho p = new Ancho(new FlowLayout(FlowLayout.LEFT, 8, 0));
        for (int i = 0; i < ruta.size(); i++) {
            Carpeta c = ruta.get(i);
            String txt = (c == raiz) ? "Inicio" : c.nombre;
            JLabel l = etiqueta(txt, 13, i == ruta.size() - 1 ? Font.BOLD : Font.PLAIN,
                    i == ruta.size() - 1 ? AZUL : ROJO);
            if (i < ruta.size() - 1) {
                l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                l.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) { abrir(c); }
                });
            }
            p.add(l);
            if (i < ruta.size() - 1) p.add(etiqueta("›", 14, Font.PLAIN, GRIS));
        }
        return p;
    }

    JComponent encabezado() {
        Ancho p = new Ancho(new BorderLayout());
        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(etiqueta(actual.nombre, 30, Font.BOLD, AZUL));
        t.add(Box.createVerticalStrut(4));
        int tot = actual.total();
        t.add(etiqueta(tot + (tot == 1 ? " ejercicio en total" : " ejercicios en total"), 13, Font.PLAIN, GRIS));
        p.add(t, BorderLayout.WEST);

        JPanel b = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        b.setOpaque(false);
        Boton nc = new Boton("+ Carpeta", AZUL, AZUL_OSC, Color.WHITE);
        Boton ne = new Boton("+ Ejercicio", ROJO, ROJO_OSC, Color.WHITE);
        nc.addActionListener(e -> nuevaCarpeta());
        ne.addActionListener(e -> dialogoEjercicio());
        b.add(nc); b.add(ne);
        JPanel c = new JPanel(new GridBagLayout());
        c.setOpaque(false);
        c.add(b);
        p.add(c, BorderLayout.EAST);
        return p;
    }

    JComponent grilla() {
        Ancho g = new Ancho(new GridLayout(0, 3, 22, 22));
        for (Carpeta c : actual.hijos) g.add(new TarjetaCarpeta(c));
        int relleno = (3 - actual.hijos.size() % 3) % 3;
        for (int i = 0; i < relleno; i++) { JPanel v = new JPanel(); v.setOpaque(false); g.add(v); }
        return g;
    }

    JComponent vacio() {
        Ancho p = new Ancho(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                calidad(g);
                g.setColor(new Color(0xC9CEE0));
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{7f, 7f}, 0f));
                g.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 22, 22));
                g.dispose();
            }
        };
        p.setPreferredSize(new Dimension(100, 100));
        p.add(etiqueta("Aún no hay ejercicios aquí. Usa «+ Ejercicio» para agregar el primero.", 14, Font.PLAIN, GRIS));
        return p;
    }

    // ===================== Acciones =====================
    void nuevaCarpeta() {
        String n = JOptionPane.showInputDialog(this, "Nombre de la carpeta:", "Nueva carpeta", JOptionPane.PLAIN_MESSAGE);
        if (n != null && !n.trim().isEmpty()) { actual.add(n.trim()); refrescar(); }
    }

    void renombrar(Carpeta c) {
        String n = (String) JOptionPane.showInputDialog(this, "Nuevo nombre:", "Renombrar",
                JOptionPane.PLAIN_MESSAGE, null, null, c.nombre);
        if (n != null && !n.trim().isEmpty()) { c.nombre = n.trim(); refrescar(); }
    }

    void eliminar(Carpeta c) {
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la carpeta «" + c.nombre + "» y todo su contenido?", "Eliminar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) { actual.hijos.remove(c); refrescar(); }
    }

    void dialogoEjercicio() {
        JTextField titulo = new JTextField(28);
        JTextArea desc = new JTextArea(4, 28);
        desc.setLineWrap(true); desc.setWrapStyleWord(true);
        JLabel arch = new JLabel("Sin archivo adjunto");
        File[] sel = {null};
        JButton adj = new JButton("Adjuntar archivo…");
        adj.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                sel[0] = fc.getSelectedFile();
                arch.setText(sel[0].getName());
            }
        });

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 0, 3, 0);
        g.gridy = 0; p.add(new JLabel("Título del ejercicio:"), g);
        g.gridy = 1; p.add(titulo, g);
        g.gridy = 2; p.add(new JLabel("Descripción:"), g);
        g.gridy = 3; p.add(new JScrollPane(desc), g);
        g.gridy = 4; p.add(adj, g);
        g.gridy = 5; p.add(arch, g);

        int r = JOptionPane.showConfirmDialog(this, p, "Nuevo ejercicio en «" + actual.nombre + "»",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION && !titulo.getText().trim().isEmpty()) {
            actual.ejercicios.add(new Ejercicio(titulo.getText().trim(), desc.getText().trim(), sel[0]));
            refrescar();
        }
    }

    // ===================== Tarjeta de carpeta =====================
    class TarjetaCarpeta extends JPanel {
        final Carpeta c;
        boolean hover;

        TarjetaCarpeta(Carpeta c) {
            this.c = c;
            setOpaque(false);
            setPreferredSize(new Dimension(260, 185));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPopupMenu menu = new JPopupMenu();
            JMenuItem ren = new JMenuItem("Renombrar");
            JMenuItem del = new JMenuItem("Eliminar");
            ren.addActionListener(e -> renombrar(c));
            del.addActionListener(e -> eliminar(c));
            menu.add(ren); menu.add(del);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) menu.show(TarjetaCarpeta.this, e.getX(), e.getY()); }
                public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) menu.show(TarjetaCarpeta.this, e.getX(), e.getY()); }
                public void mouseClicked(MouseEvent e) { if (SwingUtilities.isLeftMouseButton(e)) abrir(c); }
            });
        }

        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            calidad(g);
            int w = getWidth() - 8, h = getHeight() - 10;
            sombra(g, 0, 0, w, h, 24, hover ? 12 : 6);
            RoundRectangle2D card = new RoundRectangle2D.Float(0, 0, w, h, 24, 24);
            g.setColor(Color.WHITE);
            g.fill(card);
            if (hover) {
                g.setColor(ROJO);
                g.setStroke(new BasicStroke(2f));
                g.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 24, 24));
            }

            // Icono de carpeta
            int ix = 24, iy = 24;
            g.setColor(new Color(0xE09A00));
            g.fill(new RoundRectangle2D.Float(ix, iy, 36, 16, 8, 8));
            g.setPaint(new GradientPaint(0, iy + 10, DORADO_C, 0, iy + 56, DORADO));
            g.fill(new RoundRectangle2D.Float(ix, iy + 8, 70, 50, 12, 12));
            g.setColor(new Color(255, 255, 255, 70));
            g.fill(new RoundRectangle2D.Float(ix, iy + 18, 70, 40, 12, 12));

            // Insignia con cantidad
            int n = c.total();
            String ns = String.valueOf(n);
            g.setFont(new Font("Segoe UI", Font.BOLD, 13));
            FontMetrics fb = g.getFontMetrics();
            int bw = Math.max(28, fb.stringWidth(ns) + 18);
            int bx = w - bw - 20, by = 24;
            g.setColor(n > 0 ? ROJO : new Color(0xD5D9E8));
            g.fill(new RoundRectangle2D.Float(bx, by, bw, 26, 26, 26));
            g.setColor(n > 0 ? Color.WHITE : GRIS);
            g.drawString(ns, bx + (bw - fb.stringWidth(ns)) / 2, by + 18);

            // Nombre
            g.setFont(new Font("Segoe UI", Font.BOLD, 17));
            g.setColor(AZUL);
            FontMetrics fm = g.getFontMetrics();
            int y = 116;
            for (String l : envolver(c.nombre, fm, w - 44, 2)) {
                g.drawString(l, 24, y);
                y += fm.getHeight() - 2;
            }

            // Detalle
            String det = (c.hijos.isEmpty() ? "" : c.hijos.size() + (c.hijos.size() == 1 ? " subcarpeta · " : " subcarpetas · "))
                    + n + (n == 1 ? " ejercicio" : " ejercicios");
            g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g.setColor(GRIS);
            g.drawString(det, 24, h - 18);
            g.dispose();
        }
    }

    // ===================== Fila de ejercicio =====================
    class FilaEjercicio extends Ancho {
        FilaEjercicio(Ejercicio e) {
            super(new BorderLayout(14, 0));
            setBorder(new EmptyBorder(16, 26, 16, 18));

            JPanel textos = new JPanel();
            textos.setOpaque(false);
            textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
            textos.add(etiqueta(e.titulo, 16, Font.BOLD, AZUL));
            if (e.descripcion != null && !e.descripcion.isEmpty()) {
                textos.add(Box.createVerticalStrut(4));
                JLabel d = new JLabel("<html><body style='width:520px'>" + esc(e.descripcion).replace("\n", "<br>") + "</body></html>");
                d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                d.setForeground(GRIS);
                textos.add(d);
            }
            add(textos, BorderLayout.CENTER);

            JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            acc.setOpaque(false);
            if (e.archivo != null) {
                Boton ab = new Boton("Abrir", AZUL, AZUL_OSC, Color.WHITE);
                ab.setBorder(new EmptyBorder(7, 18, 7, 18));
                ab.setFont(new Font("Segoe UI", Font.BOLD, 12));
                ab.addActionListener(ev -> {
                    try { Desktop.getDesktop().open(e.archivo); }
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "No se pudo abrir:\n" + e.archivo, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                acc.add(ab);
            }
            Boton x = new Boton("✕", new Color(0xEEF0F7), new Color(0xF9D6DC), ROJO);
            x.setBorder(new EmptyBorder(7, 12, 7, 12));
            x.addActionListener(ev -> {
                if (JOptionPane.showConfirmDialog(this, "¿Eliminar «" + e.titulo + "»?", "Eliminar",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    actual.ejercicios.remove(e);
                    refrescar();
                }
            });
            acc.add(x);
            JPanel c = new JPanel(new GridBagLayout());
            c.setOpaque(false);
            c.add(acc);
            add(c, BorderLayout.EAST);
        }

        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            calidad(g);
            int w = getWidth() - 4, h = getHeight() - 6;
            sombra(g, 0, 0, w, h, 18, 5);
            RoundRectangle2D rr = new RoundRectangle2D.Float(0, 0, w, h, 18, 18);
            g.setColor(Color.WHITE);
            g.fill(rr);
            g.setClip(rr);
            g.setColor(DORADO);
            g.fillRect(0, 0, 7, h);
            g.dispose();
        }
    }

    // ===================== Main =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PortafolioEPN().setVisible(true));
    }
}
