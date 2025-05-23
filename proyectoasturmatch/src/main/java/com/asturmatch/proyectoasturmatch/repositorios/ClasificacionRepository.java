package com.asturmatch.proyectoasturmatch.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asturmatch.proyectoasturmatch.modelo.Clasificacion;

public interface ClasificacionRepository extends JpaRepository<Clasificacion, Long>{
	
	List<Clasificacion> findByTorneoId(Long torneoId);

}
