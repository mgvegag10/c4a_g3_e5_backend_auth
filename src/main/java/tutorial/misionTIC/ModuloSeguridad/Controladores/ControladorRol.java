package tutorial.misionTIC.ModuloSeguridad.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Permiso;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Rol;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Usuario;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioRol;
import org.springframework.web.server.ResponseStatusException;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioUsuario;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class ControladorRol {
    @Autowired
    private RepositorioRol miRepositorioRol;

    @Autowired
    private RepositorioUsuario miRepositorioUsuario;

    @GetMapping("")
    public List<Rol> index(){
        List<Rol> roles =this.miRepositorioRol.findAll();
        if(roles.isEmpty())
            throw new ResponseStatusException(HttpStatus.ACCEPTED,"No existen roles registrados");
        return roles;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Rol create(@RequestBody  Rol infoRol){
        if(infoRol.getDescripcion() == null &&
                infoRol.getNombre()== null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos por ser enviados en el body");

        List<Rol> roles = this.miRepositorioRol.findAll();

        roles.forEach((n)->{
            if(n.getNombre().equals(infoRol.getNombre()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Este rol ya existe. Ingrese uno diferente");
        });

        return this.miRepositorioRol.save(infoRol);
    }

    @GetMapping("{id}")
    public Rol show(@PathVariable String id){
        Rol rolActual=this.miRepositorioRol
                .findById(id)
                .orElse(null);
        if(rolActual == null)
            throw new ResponseStatusException(HttpStatus.ACCEPTED,"El rol solicitado no existe");
        return rolActual;
    }
    @PutMapping("{id}")
    public Rol update(@PathVariable String id,@RequestBody  Rol infoRol){
        Rol rolActual=this.miRepositorioRol
                .findById(id)
                .orElse(null);
        if(rolActual == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El rol solicitado no existe");

        if (infoRol.getNombre()==null || infoRol.getDescripcion()==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El rol solicitado no existe");

        List<Rol> roles = this.miRepositorioRol.findAll();

        roles.forEach((n)->{
            if(n.getNombre().equals(infoRol.getNombre()) && !rolActual.get_id().equals(n.get_id()) )
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Este nombre de rol ya existe. Ingrese uno diferente");
        });

        rolActual.setDescripcion(infoRol.getDescripcion());
        rolActual.setNombre(infoRol.getNombre());
        return this.miRepositorioRol.save(rolActual);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Rol rolActual=this.miRepositorioRol
                .findById(id)
                .orElse(null);

        List<Usuario> usuarios = this.miRepositorioUsuario.findAll();

        usuarios.forEach((n)->{

            if(id.equals(n.getRol()) )
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El rol está asociado con uno o varios usuario, elimine o cambie los usuarios asociados a este rol");
        });

        if (rolActual!=null){
            this.miRepositorioRol.delete(rolActual);
            throw new ResponseStatusException(HttpStatus.OK,"El rol solicitado ha sido eliminado");
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El rol que se quiere eliminar no existe");
    }
}
