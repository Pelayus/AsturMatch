package com.asturmatch.proyectoasturmatch.configuracion;

import jakarta.servlet.ServletException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@SessionAttributes({"nombreUsuario"})
public class RedireccionUsuarioAutenticacionExitosa implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        DetallesUsuario detallesUsuario = (DetallesUsuario) authentication.getPrincipal();
        String username = detallesUsuario.getUsername();

        HttpSession session = request.getSession();
        session.setAttribute("nombreUsuario", username);
        
        //DATOS AL INICIO DE SESIÓN
        System.out.println(username);

        response.sendRedirect(request.getContextPath() + "/principal");
    }
}
