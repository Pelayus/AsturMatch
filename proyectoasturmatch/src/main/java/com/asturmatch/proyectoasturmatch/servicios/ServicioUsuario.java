package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import java.util.Optional;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface ServicioUsuario {
	
	List<Usuario> obtenerTodosUsuarios();

    Optional<Usuario> obtenerUsuarioPorId(Long id);
    
    Usuario obtenerUsuarioPorEmail(String email);
    
    Usuario obtenerUsuarioPorNombre(String nombre);

    Usuario guardarUsuario(Usuario usuario);

    void eliminarUsuario(Long id);

    Usuario actualizarUsuario(Long id, Usuario usuario);

}
