package com.asturmatch.proyectoasturmatch.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
