package vista;

import modelo.Ejercicio;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/** Diálogo para capturar un nuevo ejercicio. */
public final class DialogoEjercicio {
    private DialogoEjercicio() { }

    /** Devuelve el ejercicio creado o null si se canceló. */
    public static Ejercicio pedir(Component padre, String carpeta) {
        JTextField titulo = new JTextField(28);
        JTextArea desc = new JTextArea(4, 28);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        JLabel arch = new JLabel("Sin archivo adjunto");
        File[] sel = {null};

        JButton adj = new JButton("Adjuntar archivo…");
        adj.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
                sel[0] = fc.getSelectedFile();
                arch.setText(sel[0].getName());
            }
        });

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 0, 3, 0);
        g.gridy = 0; p.add(new JLabel("Título del ejercicio:"), g);
        g.gridy = 1; p.add(titulo, g);
        g.gridy = 2; p.add(new JLabel("Descripción:"), g);
        g.gridy = 3; p.add(new JScrollPane(desc), g);
        g.gridy = 4; p.add(adj, g);
        g.gridy = 5; p.add(arch, g);

        int r = JOptionPane.showConfirmDialog(padre, p, "Nuevo ejercicio en «" + carpeta + "»",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r == JOptionPane.OK_OPTION && !titulo.getText().trim().isEmpty()) {
            return new Ejercicio(titulo.getText().trim(), desc.getText().trim(), sel[0]);
        }
        return null;
    }
}
