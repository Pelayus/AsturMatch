package com.asturmatch.proyectoasturmatch.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.SessionCookieConfig;

import org.springframework.boot.web.servlet.ServletContextInitializer;

@Configuration
public class ConfiguracionSesion extends AbstractHttpSessionApplicationInitializer implements ServletContextInitializer {

	/**
	 * Configuramos la sesión HTTP al iniciar la aplicación.
	 * Establecemos un tiempo de inactividad máximo de 6 horas (21600 segundos)
	 * antes de que la sesión caduque automáticamente, y definimos la cookie de sesión
	 * como HttpOnly para mejorar la seguridad.
	 *
	 * @param servletContext el contexto del servlet proporcionado por el contenedor
	 */
	@Override
	public void onStartup(ServletContext servletContext) {
	    servletContext.setSessionTimeout(21600);

	    SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
	    sessionCookieConfig.setHttpOnly(true);
	}
}

