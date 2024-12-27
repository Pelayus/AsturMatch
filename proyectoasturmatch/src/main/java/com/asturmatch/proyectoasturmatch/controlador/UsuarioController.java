package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        usuario.setRol(Rol.JUGADOR); // Establezco por defecto el rol de JUGADOR 
        usuarioServicio.guardarUsuario(usuario);
        modelo.addAttribute("nombreUsuario", usuario.getNombre());
        return "redirect:/principal";
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
            return "redirect:/principal";
        } else {
            modelo.addAttribute("error", "Email o contraseña incorrectos");
            return "redirect:/iniciosesion";
        }
    }
}

