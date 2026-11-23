package modelo;

import java.io.File;
import java.io.Serializable;

/** Un ejercicio/proyecto guardado dentro de una carpeta. */
public class Ejercicio implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String titulo;
    private final String descripcion;
    private final File archivo;

    public Ejercicio(String titulo, String descripcion, File archivo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.archivo = archivo;
    }

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public File getArchivo() { return archivo; }
}
