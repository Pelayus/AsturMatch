package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("nombreUsuario")
public class MainController {

    @GetMapping("/principal")
    public String mostrarPaginaPrincipal(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        return "principal";
    }

    @GetMapping("/torneos")
    public String torneos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        return "torneos";
    }

    @GetMapping("/equipos")
    public String equipos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        return "equipos";
    }

    @GetMapping("/resultados")
    public String resultados(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        return "resultados";
    }

    @GetMapping("/contacto")
    public String contacto(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        return "contacto";
    }
}
