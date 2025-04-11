package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.repositorios.TorneoRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioTorneoImpl implements ServicioTorneo {

    @Autowired
    private TorneoRepository torneo_R;


    @Override
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTodosTorneos() {
        return torneo_R.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Torneo> obtenerTorneoPorId(Long id) {
        return torneo_R.findById(id);
    }
    
    @Override
    public List<Torneo> obtenerTorneosPorCreador(Usuario creador) {
        return torneo_R.findByCreador(creador);
    }

    @Override
    @Transactional
    public Torneo guardarTorneo(Torneo torneo) {
        validarTorneo(torneo);
        return torneo_R.save(torneo);
    }

    @Override
    @Transactional
    public void eliminarTorneo(Long id) {
        if (!torneo_R.existsById(id)) {
            throw new EntityNotFoundException("Torneo no encontrado con ID: " + id);
        }
        torneo_R.deleteById(id);
    }

    @Override
    @Transactional
    public Torneo actualizarTorneo(Long id, Torneo torneo) {
        if (!torneo_R.existsById(id)) {
            throw new EntityNotFoundException("Torneo no encontrado con ID: " + id);
        }
        torneo.setId(id);
        validarTorneo(torneo);
        return torneo_R.save(torneo);
    }


    private void validarTorneo(Torneo torneo) {
        if (torneo.getNombre() == null || torneo.getNombre().isEmpty()) {
           System.err.println("El nombre del torneo es obligatorio");
        }
        if (torneo.getFechaInicio() == null || torneo.getFechaFin() == null || 
            torneo.getFechaInicio().isAfter(torneo.getFechaFin())) {
        	System.err.println("Las fechas del torneo son inválidas");
        }
    }
}
