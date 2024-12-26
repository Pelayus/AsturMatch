package com.asturmatch.proyectoasturmatch.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

	@GetMapping("/principal")
	public String mostrarPaginaPrincipal(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
		modelo.addAttribute("mensajeBienvenida", "Bienvenido, " + nombreUsuario
				+ " a AsturMatch el mejor lugar para crear torneos deportivos en Asturias. ¡Únete y crea tus propias competiciones hoy mismo!");
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
