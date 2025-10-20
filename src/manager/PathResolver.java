package manager;

import java.nio.file.Path;
/**
 * Utilidad interna para resolver rutas de archivos.
 * <p>
 * Clase no instanciable; expone un método de ayuda para obtener {@link java.nio.file.Path}.
 */
final class PathResolver {
  private PathResolver(){}
  /**
   * Crea un {@link java.nio.file.Path} a partir de una cadena.
   *
   * @param s ruta en forma de cadena.
   * @return instancia de {@code Path} correspondiente.
   * @throws java.nio.file.InvalidPathException si la cadena no representa una ruta válida.
   */
  static Path of(String s) { return Path.of(s); }
}
