package com.asturmatch.proyectoasturmatch.serviciosImpl;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.TipoEquipo;
import com.asturmatch.proyectoasturmatch.modelo.TipoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
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
    private EquipoRepository equipo_R;

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> obtenerTodosEquipos() {
        return equipo_R.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> obtenerEquipoPorId(Long id) {
        return equipo_R.findById(id);
    }
    
    @Override
    @Transactional
    public List<Equipo> obtenerEquipoPorUsuario(Usuario usuario) {
        return equipo_R.findByJugadores(usuario);
    }
    
    @Transactional
    public List<Equipo> obtenerEquiposPorTipo(TipoEquipo tipo) {
        return equipo_R.findByTipoEquipo(tipo);
    }

    public List<Equipo> obtenerEquiposPorTorneo(Torneo torneo) {
    return equipo_R.findByTorneo(torneo);
   }

    @Override
    @Transactional
    public Equipo guardarEquipo(Equipo equipo) {
        validarEquipo(equipo);
        return equipo_R.save(equipo);
    }
    
    @Override
    @Transactional
    public Equipo modificarEquipo(Equipo equipo) {
        return equipo_R.save(equipo);
    }

    @Override
    @Transactional
    public void eliminarEquipo(Long id) {
        if (!equipo_R.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado con ID: " + id);
        }
        equipo_R.deleteById(id);
    }

    @Override
    @Transactional
    public Equipo actualizarEquipo(Long id, Equipo equipo) {
        if (!equipo_R.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado con ID: " + id);
        }
        equipo.setId(id);
        validarEquipo(equipo);
        return equipo_R.save(equipo);
    }
    
    @Transactional
    public void unirseATorneo(Equipo equipo, Torneo torneo) {
        if (equipo.getTipoEquipo() == TipoEquipo.AMATEUR && torneo.getTipoTorneo() == TipoTorneo.PROFESIONAL) {
            System.err.println("Los equipos amateur solo pueden unirse a torneos amateur.");
        }
        equipo.setTorneo(torneo);
        equipo_R.save(equipo);
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