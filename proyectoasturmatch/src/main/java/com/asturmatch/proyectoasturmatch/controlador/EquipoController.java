package com.asturmatch.proyectoasturmatch.controlador;

import java.time.LocalDate;
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
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.TipoEquipo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class EquipoController {

	@Autowired
	private ServicioUsuario S_usuario;

	@Autowired
	private ServicioEquipo S_equipo;

	@GetMapping("/crear-equipo")
	public String mostrarFormularioCrearEquipo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("equipo", new Equipo());
		Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
		return "crear-equipo";
	}

	/**
	 * Creo un nuevo equipo amateur si el usuario no pertenece ya a otro equipo.
	 *
	 * @param equipo Objeto equipo con los datos ingresados.
	 * @param nombreUsuario Nombre del usuario actual.
	 * @param modelo Modelo de datos para la vista.
	 * @return Redirección a "/equipos" si la creación es exitosa, 
	 *         o la vista "crear-equipo" con un mensaje de error si el usuario ya pertenece a un equipo.
	 */
	@PostMapping("/crear-equipo")
	public String crearEquipo(@ModelAttribute Equipo equipo, @ModelAttribute("nombreUsuario") String nombreUsuario,
	                          Model modelo) {
	    Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
	    
	    List<Equipo> equipoDelUsuario = S_equipo.obtenerEquipoPorUsuario(usuarioActual);
	    if (!equipoDelUsuario.isEmpty()) {
	        modelo.addAttribute("error", "No puedes crear un equipo porque ya perteneces a uno.");
	        return "crear-equipo";
	    }
	    
	    Long idUsuarioActual = usuarioActual.getId();
	    usuarioActual.setRol(Rol.JUGADOR);
	    S_usuario.actualizarUsuario(idUsuarioActual,usuarioActual);

	    equipo.setJugadores(List.of(usuarioActual));
	    equipo.setTipoEquipo(TipoEquipo.AMATEUR);
	    equipo.setTorneo(null);
	    equipo.setFechaCreacion(LocalDate.now());

	    S_equipo.guardarEquipo(equipo);

	    modelo.addAttribute("mensaje", "Equipo creado con éxito");
	    return "redirect:/equipos";
	}

	
	@GetMapping("/crear-equipopro")
	public String mostrarFormularioCrearEquipoPro(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("equipo", new Equipo());
		Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
		return "crear-equipopro";
	}

	
	/**
	 * Creo un nuevo equipo profesional si el usuario no pertenece ya a otro equipo.
	 *
	 * @param equipo Objeto equipo con los datos ingresados.
	 * @param nombreUsuario Nombre del usuario actual.
	 * @param modelo Modelo de datos para la vista.
	 * @return Redirección a "/equipos" si la creación es exitosa,
	 *         o la vista "crear-equipo" con un mensaje de error si el usuario ya pertenece a un equipo.
	 */
	@PostMapping("/crear-equipopro")
	public String crearEquipoPro(@ModelAttribute Equipo equipo, @ModelAttribute("nombreUsuario") String nombreUsuario,
			Model modelo) {
		Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
		
		List<Equipo> equipoDelUsuario = S_equipo.obtenerEquipoPorUsuario(usuarioActual);
	    if (!equipoDelUsuario.isEmpty()) {
	        modelo.addAttribute("error", "No puedes crear un equipo porque ya perteneces a uno.");
	        System.err.println("No puedes crear un equipo porque ya perteneces a uno.");
	        return "crear-equipo";
	    }
		
		usuarioActual.setRol(Rol.JUGADOR);
		S_usuario.guardarUsuario(usuarioActual);

		equipo.setJugadores(List.of(usuarioActual));
		equipo.setTipoEquipo(TipoEquipo.PROFESIONAL);
		equipo.setTorneo(null);
		equipo.setFechaCreacion(LocalDate.now());

		S_equipo.guardarEquipo(equipo);

		modelo.addAttribute("mensaje", "Equipo creado con éxito");
		return "redirect:/equipos";
	}
	
	@GetMapping("/unirse-equipo")
	public String mostrarEquiposAmateur(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
	    List<Equipo> equiposAmateur = S_equipo.obtenerEquiposPorTipo(TipoEquipo.AMATEUR);
	    modelo.addAttribute("equipos", equiposAmateur);
	    Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
	    return "unirse-equipo";
	}
	
	/**
	 * Permite a un usuario unirse a un equipo amateur si no es miembro de él.
	 *
	 * @param nombreUsuario Nombre del usuario actual.
	 * @param equipoId ID del equipo al que desea unirse.
	 * @param modelo Modelo de datos para la vista.
	 * @return Redirección a "/equipos" si la unión es exitosa,
	 *         o a "unirse-equipo" con un mensaje de error si el usuario ya está en el equipo o el equipo no existe.
	 */
	@PostMapping("/unirse-equipo")
	public String unirseAEquipo(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                            @ModelAttribute("equipoId") Long equipoId, Model modelo) {
	    Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
	    
	    Optional<Equipo> equipoOptional = S_equipo.obtenerEquipoPorId(equipoId);

	    // Verificar si el equipo existe
	    if (equipoOptional.isEmpty()) {
	        modelo.addAttribute("error", "El equipo no existe.");
	        return "redirect:/unirse-equipo";
	    }

	    Equipo equipo = equipoOptional.get();

	    // Verificar si el usuario ya está en el equipo
	    if (equipo.getJugadores().contains(usuarioActual)) {
	        modelo.addAttribute("error", "Ya eres parte de este equipo.");
	        return "redirect:/unirse-equipo";
	    }

	    equipo.getJugadores().add(usuarioActual);
	    S_equipo.guardarEquipo(equipo);
	    
	    usuarioActual.setRol(Rol.JUGADOR);
	    S_usuario.actualizarUsuario(usuarioActual.getId(), usuarioActual); 

	    modelo.addAttribute("mensaje", "Te has unido al equipo con éxito.");
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
