package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.asturmatch.proyectoasturmatch.configuracion.DetallesUsuario;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes({"nombreUsuario", "UsuarioActual"})
public class MainController {
	
	@Autowired
	private ServicioUsuario S_usuario;
	
	/*******************************************/
	/*     LLAMADA A LA PANTALLA PRINCIPAL     */
	/*******************************************/

    @GetMapping("/principal")
	public String mostrarPaginaPrincipal(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
		Usuario usuarioActual = detallesUsuario.getUsuario();

		modelo.addAttribute("UsuarioActual", usuarioActual.getNombreUsuario());
		modelo.addAttribute("nombreUsuario", usuarioActual.getNombreUsuario());
		modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
		modelo.addAttribute("rol", usuarioActual.getRol().toString());

		return "principal";
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

