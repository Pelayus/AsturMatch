package com.asturmatch.proyectoasturmatch.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface TorneoRepository extends JpaRepository<Torneo, Long>{

	@Query("SELECT t FROM Torneo t WHERE t.creador = :creador")
	List<Torneo> findByCreador(@Param("creador") Usuario creador);
	
	List<Torneo> findByEquiposJugadoresContains(Usuario jugador);

}
