package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import java.util.Optional;
import com.asturmatch.proyectoasturmatch.modelo.Resultado;

public interface ServicioResultado {
	
	List<Resultado> obtenerTodosResultados();

    Optional<Resultado> obtenerResultadoPorId(Long id);

    Resultado guardarResultado(Resultado resultado);

    void eliminarResultado(Long id);

    Resultado actualizarResultado(Long id, Resultado resultado);

}
