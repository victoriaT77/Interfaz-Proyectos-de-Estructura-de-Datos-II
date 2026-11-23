package app;

import controlador.PortafolioControlador;
import modelo.Carpeta;
import modelo.Portafolio;
import modelo.RepositorioPortafolio;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RepositorioPortafolio repo = new RepositorioPortafolio("sidedaii.dat");
            Carpeta raiz = repo.cargar();
            if (raiz == null) raiz = Portafolio.estructuraInicial();
            new PortafolioControlador(new Portafolio(raiz), repo).iniciar();
        });
    }
}
