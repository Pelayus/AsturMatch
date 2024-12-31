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

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

    @Autowired
    private UsuarioRepository repositorioUsuario;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosUsuarios() {
        return repositorioUsuario.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return repositorioUsuario.findById(id);
    }
    
    @Override
    @Transactional
    public Usuario obtenerUsuarioPorEmail(String email) {
        return repositorioUsuario.findByEmail(email); 
    }
    
    @Transactional
    public Usuario obtenerUsuarioPorNombre(String nombre) {
        return repositorioUsuario.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
    	validarUsuario(usuario);
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.JUGADOR); // Asigno por defecto el rol de JUGADOR 
        }
        return repositorioUsuario.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!repositorioUsuario.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        repositorioUsuario.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        if (!repositorioUsuario.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuario.setId(id);
        validarUsuario(usuario);
        return repositorioUsuario.save(usuario);
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

