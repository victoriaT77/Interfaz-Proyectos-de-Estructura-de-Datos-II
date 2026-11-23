package vista;

import modelo.Carpeta;
import modelo.Ejercicio;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Dimension;

/** Ventana principal (vista). No contiene lógica: solo muestra y notifica al controlador. */
public class VentanaPrincipal extends JFrame {
    public static final String TITULO = "SIDEDAII";

    private final CardLayout cartas = new CardLayout();
    private final JPanel raiz = new JPanel(cartas);
    private final PanelDashboard dashboard;

    public VentanaPrincipal(VistaListener acciones, String institucion, String materia, String estudiante) {
        super(TITULO);
        if (Estilo.LOGO != null) setIconImage(Estilo.LOGO);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1140, 760);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);

        dashboard = new PanelDashboard(acciones, institucion, materia, estudiante);
        raiz.add(new PanelLogin(acciones, institucion, materia, estudiante), "login");
        raiz.add(dashboard, "dash");
        setContentPane(raiz);
    }

    public void mostrarLogin() { cartas.show(raiz, "login"); }
    public void mostrarDashboard() { cartas.show(raiz, "dash"); }
    public void mostrarCarpeta(Carpeta actual, Carpeta raizModelo) { dashboard.mostrar(actual, raizModelo); }

    // ---------- Diálogos ----------
    public String pedirTexto(String titulo, String mensaje, String valorInicial) {
        return (String) JOptionPane.showInputDialog(this, mensaje, titulo,
                JOptionPane.PLAIN_MESSAGE, null, null, valorInicial);
    }

    public Ejercicio pedirEjercicio(String nombreCarpeta) {
        return DialogoEjercicio.pedir(this, nombreCarpeta);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
