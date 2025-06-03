package com.asturmatch.proyectoasturmatch.configuracion;

import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class DetallesUsuario implements UserDetails {
    private final Usuario usuario;

    public DetallesUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario == null || usuario.getRol() == null) {
            throw new IllegalStateException("Usuario o su rol es null");
        }

        String rol = "ROLE_" + usuario.getRol().name().toUpperCase();
        return List.of(new SimpleGrantedAuthority(rol));
    }


    @Override
    public String getPassword() {
        return usuario.getContraseña();
    }

    @Override
    public String getUsername() {
        return usuario.getNombreUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
