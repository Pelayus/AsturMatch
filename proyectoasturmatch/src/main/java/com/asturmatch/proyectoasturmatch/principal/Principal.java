package com.asturmatch.proyectoasturmatch.principal;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Principal implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n\n\n\n\n\t\t\t\t\033[1;35m************************************************\033[0m");
		System.out.println("\t\t\t\t\033[1;36mProyecto : Proyecto AsturMatch (Con Spring)\033[0m");
		System.out.println("\t\t\t\t\033[1;33mAutor: Pelayo Rodríguez Álvarez\033[0m");
		System.out.printf("\t\t\t\t\033[1;32mFecha: %-38s\033[0m\n", LocalDate.now().toString());
		System.out.println("\t\t\t\t\033[1;35m************************************************\033[0m");
		System.out.println("\n\t\t\t\033[1;36m-- Bienvenido/a a AsturMatch (Torneos Asturianos Personalizados) --\033[0m");
	}
}
