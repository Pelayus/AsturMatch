package com.asturmatch.proyectoasturmatch.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.configuracion.DetallesUsuario;
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.TipoEquipo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes({"nombreUsuario", "UsuarioActual"})
public class EquipoController {

	@Autowired
	private ServicioUsuario S_usuario;

	@Autowired
	private ServicioEquipo S_equipo;
	
	/*****************************************************/
	/*      MÉTODOS PARA USUARIOS Y ORGANIZADORES        */
	/*****************************************************/
	
	@GetMapping("/equipos")
    public String equipos(Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Usuario usuarioActual = detallesUsuario.getUsuario();
		List<Equipo> equiposAmateur = S_equipo.obtenerEquiposPorTipo(TipoEquipo.AMATEUR);

		modelo.addAttribute("equipos", equiposAmateur);
		modelo.addAttribute("nombreUsuario", usuarioActual.getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
		modelo.addAttribute("rol", usuarioActual.getRol().toString());
        return "equipos";
    }

	@GetMapping("/crear-equipo")
	public String mostrarFormularioCrearEquipo(Model modelo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Long idUsuario = detallesUsuario.getUsuario().getId();

		Optional<Usuario> usuarioActualizado = S_usuario.obtenerUsuarioPorId(idUsuario);

		modelo.addAttribute("equipo", new Equipo());
		modelo.addAttribute("UsuarioActual", usuarioActualizado.get().getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActualizado.get().getNombreUsuario()));
		modelo.addAttribute("rol", usuarioActualizado.get().getRol().toString());

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
	public String crearEquipo(@ModelAttribute Equipo equipo, Model modelo, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Usuario usuarioActual = detallesUsuario.getUsuario();

		List<Equipo> equipoDelUsuario = S_equipo.obtenerEquipoPorUsuario(usuarioActual);
		if (!equipoDelUsuario.isEmpty()) {
			modelo.addAttribute("error", "No puedes crear un equipo porque ya perteneces a uno.");
			return "crear-equipo";
		}

		Long idUsuarioActual = usuarioActual.getId();
		usuarioActual.setRol(Rol.JUGADOR);
		S_usuario.actualizarUsuario(idUsuarioActual, usuarioActual);

		Optional<Usuario> optionalUsuario = S_usuario.obtenerUsuarioPorId(idUsuarioActual);
		if (optionalUsuario.isEmpty()) {
			modelo.addAttribute("error", "Error al actualizar el rol del usuario.");
			return "crear-equipo";
		}

		Usuario usuarioActualizado = optionalUsuario.get();

		UserDetails nuevoUserDetails = new DetallesUsuario(usuarioActualizado);
		Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
				nuevoUserDetails,
				auth.getCredentials(),
				nuevoUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(nuevaAuth);

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.setAttribute(
					HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
		}

		equipo.setJugadores(List.of(usuarioActualizado));
		equipo.setTipoEquipo(TipoEquipo.AMATEUR);
		equipo.setTorneo(null);
		equipo.setFechaCreacion(LocalDate.now());
		S_equipo.guardarEquipo(equipo);

		modelo.addAttribute("mensaje", "Equipo creado con éxito");
		return "redirect:/torneos";
	}
	
	@GetMapping("/crear-equipopro")
	public String mostrarFormularioCrearEquipoPro(Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Long idUsuario = detallesUsuario.getUsuario().getId();

		Optional<Usuario> usuarioActualizado = S_usuario.obtenerUsuarioPorId(idUsuario);

		modelo.addAttribute("equipo", new Equipo());
		modelo.addAttribute("UsuarioActual", usuarioActualizado.get().getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActualizado.get().getNombreUsuario()));
		modelo.addAttribute("rol", usuarioActualizado.get().getRol().toString());
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
	public String crearEquipoPro(@ModelAttribute Equipo equipo, Model modelo, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Usuario usuarioActual = detallesUsuario.getUsuario();

		List<Equipo> equipoDelUsuario = S_equipo.obtenerEquipoPorUsuario(usuarioActual);
		if (!equipoDelUsuario.isEmpty()) {
			modelo.addAttribute("error", "No puedes crear un equipo porque ya perteneces a uno.");
			return "crear-equipopro";
		}

		Long idUsuarioActual = usuarioActual.getId();
		usuarioActual.setRol(Rol.JUGADOR);
		S_usuario.actualizarUsuario(idUsuarioActual, usuarioActual);

		Optional<Usuario> optionalUsuario = S_usuario.obtenerUsuarioPorId(idUsuarioActual);
		if (optionalUsuario.isEmpty()) {
			modelo.addAttribute("error", "Error al actualizar el rol del usuario.");
			return "crear-equipopro";
		}

		Usuario usuarioActualizado = optionalUsuario.get();

		UserDetails nuevoUserDetails = new DetallesUsuario(usuarioActualizado);
		Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
				nuevoUserDetails,
				auth.getCredentials(),
				nuevoUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(nuevaAuth);

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.setAttribute(
					HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
		}

		equipo.setJugadores(List.of(usuarioActualizado));
		equipo.setTipoEquipo(TipoEquipo.PROFESIONAL);
		equipo.setTorneo(null);
		equipo.setFechaCreacion(LocalDate.now());
		S_equipo.guardarEquipo(equipo);

		modelo.addAttribute("mensaje", "Equipo creado con éxito");
		return "redirect:/torneos";
	}
	
	@GetMapping("/unirse-equipo")
	public String mostrarEquiposAmateur(Model modelo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Long idUsuario = detallesUsuario.getUsuario().getId();

		Optional <Usuario> usuarioActualizado = S_usuario.obtenerUsuarioPorId(idUsuario);

		List<Equipo> equiposAmateur = S_equipo.obtenerEquiposPorTipo(TipoEquipo.AMATEUR);

		modelo.addAttribute("equipos", equiposAmateur);
		modelo.addAttribute("UsuarioActual", usuarioActualizado.get().getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActualizado.get().getNombreUsuario()));
		modelo.addAttribute("rol", usuarioActualizado.get().getRol().toString());

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
	public String unirseAEquipo(@ModelAttribute("equipoId") Long equipoId, Model modelo, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Usuario usuarioActual = detallesUsuario.getUsuario();
		Long idUsuario = usuarioActual.getId();

		Optional<Equipo> equipoOptional = S_equipo.obtenerEquipoPorId(equipoId);

		if (equipoOptional.isEmpty()) {
			modelo.addAttribute("error", "El equipo no existe.");
			return "redirect:/unirse-equipo";
		}

		Equipo equipo = equipoOptional.get();

		if (equipo.getJugadores().contains(usuarioActual)) {
			modelo.addAttribute("error", "Ya eres parte de este equipo.");
			return "redirect:/unirse-equipo";
		}

		equipo.getJugadores().add(usuarioActual);
		S_equipo.guardarEquipo(equipo);

		usuarioActual.setRol(Rol.JUGADOR);
		S_usuario.actualizarUsuario(idUsuario, usuarioActual);
		Optional<Usuario> optionalUsuario = S_usuario.obtenerUsuarioPorId(idUsuario);
		if (optionalUsuario.isEmpty()) {
			modelo.addAttribute("error", "Error al actualizar el rol del usuario.");
			return "redirect:/unirse-equipo";
		}

		Usuario usuarioActualizado = optionalUsuario.get();

		UserDetails nuevoUserDetails = new DetallesUsuario(usuarioActualizado);
		Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
				nuevoUserDetails,
				auth.getCredentials(),
				nuevoUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(nuevaAuth);

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.setAttribute(
					HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
		}

		modelo.addAttribute("mensaje", "Te has unido al equipo con éxito. Ahora eres JUGADOR.");
		System.out.println("✅ Usuario se unió al equipo. Rol actualizado a JUGADOR");

		return "redirect:/torneos";
	}

	
	/*****************************************************/
	/*         MÉTODOS PARA USUARIO ADMINISTRADOR        */
	/*****************************************************/
	
	@GetMapping("/gestion-equipos")
	public String gestionEquipos(Model modelo) {
		List<Equipo> equiposAmateur = S_equipo.obtenerEquiposPorTipo(TipoEquipo.AMATEUR);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();

		modelo.addAttribute("equipos", equiposAmateur);
		modelo.addAttribute("UsuarioActual", usuarioActual.getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
		modelo.addAttribute("rol", usuarioActual.getRol().toString());
		return "gestion-equipos";
	}
	
	@PostMapping("/modificar-equipo")
	public String modificarEquipo(@ModelAttribute Equipo equipo) {
	    Optional<Equipo> optionalEquipo = S_equipo.obtenerEquipoPorId(equipo.getId());

	    if (optionalEquipo.isPresent()) {
	        Equipo equipoExistente = optionalEquipo.get();
	        equipoExistente.setNombre(equipo.getNombre());
	        equipoExistente.setFechaCreacion(equipo.getFechaCreacion());

	        S_equipo.modificarEquipo(equipoExistente);
	    }

	    return "redirect:/gestion-equipos";
	}
	
	/************************************/
	/*       MÉTODOS DE AYUDA           */
	/************************************/

	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}
