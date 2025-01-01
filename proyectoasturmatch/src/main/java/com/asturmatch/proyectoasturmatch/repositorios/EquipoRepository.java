package com.asturmatch.proyectoasturmatch.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface EquipoRepository extends JpaRepository<Equipo, Long>{
	
	@Query("SELECT e FROM Equipo e WHERE :usuario MEMBER OF e.jugadores")
	Optional<Equipo> findByJugadorUnico(Usuario usuario);

	List<Equipo> findByJugadores(Usuario usuario);

}
