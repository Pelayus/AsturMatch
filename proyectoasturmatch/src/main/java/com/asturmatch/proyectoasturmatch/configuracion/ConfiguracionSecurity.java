package com.asturmatch.proyectoasturmatch.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class ConfiguracionSecurity {

    @Autowired
    @Lazy
    private ServicioDetallesUsuario S_detallesUsuario;
    
    @Autowired
    private RedireccionUsuarioAutenticacionExitosa redireccionautenticaionExitosa;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(S_detallesUsuario);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/iniciosesion", "/registro", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/gestion-torneos", "/gestion-equipos", "/mensajes-contacto").hasRole("ADMIN")
                .requestMatchers("/equipos", "/unirse-equipo", "/crear-equipo", "/crear-equipopro", "/crear-torneo").hasRole("USUARIO")
                .requestMatchers("/mensajes").hasRole("JUGADOR")
                .requestMatchers("/partidos").hasAnyRole("ORGANIZADOR","JUGADOR")
                .requestMatchers("/torneos", "/contacto").hasAnyRole("USUARIO", "ORGANIZADOR", "JUGADOR")
                .requestMatchers("/principal").hasAnyRole("USUARIO", "ORGANIZADOR", "JUGADOR", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/iniciosesion")
                .loginProcessingUrl("/iniciosesion")
                .usernameParameter("nombreUsuario")
                .passwordParameter("contrasena")
                .successHandler((request, response, authentication) -> {
                    redireccionautenticaionExitosa.onAuthenticationSuccess(request, response, authentication);
                })
                .failureUrl("/iniciosesion?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/iniciosesion?logout=true")
                .permitAll()
            );

        return http.build();
    }

    // Maneja eventos de sesión HTTP
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}

