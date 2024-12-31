package com.asturmatch.proyectoasturmatch.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class EquipoController {

	@Autowired
	private ServicioUsuario usuarioServicio;

	@Autowired
	private ServicioEquipo equipoServicio;

	@GetMapping("/crear-equipo")
	public String mostrarFormularioCrearEquipo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("equipo", new Equipo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		return "crear-equipo";
	}

	@PostMapping("/crear-equipo")
	public String crearEquipo(@ModelAttribute Equipo equipo, @ModelAttribute("nombreUsuario") String nombreUsuario,
			Model modelo) {
		// Obtengo el usuario actual desde el servicio
		Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);
		
		usuarioActual.setRol(Rol.JUGADOR);
		usuarioServicio.guardarUsuario(usuarioActual);

		equipo.setJugadores(List.of(usuarioActual));
		equipo.setTorneo(null);

		// Guardar el equipo
		equipoServicio.guardarEquipo(equipo);

		modelo.addAttribute("mensaje", "Equipo creado con éxito");
		return "redirect:/equipos";
	}

	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}
