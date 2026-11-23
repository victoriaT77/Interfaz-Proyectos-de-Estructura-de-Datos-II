package controlador;

import modelo.*;
import vista.VentanaPrincipal;
import vista.VistaListener;

import java.awt.Desktop;

/** Controlador: recibe los eventos de la vista, modifica el modelo y actualiza la vista. */
public class PortafolioControlador implements VistaListener {
    private final Portafolio modelo;
    private final RepositorioPortafolio repositorio;
    private final VentanaPrincipal vista;
    private Carpeta actual;

    public PortafolioControlador(Portafolio modelo, RepositorioPortafolio repositorio) {
        this.modelo = modelo;
        this.repositorio = repositorio;
        this.actual = modelo.getRaiz();
        this.vista = new VentanaPrincipal(this,
                Portafolio.INSTITUCION, Portafolio.MATERIA, Portafolio.ESTUDIANTE);
    }

    public void iniciar() {
        vista.mostrarLogin();
        vista.setVisible(true);
    }

    private void refrescar() {
        vista.mostrarCarpeta(actual, modelo.getRaiz());
        repositorio.guardar(modelo.getRaiz());
    }

    // ---------- Eventos de la vista ----------
    @Override public void ingresar() {
        actual = modelo.getRaiz();
        refrescar();
        vista.mostrarDashboard();
    }

    @Override public void salir() { vista.mostrarLogin(); }

    @Override public void abrirCarpeta(Carpeta c) { actual = c; refrescar(); }

    @Override public void nuevaCarpeta() {
        String n = vista.pedirTexto("Nueva carpeta", "Nombre de la carpeta:", "");
        if (n != null && !n.trim().isEmpty()) {
            actual.agregarHijo(n.trim());
            refrescar();
        }
    }

    @Override public void renombrarCarpeta(Carpeta c) {
        String n = vista.pedirTexto("Renombrar", "Nuevo nombre:", c.getNombre());
        if (n != null && !n.trim().isEmpty()) {
            c.setNombre(n.trim());
            refrescar();
        }
    }

    @Override public void eliminarCarpeta(Carpeta c) {
        if (vista.confirmar("¿Eliminar la carpeta «" + c.getNombre() + "» y todo su contenido?")) {
            actual.eliminarHijo(c);
            refrescar();
        }
    }

    @Override public void nuevoEjercicio() {
        Ejercicio e = vista.pedirEjercicio(actual.getNombre());
        if (e != null) {
            actual.agregarEjercicio(e);
            refrescar();
        }
    }

    @Override public void eliminarEjercicio(Ejercicio e) {
        if (vista.confirmar("¿Eliminar «" + e.getTitulo() + "»?")) {
            actual.eliminarEjercicio(e);
            refrescar();
        }
    }

    @Override public void abrirArchivo(Ejercicio e) {
        try {
            Desktop.getDesktop().open(e.getArchivo());
        } catch (Exception ex) {
            vista.mostrarError("No se pudo abrir:\n" + e.getArchivo());
        }
    }
}
