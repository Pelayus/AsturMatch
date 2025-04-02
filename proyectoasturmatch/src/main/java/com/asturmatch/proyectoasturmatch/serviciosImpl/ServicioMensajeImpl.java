package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.repositorios.MensajeRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioMensaje;

@Service
public class ServicioMensajeImpl implements ServicioMensaje {

    @Autowired
    private MensajeRepository mensaje_R;

    @Override
    public void guardarMensaje(Mensaje mensaje) {
    	mensaje_R.save(mensaje);
    }

    @Override
    public List<Mensaje> obtenerMensajesPorUsuario(Usuario usuario) {
        return mensaje_R.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    @Override
    public void eliminarMensaje(Long mensajeId) {
    	mensaje_R.deleteById(mensajeId);
    }
}
