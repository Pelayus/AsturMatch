package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        Optional<Torneo> torneoOpt = torneo_R.findById(torneoId);
        Torneo torneo = torneoOpt.get();
        List<Equipo> equipos = torneo.getEquipos();
        
        
        int n = equipos.size();
        // Número de jornadas para un round robin (para equipos pares, son n-1 jornadas)
        int rounds = n - 1;
        int matchesPerRound = n / 2;
        
        // Calcular los días disponibles en el torneo
        LocalDate fechaInicio = torneo.getFechaInicio();
        LocalDate fechaFin = torneo.getFechaFin();
        long totalDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        
        if (totalDias < rounds) {
            throw new RuntimeException("No hay suficientes días en el torneo para acomodar todas las jornadas (se requieren " 
                    + rounds + " días, disponibles: " + totalDias + ").");
        }
        
        // Calculamos el intervalo en días entre cada jornada, de modo que la última jornada sea en fechaFin
        long intervalo = rounds > 1 ? (totalDias - 1) / (rounds - 1) : 0;
        
        List<Partido> partidos = new ArrayList<>();
        
        // Creamos una lista mutable para trabajar el algoritmo Round Robin (copiamos la lista de equipos)
        List<Equipo> equiposRound = new ArrayList<>(equipos);
        
        // Generamos cada ronda
        for (int round = 0; round < rounds; round++) {
            // Determinamos la fecha para esta ronda
            LocalDate roundDate;
            if (round == rounds - 1) {
                roundDate = fechaFin;
            } else {
                roundDate = fechaInicio.plusDays(round * intervalo);
            }
            LocalDateTime fechaHora = roundDate.atTime(10, 0);
            
            // Para la ronda actual, generamos los emparejamientos:
            for (int i = 0; i < matchesPerRound; i++) {
                int homeIndex = i;
                int awayIndex = (n - 1) - i;
                Equipo local = equiposRound.get(homeIndex);
                Equipo visitante = equiposRound.get(awayIndex);
                
                Partido partido = new Partido();
                partido.setTorneo(torneo);
                partido.setEquipoLocal(local);
                partido.setEquipoVisitante(visitante);
                partido.setUbicacion(torneo.getUbicacion());
                partido.setFechaHora(fechaHora);
                
                Resultado resultado = new Resultado();
                resultado.setPartido(partido);
                resultado.setPuntuacionLocal(0);
                resultado.setPuntuacionVisitante(0);
                partido.setResultado(resultado);
                
                partidos.add(partido);
            }
            
            // Rotar los equipos para la próxima ronda:
            // Mantenemos fijo el primer equipo y rotar el resto una posición hacia la derecha
            Equipo primer = equiposRound.get(0);
            List<Equipo> sublista = new ArrayList<>(equiposRound.subList(1, equiposRound.size()));
            // Rotamos: el último pasa a primera posición de la sublista y el resto se desplaza
            Equipo ultimo = sublista.remove(sublista.size() - 1);
            sublista.add(0, ultimo);
            // Reconstruimos la lista completa:
            equiposRound.clear();
            equiposRound.add(primer);
            equiposRound.addAll(sublista);
        }
        
        partido_R.saveAll(partidos);
    }
}
