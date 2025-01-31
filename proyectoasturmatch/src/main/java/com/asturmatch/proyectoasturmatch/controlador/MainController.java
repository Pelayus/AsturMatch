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
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        return "principal";
    }

    @GetMapping("/torneos")
    public String torneos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        return "torneos";
    }

    @GetMapping("/equipos")
    public String equipos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        return "equipos";
    }

    @GetMapping("/resultados")
    public String resultados(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        return "resultados";
    }

    @GetMapping("/contacto")
    public String contacto(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
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

