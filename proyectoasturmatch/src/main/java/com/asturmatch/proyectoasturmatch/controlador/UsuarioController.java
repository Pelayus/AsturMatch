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
	        return "redirect:/principal";
	    } catch (IllegalArgumentException e) {
	        modelo.addAttribute("errorRegistro", e.getMessage());
	        return "registro";
	    }
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
	
	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}

