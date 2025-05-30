package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class MainController {
	
	@Autowired
	private ServicioUsuario S_usuario;

    @GetMapping("/principal")
    public String mostrarPaginaPrincipal(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
    	Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
        return "principal";
    }

    @GetMapping("/contacto")
    public String contacto(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
    	Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
        return "contacto";
    }

    // Método para obtener la primera letra
    private String obtenerPrimeraLetra(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            return String.valueOf(nombre.charAt(0)).toUpperCase();
        }
        return "";
    }
}

