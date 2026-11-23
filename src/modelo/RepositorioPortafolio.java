package modelo;

import java.io.*;

/** Persistencia del árbol de carpetas en un archivo local. */
public class RepositorioPortafolio {
    private final File archivo;

    public RepositorioPortafolio(String ruta) { this.archivo = new File(ruta); }

    /** Devuelve la raíz guardada o null si no existe / no se puede leer. */
    public Carpeta cargar() {
        if (!archivo.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Carpeta) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public void guardar(Carpeta raiz) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(raiz);
        } catch (IOException ignored) { }
    }
}
