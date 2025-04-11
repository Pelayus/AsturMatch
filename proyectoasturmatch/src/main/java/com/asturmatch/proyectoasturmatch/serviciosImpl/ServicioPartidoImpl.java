package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Partido;
import com.asturmatch.proyectoasturmatch.modelo.Resultado;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.repositorios.PartidoRepository;
import com.asturmatch.proyectoasturmatch.repositorios.TorneoRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioPartidoImpl implements ServicioPartido {

    @Autowired
    private PartidoRepository partido_R;
    
    @Autowired
    private TorneoRepository torneo_R;

    public ServicioPartidoImpl(PartidoRepository repositorioPartido) {
        this.partido_R = repositorioPartido;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partido> obtenerTodosPartidos() {
        return partido_R.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partido> obtenerPartidoPorId(Long id) {
        return partido_R.findById(id);
    }

    @Override
    @Transactional
    public Partido guardarPartido(Partido partido) {
        validarPartido(partido);
        return partido_R.save(partido);
    }

    @Override
    @Transactional
    public void eliminarPartido(Long id) {
        if (!partido_R.existsById(id)) {
            throw new EntityNotFoundException("Partido no encontrado con ID: " + id);
        }
        partido_R.deleteById(id);
    }

    @Override
    @Transactional
    public Partido actualizarPartido(Long id, Partido partido) {
        if (!partido_R.existsById(id)) {
            throw new EntityNotFoundException("Partido no encontrado con ID: " + id);
        }
        partido.setId(id);
        validarPartido(partido);
        return partido_R.save(partido);
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
    
    public void generarPartidosParaTorneo(Long torneoId) {
        Torneo torneo = torneo_R.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        List<Equipo> equipos = torneo.getEquipos();

        if (equipos.size() < 8) {
            throw new RuntimeException("El torneo necesita al menos 8 equipos para generar partidos.");
        }

        List<Partido> partidos = new ArrayList<>();

        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                Equipo local = equipos.get(i);
                Equipo visitante = equipos.get(j);

                Partido partido = new Partido();
                partido.setTorneo(torneo);
                partido.setEquipoLocal(local);
                partido.setEquipoVisitante(visitante);
                partido.setUbicacion(torneo.getUbicacion());

                // Fecha escalonada: día de inicio + N días según índice
                LocalDate fecha = torneo.getFechaInicio().plusDays(partidos.size());
                partido.setFechaHora(fecha.atTime(10, 0)); // A las 10:00 am

                Resultado resultado = new Resultado();
                resultado.setPartido(partido);
                resultado.setPuntuacionLocal(0);
                resultado.setPuntuacionVisitante(0);

                partido.setResultado(resultado);

                partidos.add(partido);
            }
        }
        partido_R.saveAll(partidos);
    }
}
