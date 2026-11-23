# SIDEDAII — Portafolio de proyectos (EPN)

Arquitectura MVC en Java Swing.

```
SIDEDAII/
├── logo.png
└── src/
    ├── app/          Main.java
    ├── modelo/       Carpeta, Ejercicio, Portafolio, RepositorioPortafolio
    ├── vista/        VentanaPrincipal, PanelLogin, PanelDashboard, TarjetaCarpeta,
    │                 FilaEjercicio, DialogoEjercicio, Boton, LogoView, Ancho, Estilo,
    │                 VistaListener (interfaz de eventos)
    └── controlador/  PortafolioControlador
```

## Compilar y ejecutar (desde esta carpeta)

Linux / macOS:
    javac -d out $(find src -name "*.java")
    java -cp out app.Main

Windows (PowerShell):
    javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
    java -cp out app.Main

Los datos se guardan en `sidedaii.dat` (se crea al ejecutar).
