package tutorial.misionTIC.ModuloSeguridad.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Permiso;
import tutorial.misionTIC.ModuloSeguridad.Modelos.PermisosRoles;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioPermiso;
import org.springframework.web.server.ResponseStatusException;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioPermisosRoles;

import java.util.Iterator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permisos")
public class ControladorPermiso {

    @Autowired
    private RepositorioPermisosRoles miRepositorioPermisoRoles;
    @Autowired
    private RepositorioPermiso miRepositorioPermiso;

    @GetMapping("")
    public List<Permiso> index(){
        List<Permiso> permisos = this.miRepositorioPermiso.findAll();
        if(permisos.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"" +
                    "No existen permisos");
        return permisos;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permiso create(@RequestBody  Permiso infoPermiso){
        List<Permiso> permisos = this.miRepositorioPermiso.findAll();

        if(infoPermiso.getUrl()==null || infoPermiso.getMetodo()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos por ser enviados en el body");

        return this.miRepositorioPermiso.save(infoPermiso);
    }
    @GetMapping("{id}")

    public Permiso show(@PathVariable String id){
        Permiso permisoActual=this.miRepositorioPermiso
                .findById(id)
                .orElse(null);
        if (permisoActual==null)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"El permiso que se quiere consultar no existe");
        return permisoActual;
    }
    @PutMapping("{id}")
    public Permiso update(@PathVariable String id,@RequestBody  Permiso infoPermiso){
        Permiso permisoActual=this.miRepositorioPermiso
                .findById(id)
                .orElse(null);
        if(permisoActual==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se ha encontrado el permiso con el id solicitido");

        List<Permiso> permisos = this.miRepositorioPermiso.findAll();

        if(infoPermiso.getUrl()==null || infoPermiso.getMetodo()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos por ser enviados en el body");

        permisos.forEach((n)->{
            if(n.getUrl().equals(infoPermiso.getUrl()) && !n.getUrl().equals(permisoActual.getUrl())
            && !permisoActual.get_id().equals(n.get_id()) )
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Este url ya existe. Ingrese uno diferente");
        });

        permisoActual.setMetodo(infoPermiso.getMetodo());
        permisoActual.setUrl(infoPermiso.getUrl());
        return this.miRepositorioPermiso.save(permisoActual);


    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Permiso permisoActual=this.miRepositorioPermiso
                .findById(id)
                .orElse(null);
        if (permisoActual==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se ha encontrado el permiso a eliminar");
        else {

            if (permisoActual!=null){
                List<PermisosRoles> permRol = this.miRepositorioPermisoRoles.findAll();

                Iterator<PermisosRoles> iterator = permRol.iterator();
                while (iterator.hasNext()){
                    PermisosRoles permRolIterator = iterator.next();
                    if(permRolIterator.getPermiso().equals(permisoActual)){
                        this.miRepositorioPermisoRoles.delete(permRolIterator);
                    }

                }
            }



            this.miRepositorioPermiso.delete(permisoActual);
            throw new ResponseStatusException(HttpStatus.OK,"Se ha eliminado el permiso solicitado");
        }

    }
}
