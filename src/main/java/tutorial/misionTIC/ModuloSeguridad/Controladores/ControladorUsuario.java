package tutorial.misionTIC.ModuloSeguridad.Controladores;
import org.springframework.data.mongodb.core.mapping.DBRef;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Rol;
import tutorial.misionTIC.ModuloSeguridad.Modelos.Usuario;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioRol;
import tutorial.misionTIC.ModuloSeguridad.Repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {
    @Autowired
    private RepositorioUsuario miRepositorioUsuario;
    @Autowired
    private RepositorioRol miRepositorioRol;
    @GetMapping("")
    public List<Usuario> index(){
        List<Usuario> users =this.miRepositorioUsuario.findAll();
        if (users.isEmpty())
            throw new ResponseStatusException(HttpStatus.OK,"No existen usuarios");
        return users;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Usuario create(@RequestBody  preUser infoUsuario){

        if (infoUsuario.getContrasena()==null || infoUsuario.getSeudonimo()==null || infoUsuario.getCorreo()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos por ser enviados en el body");

        infoUsuario.setContrasena(convertirSHA256(infoUsuario.getContrasena()));

        List<Usuario> usuarios =this.miRepositorioUsuario.findAll();

        usuarios.forEach((n)->{
            if(n.getSeudonimo().equals(infoUsuario.getSeudonimo()) || n.getCorreo().equals(infoUsuario.getCorreo()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Este pseudónimo o correo ya existe. Ingrese uno diferente");
        });
        Usuario usuario = new Usuario(infoUsuario.getSeudonimo(),infoUsuario.getCorreo(),infoUsuario.getContrasena());

        if(infoUsuario.getRol()!=null){
            Rol rolActual=this.miRepositorioRol
                    .findById(infoUsuario.getRol())
                    .orElse(null);
            if(rolActual==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El rol solicitado no existe, no se ha creado un usuario");
            }else
                usuario.setRol(rolActual);
        }

        //this.miRepositorioUsuario.save(usuario);
        //asignarRolAUsuario(infoUsuario.get_id(),infoUsuario.getRol());
        return this.miRepositorioUsuario.save(usuario);
    }
    @GetMapping("{id}")
    public Usuario show(@PathVariable String id){
        Usuario usuarioActual=this.miRepositorioUsuario.findById(id).orElse(null);
        if(usuarioActual==null){
            throw new ResponseStatusException(HttpStatus.ACCEPTED,"El usuario no fue encontrado");
        }
        return usuarioActual;
    }
    @PutMapping("{id}")
    public Usuario update(@PathVariable String id,@RequestBody  Usuario infoUsuario){
        Usuario usuarioActual=this.miRepositorioUsuario.findById(id).orElse(null);
        List<Usuario> usuarios =this.miRepositorioUsuario.findAll();

        if (usuarioActual==null)
            throw new ResponseStatusException(HttpStatus.ACCEPTED,"El usuario no fue encontrado");

        if (infoUsuario.getContrasena()==null || infoUsuario.getSeudonimo()==null || infoUsuario.getCorreo()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Faltan campos por ser enviados en el body");

        usuarios.forEach((n)->{
            if( (n.getSeudonimo().equals(infoUsuario.getSeudonimo()) || n.getCorreo().equals(infoUsuario.getCorreo()))
            && !usuarioActual.get_id().equals(n.get_id())   )
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Este pseudónimo o correo ya existe. Ingrese uno diferente");
        });

        usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
        usuarioActual.setCorreo(infoUsuario.getCorreo());
        usuarioActual.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
        return this.miRepositorioUsuario.save(usuarioActual);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        if (usuarioActual!=null){
            this.miRepositorioUsuario.delete(usuarioActual);
            throw new ResponseStatusException(HttpStatus.OK,"El usuario solicitado ha sido eliminado");
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El usuario que se quiere eliminar no existe");
    }
    /**
     * Relación (1 a n) entre rol y usuario
     * @param id
     * @param id_rol
     * @return
     */
    @PutMapping("{id}/rol/{id_rol}")
    public Usuario asignarRolAUsuario(@PathVariable String id,@PathVariable String id_rol){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        if(usuarioActual==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El usuario solicitado no existe");
        }
        Rol rolActual=this.miRepositorioRol
                .findById(id_rol)
                .orElse(null);
        if(rolActual==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El rol solicitado no existe");
        }

        usuarioActual.setRol(rolActual);
        return this.miRepositorioUsuario.save(usuarioActual);
    }

    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

class preUser{
    private String seudonimo;
    private String correo;
    private String contrasena;
    private String rol;

    public preUser(String seudonimo, String correo, String contrasena, String rol) {
        this.seudonimo = seudonimo;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getSeudonimo() {
        return seudonimo;
    }

    public void setSeudonimo(String seudonimo) {
        this.seudonimo = seudonimo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}