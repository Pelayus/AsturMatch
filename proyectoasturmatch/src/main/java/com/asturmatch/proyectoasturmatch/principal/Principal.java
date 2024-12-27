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

        try {
            String url = "http://localhost:8080/iniciosesion";
            System.out.println("\n\t\t\t\033[1;36mLink de Entrada --> " + url + "\033[0m");
            
            // Intenta abrir el navegador mediante el sistema operativo
            ProcessBuilder pb = new ProcessBuilder();
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                pb.command("cmd", "/c", "start", url);
            } else if (os.contains("mac")) {
                pb.command("open", url);
            } else if (os.contains("nix") || os.contains("nux")) {
                pb.command("xdg-open", url);
            } else {
                throw new UnsupportedOperationException("Sistema operativo no soportado para abrir navegadores.");
            }
            pb.start();
            System.out.println("\n\t\t\t\033[1;32mAbriendo el navegador con el enlace...\033[0m");
        } catch (Exception e) {
            System.out.println("\n\t\t\t\033[1;31mError al intentar abrir el navegador:\033[0m");
            e.printStackTrace();
        }

    }
}


