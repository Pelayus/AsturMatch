package com.asturmatch.proyectoasturmatch.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.asturmatch.proyectoasturmatch.modelo")
@EnableJpaRepositories(basePackages = "com.asturmatch.proyectoasturmatch.repositorios")
public class ProyectoasturmatchApplication {
	
	@Bean
	public Principal applicationStartupRunner(){
		return new Principal();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProyectoasturmatchApplication.class, args);
	}

}
