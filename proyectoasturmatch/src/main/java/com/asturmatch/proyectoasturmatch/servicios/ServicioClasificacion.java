package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import com.asturmatch.proyectoasturmatch.modelo.Clasificacion;
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;

public interface ServicioClasificacion {

	void crearClasificacionParaTorneoFutbol(Torneo torneo);
	
	void crearClasificacionParaTorneoBaloncesto(Torneo torneo);

	List<Clasificacion> obtenerClasificacionPorTorneo(Long torneoId);
	
	Clasificacion obtenerClasificacionPorEquipoYTorneo(Equipo equipo, Torneo torneo);

	void actualizarClasificacion(Clasificacion clasificacion);
}
