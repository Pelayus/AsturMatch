package com.asturmatch.proyectoasturmatch.controlador;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.asturmatch.proyectoasturmatch.modelo.Clasificacion;
import com.asturmatch.proyectoasturmatch.modelo.Partido;
import com.asturmatch.proyectoasturmatch.modelo.Torneo;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.servicios.ServicioClasificacion;
import com.asturmatch.proyectoasturmatch.servicios.ServicioPartido;
import com.asturmatch.proyectoasturmatch.servicios.ServicioTorneo;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.asturmatch.proyectoasturmatch.modelo.Resultado;
import com.asturmatch.proyectoasturmatch.servicios.ServicioResultado;

@Controller
@SessionAttributes("nombreUsuario")
public class PartidoController {
	
	@Autowired
    private ServicioUsuario S_usuario;

    @Autowired
    private ServicioTorneo S_torneo;

    @Autowired
    private ServicioPartido S_partido;
    
    @Autowired
    private ServicioResultado S_resultado;
    
	@Autowired
	private ServicioClasificacion S_clasificacion;

    private static final DateTimeFormatter HORA_FORMAT = DateTimeFormatter.ofPattern("dd.MM. HH:mm");
    
    @GetMapping("/partidos")
    public String partidos(@ModelAttribute("nombreUsuario") String nombreUsuario, Model modelo) {
    	Usuario usuarioActual = S_usuario.obtenerUsuarioPorNombre(nombreUsuario);
    	List<Torneo> misTorneos = S_torneo.obtenerTorneosPorCreador(usuarioActual);
    	
    	Map<Long, List<Clasificacion>> clasificacionesPorTorneo = new HashMap<>();
        for (Torneo torneo : misTorneos) {
            List<Clasificacion> clasificaciones = S_clasificacion.obtenerClasificacionPorTorneo(torneo.getId());
            clasificacionesPorTorneo.put(torneo.getId(), clasificaciones);
        }
        modelo.addAttribute("clasificacionesPorTorneo", clasificacionesPorTorneo);

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
        modelo.addAttribute("misTorneos", misTorneos);
        return "partidos";
    }

    private String obtenerPrimeraLetra(String nombre) {
        return (nombre!=null && !nombre.isEmpty())
            ? nombre.substring(0,1).toUpperCase()
            : "";
    }
    
    @PostMapping("/guardar-resultado")
    public String guardarResultado(@RequestParam Long partidoId,
                                   @RequestParam int puntuacionLocal,
                                   @RequestParam int puntuacionVisitante) {
        try {
            Optional<Partido> optionalPartido = S_partido.obtenerPartidoPorId(partidoId);

            if (optionalPartido.isEmpty()) {
                System.out.println("Partido no encontrado con ID: " + partidoId);
                return "redirect:/partidos";
            }

            Partido partido = optionalPartido.get();
            Resultado resultado = partido.getResultado();

            if (resultado == null) {
                resultado = new Resultado();
                resultado.setPartido(partido);
            }

            resultado.setPuntuacionLocal(puntuacionLocal);
            resultado.setPuntuacionVisitante(puntuacionVisitante);

            S_resultado.guardarResultado(resultado);

        } catch (Exception e) {
            System.out.println("Error al guardar el resultado: " + e.getMessage());
            return "redirect:/partidos";
        }
        
        System.out.println("Resultado actualizado correctamente");
        return "redirect:/partidos";
    }


}
