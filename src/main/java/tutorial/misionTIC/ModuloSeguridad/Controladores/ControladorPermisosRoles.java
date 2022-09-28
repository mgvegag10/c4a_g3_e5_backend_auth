package tutorial.misionTIC.ModuloSeguridad.Controladores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Permiso;
import tutorial.misionTIC.ModuloSeguridad.Modelos.PermisosRoles;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Rol;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioPermiso;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioPermisosRoles;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioRol;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permisos-roles")
public class ControladorPermisosRoles {
    @Autowired
    private RepositorioPermisosRoles miRepositorioPermisoRoles;

    @Autowired
    private RepositorioPermiso miRepositorioPermiso;

    @Autowired
    private RepositorioRol miRepositorioRol;


    @GetMapping("")
    public List<PermisosRoles> index(){

        return this.miRepositorioPermisoRoles.findAll();
    }

    /**
     * Asignación rol y permiso
     * @param id_rol
     * @param id_permiso
     * @return
     */

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rol/{id_rol}/permiso/{id_permiso}")
    public PermisosRoles create(@PathVariable String id_rol,@PathVariable String id_permiso){

        PermisosRoles perRol=new PermisosRoles();
        Rol rol=this.miRepositorioRol.findById(id_rol).get();
        Permiso permiso=this.miRepositorioPermiso.findById(id_permiso).get();

        if (rol==null || permiso==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El rol o permiso que se desean asociar no existe");
        }
        List<PermisosRoles> permisosRoles = this.miRepositorioPermisoRoles.findAll();
        permisosRoles.forEach((n)->{
            if(n.getRol().get_id().equals(id_rol) && n.getPermiso().get_id().equals(id_permiso)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Este rol ya posee este permiso");
            }
        });

        perRol.setPermiso(permiso);
        perRol.setRol(rol);
        return this.miRepositorioPermisoRoles.save(perRol);
    }
    @GetMapping("{id}")
    public PermisosRoles show(@PathVariable String id){
        PermisosRoles permisosRolesActual=this.miRepositorioPermisoRoles
                .findById(id)
                .orElse(null);
        if(permisosRolesActual==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encuentra el permisoRol solicitado");
        return permisosRolesActual;
    }

    /**
     * Modificación Rol y Permiso
     * @param id
     * @param id_rol
     * @param id_permiso
     * @return
     */
    @PutMapping("{id}/rol/{id_rol}/permiso/{id_permiso}")
    public PermisosRoles update(@PathVariable String id,@PathVariable String id_rol,@PathVariable String id_permiso){
        PermisosRoles permisosRolesActual=this.miRepositorioPermisoRoles
                .findById(id)
                .orElse(null);
        Rol elRol=this.miRepositorioRol.findById(id_rol).get();
        Permiso elPermiso=this.miRepositorioPermiso.findById(id_permiso).get();

        if(permisosRolesActual!=null && elPermiso!=null && elRol!=null){
            permisosRolesActual.setPermiso(elPermiso);
            permisosRolesActual.setRol(elRol);
            return this.miRepositorioPermisoRoles.save(permisosRolesActual);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos en el body");
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        PermisosRoles permisosRolesActual=this.miRepositorioPermisoRoles
                .findById(id)
                .orElse(null);
        if (permisosRolesActual==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Este permisoRol no existe");

        this.miRepositorioPermisoRoles.delete(permisosRolesActual);
        throw new ResponseStatusException(HttpStatus.OK,"PermisoRol Eliminado");
    }

    @GetMapping("validar-permiso/rol/{id_rol}")
    public PermisosRoles getPermiso(@PathVariable String id_rol,@RequestBody Permiso infoPermiso){
        Permiso elPermiso=this.miRepositorioPermiso.getPermiso(infoPermiso.getUrl(),infoPermiso.getMetodo());
        Rol elRol=this.miRepositorioRol.findById(id_rol).get();
        if (elPermiso!=null && elRol!=null){
            return this.miRepositorioPermisoRoles.getPermisoRol(elRol.get_id(),
                    elPermiso.get_id());
        }else{
            return null;
        }
    }
}
