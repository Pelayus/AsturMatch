package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
    public String mostrarPaginaPrincipal() {
        return "principal";
    }

    @GetMapping("/torneos")
    public String torneos() {
        return "torneos";
    }

    @GetMapping("/equipos")
    public String equipos() {
        return "equipos";
    }

    @GetMapping("/resultados")
    public String resultados() {
        return "resultados";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }
}
