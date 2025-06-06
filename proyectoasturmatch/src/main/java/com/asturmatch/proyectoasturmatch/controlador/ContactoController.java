package com.asturmatch.proyectoasturmatch.controlador;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asturmatch.proyectoasturmatch.configuracion.DetallesUsuario;
import com.asturmatch.proyectoasturmatch.modelo.Mensaje;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.TipoMensaje;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioMensaje;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes({ "nombreUsuario", "UsuarioActual" })
public class ContactoController {

    @Autowired
    private ServicioMensaje S_mensaje;

    @Autowired
    private ServicioUsuario S_usuario;

    /*******************************************/
    /* LLAMADA A LA PANTALLA DE CONTACTO */
    /*******************************************/

    @GetMapping("/contacto")
    public String contacto(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();

        modelo.addAttribute("nombreUsuario", usuarioActual.getNombreUsuario());
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
        return "contacto";
    }

    @PostMapping("/contactar")
    public String enviarContacto(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String message,
            RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario emisor = detallesUsuario.getUsuario();

        Usuario administrador = S_usuario.obtenerUsuarioPorRol(Rol.ADMIN);
        if (administrador == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró un administrador.");
            return "redirect:/contacto";
        }

        Usuario admin = administrador;

        // Crear mensaje
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido("De: " + name + " (" + email + ")\n\n" + message);
        mensaje.setFechaCreacion(LocalDateTime.now());
        mensaje.setTipoMensaje(TipoMensaje.MENSAJES_CONTACTO);
        mensaje.setReceptor(admin);
        mensaje.setUsuario(emisor); 

        S_mensaje.guardarMensaje(mensaje);

        redirectAttributes.addFlashAttribute("mensaje", "Mensaje enviado con éxito.");
        return "redirect:/contacto";
    }

    @GetMapping("/mensajes-contacto")
    public String verMensajesContacto(Model modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DetallesUsuario detallesUsuario = (DetallesUsuario) auth.getPrincipal();
        Usuario usuarioActual = detallesUsuario.getUsuario();

        if (usuarioActual.getRol() != Rol.ADMIN) {
            return "redirect:/";
        }

        List<Mensaje> mensajes = S_mensaje.obtenerMensajesPorReceptorYTipo(usuarioActual, TipoMensaje.MENSAJES_CONTACTO);
        modelo.addAttribute("mensajes", mensajes);
        modelo.addAttribute("nombreUsuario", usuarioActual.getNombreUsuario());
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(usuarioActual.getNombreUsuario()));
        return "mensajes-contacto";
    }

    /************************************/
    /* MÉTODOS DE AYUDA */
    /************************************/

    // Método para obtener la primera letra
    private String obtenerPrimeraLetra(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            return String.valueOf(nombre.charAt(0)).toUpperCase();
        }
        return "";
    }
}
