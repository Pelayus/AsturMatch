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
	    Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);
	    
	    usuarioActual.setRol(Rol.JUGADOR);
	    usuarioServicio.guardarUsuario(usuarioActual);

	    equipo.setJugadores(List.of(usuarioActual));
	    equipo.setTipoEquipo(TipoEquipo.AMATEUR);
	    equipo.setTorneo(null);
	    equipo.setFechaCreacion(LocalDate.now()); // Asigno la fecha actual

	    equipoServicio.guardarEquipo(equipo);

	    modelo.addAttribute("mensaje", "Equipo creado con éxito");
	    return "redirect:/equipos";
	}
	
	@GetMapping("/crear-equipopro")
	public String mostrarFormularioCrearEquipoPro(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("equipo", new Equipo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
		return "crear-equipopro";
	}

	@PostMapping("/crear-equipopro")
	public String crearEquipoPro(@ModelAttribute Equipo equipo, @ModelAttribute("nombreUsuario") String nombreUsuario,
			Model modelo) {
		// Obtengo el usuario actual desde el servicio
		Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);
		
		usuarioActual.setRol(Rol.JUGADOR);
		usuarioServicio.guardarUsuario(usuarioActual);

		equipo.setJugadores(List.of(usuarioActual));
		equipo.setTipoEquipo(TipoEquipo.PROFESIONAL);
		equipo.setTorneo(null);
		equipo.setFechaCreacion(LocalDate.now()); // Asigno la fecha actual

		equipoServicio.guardarEquipo(equipo);

		modelo.addAttribute("mensaje", "Equipo creado con éxito");
		return "redirect:/equipos";
	}
	
	@GetMapping("/unirse-equipo")
	public String mostrarEquiposAmateur(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
	    List<Equipo> equiposAmateur = equipoServicio.obtenerEquiposPorTipo(TipoEquipo.AMATEUR);
	    modelo.addAttribute("equipos", equiposAmateur);
	    modelo.addAttribute("UsuarioActual", nombreUsuario);
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
	    return "unirse-equipo";
	}
	
	@PostMapping("/unirse-equipo")
	public String unirseAEquipo(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                            @ModelAttribute("equipoId") Long equipoId, Model modelo) {
	    Usuario usuarioActual = usuarioServicio.obtenerUsuarioPorNombre(nombreUsuario);
	    
	    Optional<Equipo> equipoOptional = equipoServicio.obtenerEquipoPorId(equipoId);

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
	    equipoServicio.guardarEquipo(equipo);

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
