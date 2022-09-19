package tutorial.misionTIC.ModuloSeguridad.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
        if(infoRol.getDescripcion() != null &&
                infoRol.getNombre()!= null)
            return this.miRepositorioRol.save(infoRol);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos por ser enviados en el body");
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
        if (rolActual!=null){
            if(rolActual.getNombre()!=null)
                rolActual.setNombre(infoRol.getNombre());
            if(rolActual.getDescripcion()!=null)
                rolActual.setDescripcion(infoRol.getDescripcion());
            return this.miRepositorioRol.save(rolActual);
        }else{
            throw new ResponseStatusException(HttpStatus.ACCEPTED,"El rol que se quiere cambiar no existe");
        }
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
