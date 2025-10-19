package manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class JsonFiles {
  private JsonFiles(){}

  public static void ensureDir(Path dir) {
    try { Files.createDirectories(dir); }
    catch (IOException e) { throw new RuntimeException("No se pudo crear carpeta: "+dir, e); }
  }

  public static void write(Path file, String content) {
    try {
      Files.createDirectories(file.getParent());
      Files.writeString(file, content, StandardCharsets.UTF_8,
          StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("No se pudo escribir archivo: "+file, e);
    }
  }

  public static String read(Path file) {
    try { return Files.readString(file, StandardCharsets.UTF_8); }
    catch (IOException e) { throw new RuntimeException("No se pudo leer archivo: "+file, e); }
  }
}
