package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface ServicioMensaje {

	void guardarMensaje(Mensaje mensaje);
    List<Mensaje> obtenerMensajesPorUsuario(Usuario usuario);
    void eliminarMensaje(Long mensajeId);
}
