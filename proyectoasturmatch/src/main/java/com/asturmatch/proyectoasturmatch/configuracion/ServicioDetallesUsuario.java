package com.asturmatch.proyectoasturmatch.configuracion;

import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class ServicioDetallesUsuario implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return new DetallesUsuario(usuario);
    }

}
