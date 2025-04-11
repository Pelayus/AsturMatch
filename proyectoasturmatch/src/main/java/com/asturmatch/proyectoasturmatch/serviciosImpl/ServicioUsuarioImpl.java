package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.repositorios.UsuarioRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

    @Autowired
    private UsuarioRepository usuario_R;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosUsuarios() {
        return usuario_R.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuario_R.findById(id);
    }
    
    @Override
    @Transactional
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuario_R.findByEmail(email); 
    }
    
    @Transactional
    public Usuario obtenerUsuarioPorNombre(String nombre) {
        return usuario_R.findByNombre(nombre);
    }
    
    @PostConstruct
    public void crearAdminSiNoExiste() {
        Usuario admin = usuario_R.findByNombre("admin");
        if (admin == null) {
            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setNombre("Admin");
            usuarioAdmin.setEmail("admin@admin.com");
            usuarioAdmin.setContraseña("admin");
            usuarioAdmin.setRol(Rol.ADMIN);
            usuarioAdmin = usuario_R.save(usuarioAdmin);

            System.out.println("Administrador creado con éxito.");
        } else {
            System.out.println("El usuario administrador ya existe.");
        }
    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
    	validarUsuario(usuario);
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.USUARIO);
        }
        return usuario_R.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuario_R.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuario_R.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        if (!usuario_R.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuario.setId(id);
        validarUsuario(usuario);
        return usuario_R.save(usuario);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio");
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
    }
}

