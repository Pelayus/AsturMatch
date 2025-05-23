package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import com.asturmatch.proyectoasturmatch.modelo.Clasificacion;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;

public interface ServicioClasificacion {

	void crearClasificacionParaTorneo(Torneo torneo);

	List<Clasificacion> obtenerClasificacionPorTorneo(Long torneoId);

	void actualizarClasificacion(Clasificacion clasificacion);
}
