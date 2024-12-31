package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.modelo.EstadoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class TorneoController {

	@Autowired
	private ServicioUsuario usuarioServicio;

	@Autowired
	private ServicioTorneo torneoServicio;

	@GetMapping("/crear-torneo")
	public String mostrarFormularioCrearTorneo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("torneo", new Torneo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		return "crear-torneo";
	}

	@PostMapping("/crear-torneo")
	public String crearTorneo(@ModelAttribute Torneo torneo, @ModelAttribute("nombreUsuario") String nombreUsuario,
			Model modelo) {
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

	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}
