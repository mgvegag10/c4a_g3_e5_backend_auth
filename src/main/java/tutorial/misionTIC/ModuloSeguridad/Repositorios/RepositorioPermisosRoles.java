package tutorial.misionTIC.ModuloSeguridad.Repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import tutorial.misionTIC.ModuloSeguridad.Modelos.PermisosRoles;


public interface RepositorioPermisosRoles extends MongoRepository<PermisosRoles,String> {
}
