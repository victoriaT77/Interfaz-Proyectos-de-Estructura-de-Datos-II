package modelo;

/** Modelo principal: datos del curso y árbol de carpetas. */
public class Portafolio {
    public static final String INSTITUCION = "ESCUELA POLITÉCNICA NACIONAL";
    public static final String MATERIA = "ESTRUCTURA DE DATOS Y ALGORITMOS II";
    public static final String ESTUDIANTE = "VICTORIA TORRES";

    private final Carpeta raiz;

    public Portafolio(Carpeta raiz) { this.raiz = raiz; }

    public Carpeta getRaiz() { return raiz; }

    /** Estructura inicial según los temas de la clase. */
    public static Carpeta estructuraInicial() {
        Carpeta r = new Carpeta("Proyectos del semestre");
        r.agregarHijo("Eficiencia Algorítmica");
        r.agregarHijo("Análisis de Complejidad");
        r.agregarHijo("P · NP · NP-Hard · NP-Completo");
        Carpeta g = r.agregarHijo("Grafos");
        g.agregarHijo("Orden Topológico");
        g.agregarHijo("TSP – Viajante");
        g.agregarHijo("Prim");
        g.agregarHijo("Kahn");
        g.agregarHijo("Kruskal");
        g.agregarHijo("Dijkstra");
        return r;
    }
}
