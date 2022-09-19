package tutorial.misionTIC.ModuloSeguridad.Repositorios;

import tutorial.misionTIC.ModuloSeguridad.Modelos.Permiso;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RepositorioPermiso extends MongoRepository<Permiso,String> {
}
