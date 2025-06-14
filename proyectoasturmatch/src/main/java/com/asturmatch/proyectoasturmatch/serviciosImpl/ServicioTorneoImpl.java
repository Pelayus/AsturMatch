package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asturmatch.proyectoasturmatch.modelo.TipoDeporte;
import com.asturmatch.proyectoasturmatch.modelo.TipoTorneo;
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
    public List<Torneo> obtenerTorneosPorJugador(Usuario jugador) {
        return torneo_R.findByEquiposJugadoresContains(jugador);
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
    public Torneo actualizarTorneo(Torneo torneo) {
        validarTorneo(torneo);
        return torneo_R.save(torneo);
    }
    
    public List<Torneo> filtrarTorneos(String ubicacion, TipoTorneo tipoTorneo, TipoDeporte deporte) {
        return torneo_R.findAll().stream()
            .filter(t -> ubicacion == null || ubicacion.isBlank() || t.getUbicacion().equalsIgnoreCase(ubicacion))
            .filter(t -> tipoTorneo == null || t.getTipoTorneo() == tipoTorneo)
            .filter(t -> deporte == null || t.getDeporte() == deporte)
            .toList();
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
