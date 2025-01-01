package com.asturmatch.proyectoasturmatch.servicios;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

import java.util.List;
import java.util.Optional;

public interface ServicioEquipo {
	
	List<Equipo> obtenerTodosEquipos();

    Optional<Equipo> obtenerEquipoPorId(Long id);
    
    List<Equipo> obtenerEquipoPorUsuario(Usuario usuario);

    Equipo guardarEquipo(Equipo equipo);

    void eliminarEquipo(Long id);

    Equipo actualizarEquipo(Long id, Equipo equipo);

}
