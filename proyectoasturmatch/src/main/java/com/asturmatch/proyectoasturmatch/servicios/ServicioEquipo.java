package com.asturmatch.proyectoasturmatch.servicios;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.TipoEquipo;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

import java.util.List;
import java.util.Optional;

public interface ServicioEquipo {
	
	List<Equipo> obtenerTodosEquipos();

    Optional<Equipo> obtenerEquipoPorId(Long id);
    
    List<Equipo> obtenerEquipoPorUsuario(Usuario usuario);
    
    List<Equipo> obtenerEquiposPorTipo(TipoEquipo tipo);

    Equipo guardarEquipo(Equipo equipo);

    void eliminarEquipo(Long id);
    
    void unirseATorneo(Equipo equipo, Torneo torneo);

    Equipo actualizarEquipo(Long id, Equipo equipo);

}
