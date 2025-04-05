package com.asturmatch.proyectoasturmatch.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface MensajeRepository extends JpaRepository<Mensaje, Long>{

	List<Mensaje> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
	
	// Mensajes recibidos por un usuario, ordenados del más reciente al más antiguo
    List<Mensaje> findByReceptorOrderByFechaCreacionDesc(Usuario receptor);
}
