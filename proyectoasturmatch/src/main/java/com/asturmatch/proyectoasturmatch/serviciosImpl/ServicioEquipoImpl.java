package com.asturmatch.proyectoasturmatch.serviciosImpl;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.repositorios.EquipoRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioEquipoImpl implements ServicioEquipo {

	@Autowired
    private EquipoRepository repositorioEquipo;

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> obtenerTodosEquipos() {
        return repositorioEquipo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> obtenerEquipoPorId(Long id) {
        return repositorioEquipo.findById(id);
    }

    @Override
    @Transactional
    public Equipo guardarEquipo(Equipo equipo) {
        validarEquipo(equipo);
        return repositorioEquipo.save(equipo);
    }

    @Override
    @Transactional
    public void eliminarEquipo(Long id) {
        if (!repositorioEquipo.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado con ID: " + id);
        }
        repositorioEquipo.deleteById(id);
    }

    @Override
    @Transactional
    public Equipo actualizarEquipo(Long id, Equipo equipo) {
        if (!repositorioEquipo.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado con ID: " + id);
        }
        equipo.setId(id);
        validarEquipo(equipo);
        return repositorioEquipo.save(equipo);
    }

    private void validarEquipo(Equipo equipo) {
        if (equipo.getNombre() == null || equipo.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }
        if (equipo.getJugadores() == null || equipo.getJugadores().isEmpty()) {
            throw new IllegalArgumentException("El equipo debe tener al menos un jugador");
        }
    }
}