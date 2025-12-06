# BoletaMaster (Proyecto 3)

Guía rápida para compilar, ejecutar y probar las reglas nuevas de impresión/transferencia de tiquetes.

## Prerrequisitos
- Java 17+ en el PATH (`java -version` debe reportar 17 o superior).
- Los datos de ejemplo ya vienen en `data/` (usuarios, eventos, tiquetes, etc.).

Credenciales útiles del dataset de ejemplo:
- Administrador: **login** `ronny`, **password** `ronny`
- Clientes: `cli05` / `cli05`, `cli04` / `cli04` (ver `data/usuarios.json` para más cuentas)

## Compilar la aplicación (clases de producción)
```bash
mkdir -p bin
find src -name "*.java" ! -path "*/Tests/*" > sources.txt
javac -d bin @sources.txt
```

## Ejecutar las interfaces de consola
La aplicación carga los datos desde `data/` y persiste cambios al salir.

```bash
# Interfaz gráfica (Swing)
java -cp bin gui.BoletaMasterUI

# Cliente final (reventa, contraofertas, impresión de tiquetes en desarrollo futuro)
java -cp bin console.ClienteApp

# Organizador (creación de eventos/localidades)
java -cp bin console.OrganizadorApp

# Administrador (aprobar venues, cancelar ofertas del marketplace)
java -cp bin console.AdminApp
```

## Preparar y ejecutar las pruebas JUnit 5
El repositorio no incluye los *jars* de JUnit para ahorrar espacio. Usa los scripts para automatizar la descarga y ejecución:

```bash
# Descarga el runner de JUnit 5 (~6 MB) solo si falta
./scripts/descargar_junit.sh

# Compila todo (producción + tests) y ejecuta la suite del paquete Tests
./scripts/ejecutar_tests.sh
```

Si tu red bloquea la descarga directa (p. ej., proxy corporativo), copia manualmente el archivo `junit-platform-console-standalone-1.10.2.jar` a la carpeta `lib/` y luego ejecuta `./scripts/ejecutar_tests.sh` para compilar y correr las pruebas.

## Pruebas manuales rápidas de las nuevas reglas
1) Ingresa con `cli05` en `console.ClienteApp` y revisa **Mis tiquetes** (opción 1).
2) Intenta publicar una oferta (opción 2) con un tiquete ya impreso: la aplicación debe rechazarla con el mensaje
   *"El tiquete <id> ya fue impreso y no puede revenderse"*.
3) En un `jshell` rápido puedes validar la impresión única y bloqueo de transferencias:
   ```bash
   jshell --class-path bin
   jshell> import manager.*; import Cliente.*; import eventos.*; import tiquetes.*; import java.time.*; import java.util.*;
   jshell> var state = new BoletaMasterState();
   jshell> var vendedor = new Cliente("cli_demo", "1234", "Demo", 0.0, "CLI999");
   jshell> var comprador = new Cliente("cli_b", "1234", "Comprador", 0.0, "CLI998");
   jshell> var evento = new Evento(null, "E-DEMO", "Prueba", LocalDate.now().plusDays(10), LocalTime.NOON, "Programado", TipoEvento.CONCIERTO, null, null, null, new ArrayList<>());
   jshell> var loc = new Localidad(null, new ArrayList<>(), "General", 50000, false, 0);
   jshell> var t = new TiqueteBasico(vendedor, 5001, 50000, 0, 0, "Emitido", loc, evento, null, false);
   jshell> t.imprimir();   // primera impresión: OK
   jshell> t.imprimir();   // segunda impresión: lanza IllegalStateException
   jshell> vendedor.transferirTiquete(comprador, 5001, "1234"); // devuelve false por estar impreso
   ```

Así podrás comprobar el flujo de impresión y las restricciones de transferencia/reventa sin depender de datos externos.

## ¿Cómo probar todo esto en Eclipse?
1. **Importa el proyecto**: `File > Import > General > Existing Projects into Workspace`, elige la carpeta `TALLER_3` y finaliza.
2. **Marca las rutas correctas**:
   - En las propiedades del proyecto (`Right click > Properties > Java Build Path > Source`) asegúrate de que `src/` esté como *Source folder*.
   - En `Java Build Path > Libraries` agrega el *jar* de JUnit (descargado en `lib/junit-platform-console-standalone-1.10.2.jar`) con **Add External JARs…**. Si no quieres descargar nada todavía, omite este paso y solo podrás ejecutar las apps de consola.
   - En `Java Build Path > Libraries` agrega también `bin/` como *Class folder* si Eclipse no lo detecta automáticamente.
   - Si Eclipse te muestra un *error* por un jar inexistente (p. ej. un `json-20240303.jar` que apunta a tu PC local) elimínalo de la pestaña **Libraries** y luego ejecuta `Project > Clean`. Sin ese jar fantasma, la clase `gui.BoletaMasterUI` se compila y Eclipse la reconoce como entrada principal.
3. **Genera los `.class` en Eclipse**: clic derecho sobre el proyecto → **Build Project**. Eclipse compilará a `bin/` (o al output folder que definas).
4. **Ejecuta las apps de consola**: clic derecho sobre `console.ClienteApp` (o `console.OrganizadorApp`/`console.AdminApp`) → `Run As > Java Application`. Si Eclipse pregunta por la configuración principal, selecciona la clase correspondiente.
5. **Ejecuta los tests** (opcional): con el *jar* de JUnit en el *Build Path*, clic derecho sobre `src/Tests` → `Run As > JUnit Test`. Verás en la vista de JUnit que los casos `ImpresionYTransferenciaTest` pasan o fallan.
6. **Depuración rápida**: usa `Run > Debug As > Java Application` sobre las mismas clases; coloca *breakpoints* en métodos de `Tiquete` o `Cliente` para ver cómo se bloquean impresiones o transferencias tras el primer `imprimir()`.

Con esos pasos podrás revisar desde Eclipse los mismos flujos de impresión, transferencia y marketplace descritos más arriba.

### ¿Cómo abrir y probar la GUI (Swing) en Eclipse?
1. **Ubica la clase principal**: en `src/gui/BoletaMasterUI.java` hay un `main` listo para ejecutar.
2. **Confirma el *working directory***: en `Run > Run Configurations... > Java Application > (BoletaMasterUI)` establece el *Working directory* al directorio raíz del proyecto (`TALLER_3`). Esto asegura que la ruta relativa `data/` se resuelva y el estado se cargue/guarde.
3. **Ejecuta**: clic derecho sobre `BoletaMasterUI` → **Run As > Java Application**. Se abrirá la ventana Swing con la pantalla de login.
4. **Credenciales de ejemplo**:
   - Administrador: usuario `ronny`, contraseña `ronny`.
   - Organizadores: `org01`/`org01`, `org02`/`org02`, `org03`/`org03`.
   - Clientes: `cli01`/`cli01` ... `cli05`/`cli05`.
5. **Flujo de cierre**: al cerrar la ventana, el sistema invoca `guardarDatos()`; si hiciste pruebas que cambian el estado, los JSON en `data/` se actualizarán.

Con esa configuración podrás validar desde Eclipse toda la interfaz gráfica Swing, sin pasos adicionales.
