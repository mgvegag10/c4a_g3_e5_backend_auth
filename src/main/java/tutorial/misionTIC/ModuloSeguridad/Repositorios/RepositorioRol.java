package tutorial.misionTIC.ModuloSeguridad.Repositorios;

import tutorial.misionTIC.ModuloSeguridad.Modelos.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RepositorioRol extends MongoRepository<Rol,String> {
}
