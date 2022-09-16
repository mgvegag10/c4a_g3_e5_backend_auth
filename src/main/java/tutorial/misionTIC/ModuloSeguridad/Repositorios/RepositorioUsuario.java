package tutorial.misionTIC.ModuloSeguridad.Repositorios;

import tutorial.misionTIC.ModuloSeguridad.Modelos.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioUsuario extends MongoRepository<Usuario,String> {
}

