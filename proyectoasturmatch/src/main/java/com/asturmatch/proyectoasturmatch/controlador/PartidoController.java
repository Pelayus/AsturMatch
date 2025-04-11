package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class PartidoController {
	
	@Autowired
	private ServicioUsuario S_usuario;

	@Autowired
	private ServicioEquipo S_equipo;
	
	@Autowired
	private ServicioPartido S_partido;

}
