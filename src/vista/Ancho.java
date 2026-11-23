package vista;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.LayoutManager;

/** Panel transparente que se estira a lo ancho dentro de un BoxLayout vertical. */
public class Ancho extends JPanel {
    public Ancho(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setAlignmentX(LEFT_ALIGNMENT);
    }

    @Override public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
