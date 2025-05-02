package com.asturmatch.proyectoasturmatch.controlador;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.asturmatch.proyectoasturmatch.modelo.Partido;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;

@Controller
@SessionAttributes("nombreUsuario")
public class PartidoController {
	
	@Autowired
    private ServicioUsuario S_usuario;

    @Autowired
    private ServicioTorneo S_torneo;

    @Autowired
    private ServicioPartido S_partido;

    private static final DateTimeFormatter HORA_FORMAT = DateTimeFormatter.ofPattern("dd.MM. HH:mm");
    
    @GetMapping("/partidos")
    public String partidos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
    	Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
    	List<Torneo> misTorneos = S_torneo.obtenerTorneosPorCreador(usuarioActual);

        // Mostramos los partidos por jornada
        Map<String, List<Partido>> partidosPorJornada = misTorneos.stream()
            .flatMap(t -> S_partido.obtenerPartidosPorTorneo(t).stream())
            .collect(Collectors.groupingBy(
                p -> p.getFechaHora().format(HORA_FORMAT).split(" ")[0]
            ));

        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("partidosPorJornada", partidosPorJornada);
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
        return "partidos";
    }

    private String obtenerPrimeraLetra(String nombre) {
        return (nombre!=null && !nombre.isEmpty())
            ? nombre.substring(0,1).toUpperCase()
            : "";
    }

}
