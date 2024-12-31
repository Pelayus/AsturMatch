package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.repositorios.TorneoRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioTorneoImpl implements ServicioTorneo {

    @Autowired
    private TorneoRepository repositorioTorneo;

    @Override
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTodosTorneos() {
        return repositorioTorneo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Torneo> obtenerTorneoPorId(Long id) {
        return repositorioTorneo.findById(id);
    }

    @Override
    @Transactional
    public Torneo guardarTorneo(Torneo torneo) {
        validarTorneo(torneo);
        return repositorioTorneo.save(torneo);
    }

    @Override
    @Transactional
    public void eliminarTorneo(Long id) {
        if (!repositorioTorneo.existsById(id)) {
            throw new EntityNotFoundException("Torneo no encontrado con ID: " + id);
        }
        repositorioTorneo.deleteById(id);
    }

    @Override
    @Transactional
    public Torneo actualizarTorneo(Long id, Torneo torneo) {
        if (!repositorioTorneo.existsById(id)) {
            throw new EntityNotFoundException("Torneo no encontrado con ID: " + id);
        }
        torneo.setId(id);
        validarTorneo(torneo);
        return repositorioTorneo.save(torneo);
    }

    private void validarTorneo(Torneo torneo) {
        if (torneo.getNombre() == null || torneo.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del torneo es obligatorio");
        }
        if (torneo.getFechaInicio() == null || torneo.getFechaFin() == null || 
            torneo.getFechaInicio().isAfter(torneo.getFechaFin())) {
            throw new IllegalArgumentException("Las fechas del torneo son inválidas");
        }
    }
}
