package vista;

import modelo.Carpeta;
import modelo.Ejercicio;

/** Eventos que la vista notifica al controlador. */
public interface VistaListener {
    void ingresar();
    void salir();
    void abrirCarpeta(Carpeta c);
    void nuevaCarpeta();
    void renombrarCarpeta(Carpeta c);
    void eliminarCarpeta(Carpeta c);
    void nuevoEjercicio();
    void eliminarEjercicio(Ejercicio e);
    void abrirArchivo(Ejercicio e);
}
