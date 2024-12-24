package com.asturmatch.proyectoasturmatch.servicios;

import com.asturmatch.proyectoasturmatch.modelo.Partido;
import java.util.List;
import java.util.Optional;

public interface ServicioPartido {
	
	List<Partido> obtenerTodosPartidos();

	Optional<Partido> obtenerPartidoPorId(Long id);

	Partido guardarPartido(Partido partido);

	void eliminarPartido(Long id);

	Partido actualizarPartido(Long id, Partido partido);

}
