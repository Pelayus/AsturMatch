package com.asturmatch.proyectoasturmatch.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Controller;

@Controller
@SessionAttributes("nombreUsuario")
public class UsuarioController {

	@Autowired
	private ServicioUsuario usuarioServicio;
	
	@Autowired
	private ServicioTorneo torneoServicio;
	
	@Autowired
	private ServicioEquipo equipoServicio;

	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model modelo) {
		modelo.addAttribute("usuario", new Usuario());
		return "registro";
	}

	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute Usuario usuario, Model modelo) {
		usuario.setRol(Rol.USUARIO); // Establezco por defecto el rol de JUGADOR
		usuarioServicio.guardarUsuario(usuario);
		modelo.addAttribute("nombreUsuario", usuario.getNombre());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuario.getNombre()));
		return "redirect:/principal";
	}

	@GetMapping("/iniciosesion")
	public String mostrarFormularioLogin(Model modelo) {
		modelo.addAttribute("usuario", new Usuario());
		return "iniciosesion";
	}

	@PostMapping("/iniciosesion")
	public String iniciarSesion(@ModelAttribute Usuario usuario, Model modelo) {
	    Usuario usuarioAutenticado = usuarioServicio.obtenerUsuarioPorEmail(usuario.getEmail());
	    if (usuarioAutenticado != null && usuarioAutenticado.getContraseña().equals(usuario.getContraseña())) {
	        modelo.addAttribute("nombreUsuario", usuarioAutenticado.getNombre());
	        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioAutenticado.getNombre()));
	        return "redirect:/principal";
	    } else {
	        modelo.addAttribute("error", "Email o contraseña incorrectos");
	        return "redirect:/iniciosesion";
	    }
	}
	
	@GetMapping("/crear-torneo")
	public String mostrarFormularioCrearTorneo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("torneo", new Torneo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		return "crear-torneo";
	}
	
	@PostMapping("/crear-torneo")
	public String crearTorneo(@ModelAttribute Torneo torneo, Model modelo) {
	    torneo.setEstado(EstadoTorneo.PENDIENTE); // Estado por defecto
	    torneoServicio.guardarTorneo(torneo);
	    modelo.addAttribute("mensaje", "Torneo creado con éxito");
	    return "redirect:/torneos";
	}
	
	@GetMapping("/unirse-torneo")
	public String listarTorneosDisponibles(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
	    modelo.addAttribute("UsuarioActual", nombreUsuario);
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
	    modelo.addAttribute("torneos", torneoServicio.obtenerTodosTorneos()); // Añado la lista de torneos
	    return "unirse-torneo";
	}
	
	@GetMapping("/crear-equipo")
	public String mostrarFormularioCrearEquipo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("equipo", new Equipo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		return "crear-equipo";
	}
	@PostMapping("/crear-equipo")
	public String crearEquipo(@ModelAttribute Equipo equipo, @ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
	    // Obtengo el usuario actual desde el servicio
	    Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);

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

