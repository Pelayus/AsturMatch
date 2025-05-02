package com.asturmatch.proyectoasturmatch.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.asturmatch.proyectoasturmatch.modelo.Partido;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;

public interface PartidoRepository extends JpaRepository<Partido, Long>{
	
	List<Partido> findByTorneoOrderByFechaHoraAsc(Torneo torneo);
}
