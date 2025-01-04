package com.asturmatch.proyectoasturmatch.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.EstadoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class TorneoController {

	@Autowired
	private ServicioUsuario usuarioServicio;

	@Autowired
	private ServicioTorneo torneoServicio;
	
	@Autowired
	private ServicioEquipo equipoServicio;

	@GetMapping("/crear-torneo")
	public String mostrarFormularioCrearTorneo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("torneo", new Torneo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		return "crear-torneo";
	}

	@PostMapping("/crear-torneo")
	public String crearTorneo(@ModelAttribute Torneo torneo, @ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);

		usuarioActual.setRol(Rol.ORGANIZADOR);
		usuarioServicio.guardarUsuario(usuarioActual);

		torneo.setEstado(EstadoTorneo.PENDIENTE);
		torneoServicio.guardarTorneo(torneo);

		modelo.addAttribute("mensaje", "Torneo creado con éxito. Ahora eres el ORGANIZADOR.");
		return "redirect:/torneos";
	}

	@GetMapping("/unirse-torneo")
	public String listarTorneosDisponibles(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		modelo.addAttribute("torneos", torneoServicio.obtenerTodosTorneos()); // Añado la lista de torneos
		return "unirse-torneo";
	}
	
	@PostMapping("/unirse-torneo")
	public String unirseATorneo(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                            @ModelAttribute("torneoId") Long torneoId, Model modelo) {
	    modelo.addAttribute("UsuarioActual", nombreUsuario);
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));

	    Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);

	    List<Equipo> equipoDelUsuario = equipoServicio.obtenerEquipoPorUsuario(usuarioActual);
	    if (equipoDelUsuario == null || equipoDelUsuario.isEmpty()) {
	        modelo.addAttribute("error", "No tienes un equipo asociado. Crea un equipo antes de unirte a un torneo.");
	        modelo.addAttribute("torneos", torneoServicio.obtenerTodosTorneos());
	        return "unirse-torneo";
	    }

	    Optional<Torneo> torneo = torneoServicio.obtenerTorneoPorId(torneoId);
	    if (torneo.isEmpty()) {
	        modelo.addAttribute("error", "El torneo no existe.");
	        modelo.addAttribute("torneos", torneoServicio.obtenerTodosTorneos());
	        return "unirse-torneo";
	    }

	    try {
	        equipoServicio.unirseATorneo(equipoDelUsuario.get(0), torneo.get());
	        modelo.addAttribute("mensaje", "Te has unido al torneo con éxito.");
	    } catch (IllegalArgumentException e) {
	        modelo.addAttribute("error", e.getMessage());
	    }

	    modelo.addAttribute("torneos", torneoServicio.obtenerTodosTorneos());
	    return "unirse-torneo";
	}


	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}
