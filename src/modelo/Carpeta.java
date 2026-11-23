package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Carpeta del portafolio: puede contener subcarpetas y ejercicios. */
public class Carpeta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private Carpeta padre;
    private final List<Carpeta> hijos = new ArrayList<>();
    private final List<Ejercicio> ejercicios = new ArrayList<>();

    public Carpeta(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Carpeta getPadre() { return padre; }

    public List<Carpeta> getHijos() { return Collections.unmodifiableList(hijos); }
    public List<Ejercicio> getEjercicios() { return Collections.unmodifiableList(ejercicios); }

    public Carpeta agregarHijo(String nombre) {
        Carpeta c = new Carpeta(nombre);
        c.padre = this;
        hijos.add(c);
        return c;
    }

    public void eliminarHijo(Carpeta c) { hijos.remove(c); }
    public void agregarEjercicio(Ejercicio e) { ejercicios.add(e); }
    public void eliminarEjercicio(Ejercicio e) { ejercicios.remove(e); }

    /** Cantidad de ejercicios en esta carpeta y todas sus subcarpetas. */
    public int total() {
        int t = ejercicios.size();
        for (Carpeta h : hijos) t += h.total();
        return t;
    }

    /** Camino desde la raíz hasta esta carpeta (inclusive). */
    public List<Carpeta> getRuta() {
        List<Carpeta> ruta = new ArrayList<>();
        for (Carpeta c = this; c != null; c = c.padre) ruta.add(0, c);
        return ruta;
    }
}
