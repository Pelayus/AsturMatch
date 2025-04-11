package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asturmatch.proyectoasturmatch.modelo.Resultado;
import com.asturmatch.proyectoasturmatch.repositorios.ResultadoRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioResultado;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioResultadoImpl implements ServicioResultado {

    @Autowired
    private ResultadoRepository resultado_R;

    @Override
    @Transactional(readOnly = true)
    public List<Resultado> obtenerTodosResultados() {
        return resultado_R.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resultado> obtenerResultadoPorId(Long id) {
        return resultado_R.findById(id);
    }

    @Override
    @Transactional
    public Resultado guardarResultado(Resultado resultado) {
        validarResultado(resultado);
        return resultado_R.save(resultado);
    }

    @Override
    @Transactional
    public void eliminarResultado(Long id) {
        if (!resultado_R.existsById(id)) {
            throw new EntityNotFoundException("Resultado no encontrado con ID: " + id);
        }
        resultado_R.deleteById(id);
    }

    @Override
    @Transactional
    public Resultado actualizarResultado(Long id, Resultado resultado) {
        if (!resultado_R.existsById(id)) {
            throw new EntityNotFoundException("Resultado no encontrado con ID: " + id);
        }
        resultado.setId(id);
        validarResultado(resultado);
        return resultado_R.save(resultado);
    }

    private void validarResultado(Resultado resultado) {
        if (resultado.getPartido() == null) {
            throw new IllegalArgumentException("El resultado debe estar asociado a un partido");
        }
        if (resultado.getPuntuacionLocal() < 0 || resultado.getPuntuacionVisitante() < 0) {
            throw new IllegalArgumentException("Las puntuaciones no pueden ser negativas");
        }
    }
}
