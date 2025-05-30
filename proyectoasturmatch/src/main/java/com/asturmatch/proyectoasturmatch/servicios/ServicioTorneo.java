package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import java.util.Optional;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface ServicioTorneo {
	
	List<Torneo> obtenerTodosTorneos();

    Optional<Torneo> obtenerTorneoPorId(Long id);
    
    List<Torneo> obtenerTorneosPorCreador(Usuario creador);
    
    List<Torneo> obtenerTorneosPorJugador(Usuario jugador);

    Torneo guardarTorneo(Torneo torneo);
    
    Torneo actualizarTorneo(Torneo torneo);

    void eliminarTorneo(Long id);
}
