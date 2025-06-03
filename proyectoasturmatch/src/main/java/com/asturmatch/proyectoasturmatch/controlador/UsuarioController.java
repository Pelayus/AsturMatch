package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;
import org.springframework.stereotype.Controller;

@Controller
@SessionAttributes("nombreUsuario")
public class UsuarioController {

	@Autowired
	private ServicioUsuario usuarioServicio;
	
	/*****************************************************/
	/*      INICIO DE SESIÓN Y REGISTRO DE USUARIOS      */
	/*****************************************************/

	@GetMapping("/iniciosesion")
	public String mostrarFormularioLogin() {
		return "iniciosesion";
	}

	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model modelo) {
		modelo.addAttribute("usuario", new Usuario());
		return "registro";
	}

	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute Usuario usuario, Model modelo) {
	    try {
	        usuario.setRol(Rol.USUARIO);
	        usuarioServicio.guardarUsuario(usuario);
	        modelo.addAttribute("nombreUsuario", usuario.getNombre());
	        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuario.getNombre()));
	        return "redirect:/iniciosesion";
	    } catch (IllegalArgumentException e) {
	        modelo.addAttribute("errorRegistro", e.getMessage());
	        return "registro";
	    }
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

