package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asturmatch.proyectoasturmatch.modelo.Clasificacion;
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.repositorios.ClasificacionRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioClasificacion;

@Service
public class ServicioClasificacionImpl implements ServicioClasificacion{
	
	@Autowired
    private ClasificacionRepository clasificacion_R;

    @Override
    public void crearClasificacionParaTorneoFutbol(Torneo torneo) {
        for (Equipo equipo : torneo.getEquipos()) {
            Clasificacion clasificacion = new Clasificacion();
            clasificacion.setEquipo(equipo);
            clasificacion.setTorneo(torneo);
            clasificacion.setPj(0);
            clasificacion.setPg(0);
            clasificacion.setPe(0);
            clasificacion.setPp(0);
            clasificacion.setGf(0);
            clasificacion.setGc(0);
            clasificacion.setPuntos(0);
            clasificacion_R.save(clasificacion);
        }
    }
    
    @Override
    public void crearClasificacionParaTorneoBaloncesto(Torneo torneo) {
        for (Equipo equipo : torneo.getEquipos()) {
            Clasificacion clasificacion = new Clasificacion();
            clasificacion.setEquipo(equipo);
            clasificacion.setTorneo(torneo);
            clasificacion.setPj(0);
            clasificacion.setPg(0);
            clasificacion.setPp(0);
            clasificacion.setPf(0);
            clasificacion.setPc(0);
            clasificacion.setDif(0);
            clasificacion_R.save(clasificacion);
        }
    }

    @Override
    public List<Clasificacion> obtenerClasificacionPorTorneo(Long torneoId) {
        return clasificacion_R.findByTorneoId(torneoId);
    }
    
    @Override
    public Clasificacion obtenerClasificacionPorEquipoYTorneo(Equipo equipo, Torneo torneo) {
        return clasificacion_R.findByEquipoAndTorneo(equipo, torneo);
    }

    @Override
    public void actualizarClasificacion(Clasificacion clasificacion) {
    	clasificacion_R.save(clasificacion);
    }

}
