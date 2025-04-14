package com.asturmatch.proyectoasturmatch.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.EstadoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.TipoMensaje;
import com.asturmatch.proyectoasturmatch.modelo.TipoTorneo;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioMensaje;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
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
	
	@GetMapping("/torneos")
    public String torneos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);

        List<Torneo> misTorneos = S_torneo.obtenerTorneosPorCreador(usuarioActual);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("misTorneos", misTorneos);
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
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
	public String generarPartidos(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                              @RequestParam Long torneoId,
	                              RedirectAttributes redirectAttributes) {
	    Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
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
	        S_partido.generarPartidosParaTorneo(torneoId);
	        redirectAttributes.addFlashAttribute("mensaje", "Partidos generados con éxito para el torneo ");
	        System.out.println("Partidos generados con éxito para torneo: " + torneoId);
	    } catch (RuntimeException ex) {
	        redirectAttributes.addFlashAttribute("error", ex.getMessage());
	    }

	    return "redirect:/torneos";
	}

	@PostMapping("/torneos/editar")
	public String editarTorneo(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                           @RequestParam Long torneoId,
	                           @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
	                           @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
	                           @RequestParam String ubicacion,
	                           RedirectAttributes redirectAttributes) {
	    Optional<Torneo> torneoOpt = S_torneo.obtenerTorneoPorId(torneoId);
	    
	    Torneo torneo = torneoOpt.get();
	    torneo.setFechaInicio(fechaInicio);
	    torneo.setFechaFin(fechaFin);
	    torneo.setUbicacion(ubicacion);
	    
	    S_torneo.guardarTorneo(torneo);
	    redirectAttributes.addFlashAttribute("mensaje", "Torneo actualizado con éxito.");
	    return "redirect:/torneos";
	}



	@GetMapping("/crear-torneo")
	public String mostrarFormularioCrearTorneo(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("torneo", new Torneo());
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
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
	public String crearTorneo(@ModelAttribute Torneo torneo, @ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);

		usuarioActual.setRol(Rol.ORGANIZADOR);
		S_usuario.guardarUsuario(usuarioActual);

		torneo.setEstado(EstadoTorneo.PENDIENTE);
		torneo.setTipoTorneo(TipoTorneo.AMATEUR);
		torneo.setCreador(usuarioActual);
		S_torneo.guardarTorneo(torneo);
		
		Mensaje mensaje = new Mensaje();
		mensaje.setUsuario(usuarioActual);
		mensaje.setTorneo(torneo);
		mensaje.setTipoMensaje(TipoMensaje.NOTIFICACIONES_TORNEO);
		mensaje.setFechaCreacion(LocalDateTime.now());
		mensaje.setContenido(usuarioActual.getNombre() + " ha creado un torneo llamado '" 
		    + torneo.getNombre() + "' de " + torneo.getDeporte() + 
		    " los días " + torneo.getFechaInicio() + " - " + torneo.getFechaFin() +
		    " en " + torneo.getUbicacion() + ".");

		S_mensaje.guardarMensaje(mensaje);


		modelo.addAttribute("mensaje", "Torneo creado con éxito. Ahora eres el ORGANIZADOR.");
		System.out.println("Torneo creado con éxito. Ahora eres el ORGANIZADOR.");
		return "redirect:/torneos";
	}

	@GetMapping("/unirse-torneo")
	public String listarTorneosDisponibles(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("UsuarioActual", nombreUsuario);
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
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
	public String unirseATorneo(@ModelAttribute("nombreUsuario") String nombreUsuario,
	                            @ModelAttribute("torneoId") Long torneoId, Model modelo) {
	    modelo.addAttribute("UsuarioActual", nombreUsuario);
	    modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));

	    Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);

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

	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}
