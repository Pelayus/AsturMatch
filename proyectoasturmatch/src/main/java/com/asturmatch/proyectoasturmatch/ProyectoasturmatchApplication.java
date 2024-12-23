package com.asturmatch.proyectoasturmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProyectoasturmatchApplication {
	
	@Bean
	public Principal applicationStartupRunner(){
		return new Principal();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProyectoasturmatchApplication.class, args);
	}

}
