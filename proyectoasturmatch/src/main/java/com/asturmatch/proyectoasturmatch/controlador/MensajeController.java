package com.asturmatch.proyectoasturmatch.controlador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.TipoMensaje;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioEquipo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioMensaje;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class MensajeController {
	
	@Autowired
	private ServicioMensaje S_mensaje;
	
	@Autowired
	private ServicioUsuario S_usuario;
	
	@Autowired
	private ServicioEquipo S_equipo;
	
	@GetMapping("/mensajes")
	public String mensajes(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
	    
	    Usuario emisor = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
	    List<Equipo> equipos = S_equipo.obtenerEquipoPorUsuario(emisor);

	    Equipo equipo = equipos.get(0);
	    List<Usuario> companeros = equipo.getJugadores().stream()
	            .filter(u -> !u.getId().equals(emisor.getId()))
	            .toList();

	    List<Mensaje> mensajesRecibidos = S_mensaje.obtenerMensajesRecibidos(emisor);

	    modelo.addAttribute("companeros", companeros);
	    modelo.addAttribute("UsuarioActual", nombreUsuario);
	    modelo.addAttribute("mensajesRecibidos", mensajesRecibidos);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("rol", emisor.getRol().toString());

	    return "mensajes";
	}

	/**
	 * Maneja la solicitud POST para enviar un mensaje de un usuario a otro que son del mismo equipo.
	 * 
	 * Este método realiza las siguientes validaciones antes de enviar el mensaje:
	 * <ul>
	 *     <li>Verifica que el usuario emisor pertenezca al menos a un equipo.</li>
	 *     <li>Valida que el receptor también forme parte del mismo equipo.</li>
	 * </ul>
	 * Si las validaciones se cumplen, crea y guarda el mensaje con la fecha y tipo correspondiente.
	 *
	 * @param nombreUsuario el nombre del usuario que envía el mensaje
	 * @param receptorId el ID del usuario receptor
	 * @param contenido el texto del mensaje a enviar
	 * @param modelo el objeto {@link Model} para pasar atributos al frontend
	 * @return una redirección a la vista de mensajes, con mensajes de éxito o error según el caso
	 */
	@PostMapping("/enviar-mensaje")
	public String enviarMensaje(@RequestParam String nombreUsuario,
	                            @RequestParam Long receptorId,
	                            @RequestParam String contenido,
	                            Model modelo) {
	    Usuario emisor = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
	    Optional <Usuario> receptorOptional = S_usuario.obtenerUsuarioPorId(receptorId);
	    Usuario receptor = receptorOptional.get();

	    List<Equipo> equipos = S_equipo.obtenerEquipoPorUsuario(emisor);
	    if (equipos.isEmpty()) {
	        modelo.addAttribute("error", "No formas parte de ningún equipo.");
	        return "redirect:/mensajes";
	    }

	    Equipo equipo = equipos.get(0);
	    if (!equipo.getJugadores().contains(receptor)) {
	        modelo.addAttribute("error", "Solo puedes enviar mensajes a miembros de tu equipo.");
	        return "redirect:/mensajes";
	    }

	    Mensaje mensaje = new Mensaje();
	    mensaje.setUsuario(emisor);
	    mensaje.setReceptor(receptor);
	    mensaje.setContenido(contenido);
	    mensaje.setFechaCreacion(LocalDateTime.now());
	    mensaje.setTipoMensaje(TipoMensaje.MENSAJE_EQUIPO);

	    S_mensaje.guardarMensaje(mensaje);

	    modelo.addAttribute("mensaje", "Mensaje enviado con éxito.");
	    return "redirect:/mensajes";
	}
	
	// Método para obtener la primera letra
    private String obtenerPrimeraLetra(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            return String.valueOf(nombre.charAt(0)).toUpperCase();
        }
        return "";
    }
}
