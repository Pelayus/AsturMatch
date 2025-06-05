package com.asturmatch.proyectoasturmatch.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asturmatch.proyectoasturmatch.configuracion.DetallesUsuario;
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.EstadoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.Partido;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.TipoDeporte;
import com.asturmatch.proyectoasturmatch.modelo.TipoMensaje;
import com.asturmatch.proyectoasturmatch.modelo.TipoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioClasificacion;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioMensaje;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes({"nombreUsuario", "UsuarioActual"})
public class TorneoController {

	@Autowired
	private ServicioUsuario S_usuario;

	@Autowired
	private ServicioTorneo S_torneo;
	
	@Autowired
	private ServicioPartido S_partido;
	
	@Autowired
	private ServicioEquipo S_equipo;
	
	@Autowired
	private ServicioMensaje S_mensaje;
	
	@Autowired
	private ServicioClasificacion S_clasificacion;
	
	@GetMapping("/torneos")
	public String torneos(Model modelo) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();

	    List<Torneo> misTorneos = S_torneo.obtenerTorneosPorCreador(usuarioActual);

	    Map<Long, Boolean> partidosGenerados = new HashMap<>();
	    for (Torneo torneo : misTorneos) {
	        boolean yaTienePartidos = !S_partido.obtenerPartidosPorTorneo(torneo).isEmpty();
	        partidosGenerados.put(torneo.getId(), yaTienePartidos);
	    }

	    modelo.addAttribute("UsuarioActual", usuarioActual.getNombreUsuario());
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
	    modelo.addAttribute("misTorneos", misTorneos);
	    modelo.addAttribute("rol", usuarioActual.getRol().toString());
	    modelo.addAttribute("partidosGenerados", partidosGenerados);
		modelo.addAttribute("estadosTorneo", EstadoTorneo.values());

	    return "torneos";
	}

	
	/**
     * Endpoint para que el organizador genere automáticamente los partidos de uno de sus torneos.
     * Se valida que el torneo tenga al menos 8 equipos; si no, se devuelve un error visible para el usuario.
     *
     * @param nombreUsuario Nombre del usuario actual.
     * @param torneoId ID del torneo para el que se desean generar los partidos.
     * @param modelo Modelo de datos para la vista.
     * @return Redirección a la vista "gestion-torneos" con mensaje de éxito o error.
     */
	@PostMapping("/torneos")
	public String generarPartidos(@RequestParam Long torneoId,
	                              RedirectAttributes redirectAttributes) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();
	    Optional<Torneo> torneo = S_torneo.obtenerTorneoPorId(torneoId);

	    if (torneo.isEmpty() || !torneo.get().getCreador().getId().equals(usuarioActual.getId())) {
	        redirectAttributes.addFlashAttribute("error", "El torneo no existe o no te pertenece.");
	        System.out.println("El torneo no existe o no te pertenece.");
	        return "redirect:/torneos";
	    }

	    List<Equipo> equipos = torneo.get().getEquipos();
	    if (equipos.size() < 8) {
	        redirectAttributes.addFlashAttribute("error", "El torneo necesita al menos 8 equipos para generar partidos.");
	        System.err.println("El torneo necesita al menos 8 equipos para generar partidos.");
	        return "redirect:/torneos";
	    }

	    try {
	    	List<Partido> partidosExistentes = S_partido.obtenerPartidosPorTorneo(torneo.get());
	    	if (!partidosExistentes.isEmpty()) {
	    	    redirectAttributes.addFlashAttribute("error", "Los partidos ya han sido generados para este torneo.");
	    	    return "redirect:/torneos";
	    	}
	        
	        S_partido.generarPartidosParaTorneo(torneoId);
	        
	        if (S_clasificacion.obtenerClasificacionPorTorneo(torneoId).isEmpty()) {
	        	TipoDeporte deporte = torneo.get().getDeporte();
	        	if(deporte==TipoDeporte.FUTBOL) {
	        		S_clasificacion.crearClasificacionParaTorneoFutbol(torneo.get());
	        	}else {
	        		S_clasificacion.crearClasificacionParaTorneoBaloncesto(torneo.get());
	        	}
	            
	        }
	        
	        redirectAttributes.addFlashAttribute("mensaje", "Partidos generados con éxito para el torneo ");
	        System.out.println("Partidos generados con éxito para torneo: " + torneoId);
	    } catch (RuntimeException ex) {
	        redirectAttributes.addFlashAttribute("error", ex.getMessage());
	    }

	    return "redirect:/torneos";
	}

	@PostMapping("/torneos/editar")
	public String editarTorneo(@RequestParam Long torneoId,
			@RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
			@RequestParam String ubicacion,
			@RequestParam EstadoTorneo estado,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		Optional<Torneo> torneoOpt = S_torneo.obtenerTorneoPorId(torneoId);
		if (torneoOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Torneo no encontrado.");
			return "redirect:/torneos";
		}

		Torneo torneo = torneoOpt.get();
		torneo.setFechaInicio(fechaInicio);
		torneo.setFechaFin(fechaFin);
		torneo.setUbicacion(ubicacion);
		torneo.setEstado(estado);

		if (estado == EstadoTorneo.FINALIZADO) {
			Usuario creador = torneo.getCreador();
			creador.setRol(Rol.USUARIO);
			S_usuario.actualizarUsuario(creador.getId(), creador);

			Optional<Usuario> optionalUsuario = S_usuario.obtenerUsuarioPorId(creador.getId());
			if (optionalUsuario.isPresent()) {
				Usuario usuarioActualizado = optionalUsuario.get();

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
			}

			List<Equipo> equiposAsociados = S_equipo.obtenerEquiposPorTorneo(torneo);
			for (Equipo equipo : equiposAsociados) {
				equipo.setTorneo(null);
				S_equipo.guardarEquipo(equipo);
			}
		}

		S_torneo.guardarTorneo(torneo);
		redirectAttributes.addFlashAttribute("mensaje", "Torneo actualizado con éxito.");
		return "redirect:/torneos";
	}

	@GetMapping("/crear-torneo")
	public String mostrarFormularioCrearTorneo(Model modelo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Long idUsuario = detallesUsuario.getUsuario().getId();

		Optional<Usuario> usuarioActual = S_usuario.obtenerUsuarioPorId(idUsuario);

		modelo.addAttribute("torneo", new Torneo());
		modelo.addAttribute("UsuarioActual", usuarioActual.get().getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.get().getNombreUsuario()));
		return "crear-torneo";
	}

	/**
	 * Creo un nuevo torneo amateur y asigno al usuario como organizador.
	 *
	 * @param torneo Objeto torneo con los datos ingresados.
	 * @param nombreUsuario Nombre del usuario actual.
	 * @param modelo Modelo de datos para la vista.
	 * @return Redirección a "/torneos" si la creación es exitosa.
	 */
	@PostMapping("/crear-torneo")
	public String crearTorneo(@ModelAttribute Torneo torneo, Model modelo, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Usuario usuarioActual = detallesUsuario.getUsuario();
		Long idUsuarioActual = usuarioActual.getId();

		usuarioActual.setRol(Rol.ORGANIZADOR);
		S_usuario.actualizarUsuario(idUsuarioActual, usuarioActual);

		Optional<Usuario> optionalUsuario = S_usuario.obtenerUsuarioPorId(idUsuarioActual);
		if (optionalUsuario.isEmpty()) {
			modelo.addAttribute("error", "No se pudo actualizar el rol del usuario.");
			return "crear-torneo";
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

		torneo.setEstado(EstadoTorneo.PENDIENTE);
		torneo.setTipoTorneo(TipoTorneo.AMATEUR);
		torneo.setCreador(usuarioActualizado);
		S_torneo.guardarTorneo(torneo);

		Mensaje mensaje = new Mensaje();
		mensaje.setUsuario(usuarioActualizado);
		mensaje.setTorneo(torneo);
		mensaje.setTipoMensaje(TipoMensaje.NOTIFICACIONES_TORNEO);
		mensaje.setFechaCreacion(LocalDateTime.now());
		mensaje.setContenido(usuarioActualizado.getNombre() + " ha creado un torneo llamado '"
				+ torneo.getNombre() + "' de " + torneo.getDeporte() +
				" los días " + torneo.getFechaInicio() + " - " + torneo.getFechaFin() +
				" en " + torneo.getUbicacion() + ".");
		S_mensaje.guardarMensaje(mensaje);

		modelo.addAttribute("mensaje", "Torneo creado con éxito. Ahora eres el ORGANIZADOR.");
		System.out.println("✅ Torneo creado con éxito. Rol actualizado a ORGANIZADOR");

		return "redirect:/torneos";
	}

	@GetMapping("/unirse-torneo")
	public String listarTorneosDisponibles(Model modelo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();

		modelo.addAttribute("UsuarioActual", usuarioActual.getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
		modelo.addAttribute("torneos", S_torneo.obtenerTodosTorneos());
		return "unirse-torneo";
	}
	
	/**
	 * Permite a un usuario unirse a un torneo si cumple con los requisitos.
	 *
	 * @param nombreUsuario Nombre del usuario actual.
	 * @param torneoId ID del torneo al que el usuario desea unirse.
	 * @param modelo Modelo de datos para la vista.
	 * @return La vista "unirse-torneo" con un mensaje de éxito o error, según corresponda.
	 */
	@PostMapping("/unirse-torneo")
	public String unirseATorneo(@ModelAttribute("torneoId") Long torneoId, Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();

	    modelo.addAttribute("UsuarioActual", usuarioActual.getNombreUsuario());
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));

	    List<Equipo> equipoDelUsuario = S_equipo.obtenerEquipoPorUsuario(usuarioActual);
	    if (equipoDelUsuario == null || equipoDelUsuario.isEmpty()) {
	        modelo.addAttribute("error", "No tienes un equipo asociado. Crea un equipo antes de unirte a un torneo.");
	        System.err.println("No tienes un equipo asociado. Crea un equipo antes de unirte a un torneo.");
	        modelo.addAttribute("torneos", S_torneo.obtenerTodosTorneos());
	        return "unirse-torneo";
	    }
	    
	    Optional<Torneo> torneo = S_torneo.obtenerTorneoPorId(torneoId);
	    
	    if (torneo.get().getEquipos().contains(equipoDelUsuario.get(0))) {
	        modelo.addAttribute("error", "Tu equipo ya está inscrito en este torneo.");
	        System.err.println("Tu equipo ya está inscrito en este torneo.");
	        modelo.addAttribute("torneos", S_torneo.obtenerTodosTorneos());
	        return "unirse-torneo";
	    }
	    if (torneo.isEmpty()) {
	        modelo.addAttribute("error", "El torneo no existe.");
	        System.err.println("El torneo no existe.");
	        modelo.addAttribute("torneos", S_torneo.obtenerTodosTorneos());
	        return "unirse-torneo";
	    }
	    
	    if (torneo.get().getCreador().getId().equals(usuarioActual.getId())) {
	        modelo.addAttribute("error", "No puedes unirte a tu propio torneo.");
	        System.err.println("No puedes unirte a tu propio torneo.");
	        modelo.addAttribute("torneos", S_torneo.obtenerTodosTorneos());
	        return "unirse-torneo";
	    }

	    try {
	        S_equipo.unirseATorneo(equipoDelUsuario.get(0), torneo.get());
	        modelo.addAttribute("mensaje", "Te has unido al torneo con éxito.");
	        System.out.println("Te has unido al torneo con éxito.");
	    } catch (IllegalArgumentException e) {
	        modelo.addAttribute("error", e.getMessage());
	    }

	    modelo.addAttribute("torneos", S_torneo.obtenerTodosTorneos());
	    return "unirse-torneo";
	}
	
	/*****************************************************/
	/*         MÉTODOS PARA USUARIO ADMINISTRADOR        */
	/*****************************************************/
	
	@GetMapping("/gestion-torneos")
	public String gestionTorneos(
	        @RequestParam(required = false) String ubicacion,
	        @RequestParam(required = false) TipoTorneo tipoTorneo,
	        @RequestParam(required = false) TipoDeporte deporte,
	        Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();
		
	    List<Torneo> torneos = S_torneo.filtrarTorneos(ubicacion, tipoTorneo, deporte);

	    modelo.addAttribute("torneos", torneos);
	    modelo.addAttribute("UsuarioActual", usuarioActual.getNombreUsuario());
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
	    modelo.addAttribute("rol", usuarioActual.getRol().toString());
	    modelo.addAttribute("ubicacion", ubicacion);
	    modelo.addAttribute("tipoTorneo", tipoTorneo);
	    modelo.addAttribute("deporte", deporte);
	    modelo.addAttribute("tiposTorneo", List.of(TipoTorneo.values()));
	    modelo.addAttribute("tiposDeporte", List.of(TipoDeporte.values()));

	    return "gestion-torneos";
	}

	
	@PostMapping("/modificar-torneo")
	public String modificarTorneo(@ModelAttribute Torneo torneo) {
	    Optional<Torneo> optionalTorneo = S_torneo.obtenerTorneoPorId(torneo.getId());

	    if (optionalTorneo.isPresent()) {
	        Torneo torneoExistente = optionalTorneo.get();
	        torneoExistente.setNombre(torneo.getNombre());
	        torneoExistente.setFechaInicio(torneo.getFechaInicio());
	        torneoExistente.setFechaFin(torneo.getFechaFin());
	        torneoExistente.setUbicacion(torneo.getUbicacion());
	        torneoExistente.setEstado(torneo.getEstado());
	        
	        S_torneo.actualizarTorneo(torneoExistente);
	    }

	    return "redirect:/gestion-torneos";
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
