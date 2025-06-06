package com.asturmatch.proyectoasturmatch.servicios;

import java.util.List;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.TipoMensaje;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;

public interface ServicioMensaje {

	void guardarMensaje(Mensaje mensaje);
    List<Mensaje> obtenerMensajesPorUsuario(Usuario usuario);
    void eliminarMensaje(Long mensajeId);
    List<Mensaje> obtenerMensajesRecibidos(Usuario receptor);
    List<Mensaje> obtenerMensajesPorReceptorYTipo(Usuario receptor, TipoMensaje tipo);
}
