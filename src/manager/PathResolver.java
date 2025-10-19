package manager;

import java.nio.file.Path;

final class PathResolver {
  private PathResolver(){}
  static Path of(String s) { return Path.of(s); }
}
