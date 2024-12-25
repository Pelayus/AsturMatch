package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asturmatch.proyectoasturmatch.modelo.Partido;
import com.asturmatch.proyectoasturmatch.repositorios.PartidoRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioPartidoImpl implements ServicioPartido {

    @Autowired
    private PartidoRepository repositorioPartido;

    public ServicioPartidoImpl(PartidoRepository repositorioPartido) {
        this.repositorioPartido = repositorioPartido;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partido> obtenerTodosPartidos() {
        return repositorioPartido.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partido> obtenerPartidoPorId(Long id) {
        return repositorioPartido.findById(id);
    }

    @Override
    @Transactional
    public Partido guardarPartido(Partido partido) {
        validarPartido(partido);
        return repositorioPartido.save(partido);
    }

    @Override
    @Transactional
    public void eliminarPartido(Long id) {
        if (!repositorioPartido.existsById(id)) {
            throw new EntityNotFoundException("Partido no encontrado con ID: " + id);
        }
        repositorioPartido.deleteById(id);
    }

    @Override
    @Transactional
    public Partido actualizarPartido(Long id, Partido partido) {
        if (!repositorioPartido.existsById(id)) {
            throw new EntityNotFoundException("Partido no encontrado con ID: " + id);
        }
        partido.setId(id);
        validarPartido(partido);
        return repositorioPartido.save(partido);
    }

    private void validarPartido(Partido partido) {
        if (partido.getEquipoLocal() == null || partido.getEquipoVisitante() == null) {
            throw new IllegalArgumentException("El partido debe tener un equipo local y un equipo visitante");
        }
        if (partido.getEquipoLocal().equals(partido.getEquipoVisitante())) {
            throw new IllegalArgumentException("El equipo local y el equipo visitante no pueden ser el mismo");
        }
        if (partido.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora del partido es obligatoria");
        }
    }
}
