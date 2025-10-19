package manager;

import java.util.List;
import Cliente.Administrador;
import Cliente.Cliente;
import Cliente.Organizador;

public interface IPersistenciaUsuarios {
  void cargarUsuarios(String archivo,
                      Administrador admin,
                      List<Cliente> clientes,
                      List<Organizador> organizadores);

  void salvarUsuarios(String archivo,
                      Administrador admin,
                      List<Cliente> clientes,
                      List<Organizador> organizadores);
}
