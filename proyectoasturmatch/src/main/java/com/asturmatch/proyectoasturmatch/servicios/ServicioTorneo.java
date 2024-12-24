package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import java.util.Optional;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;

public interface ServicioTorneo {
	
	List<Torneo> obtenerTodosTorneos();

    Optional<Torneo> obtenerTorneoPorId(Long id);

    Torneo guardarTorneo(Torneo torneo);

    void eliminarTorneo(Long id);

    Torneo actualizarTorneo(Long id, Torneo torneo);

}
