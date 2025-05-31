package com.asturmatch.proyectoasturmatch.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Usuario findByEmail(String email);
	Usuario findByNombre(String nombre);
	boolean existsByEmail(String email);
	boolean existsByNombre(String nombre);
	boolean existsByNombreUsuario(String nombreUsuario);
	boolean existsByDni(String dni);

}
