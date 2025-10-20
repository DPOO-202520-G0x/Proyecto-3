package manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
/**
 * Utilidades mínimas para manejo de archivos JSON (crear carpetas, leer y escribir).
 * <p>
 * Clase de solo métodos estáticos; no instanciable.
 */
public final class JsonFiles {
  private JsonFiles(){}
  /**
   * Asegura la existencia de un directorio; lo crea si no existe.
   *
   * @param dir ruta del directorio a verificar/crear.
   * @throws RuntimeException si ocurre un error al crear el directorio.
   */
  public static void ensureDir(Path dir) {
    try { Files.createDirectories(dir); }
    catch (IOException e) { throw new RuntimeException("No se pudo crear carpeta: "+dir, e); }
  }
  /**
   * Escribe contenido de texto en un archivo UTF-8, creando carpetas si es necesario.
   * Sobrescribe el archivo si ya existe.
   *
   * @param file    ruta del archivo destino.
   * @param content contenido textual a escribir.
   * @throws RuntimeException si falla la escritura o la creación de carpetas.
   */
  public static void write(Path file, String content) {
    try {
      Files.createDirectories(file.getParent());
      Files.writeString(file, content, StandardCharsets.UTF_8,
          StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("No se pudo escribir archivo: "+file, e);
    }
  }
  /**
   * Lee el contenido completo de un archivo como texto UTF-8.
   *
   * @param file ruta del archivo a leer.
   * @return contenido textual del archivo.
   * @throws RuntimeException si falla la lectura.
   */
  public static String read(Path file) {
    try { return Files.readString(file, StandardCharsets.UTF_8); }
    catch (IOException e) { throw new RuntimeException("No se pudo leer archivo: "+file, e); }
  }
}
