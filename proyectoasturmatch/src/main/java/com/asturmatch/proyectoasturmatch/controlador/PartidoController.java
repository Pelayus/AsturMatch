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
import com.asturmatch.proyectoasturmatch.modelo.Equipo;
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
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.TipoDeporte;
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
    	List<Torneo> misTorneos = null;
    	
		if (usuarioActual.getRol().equals(Rol.ORGANIZADOR)) {
			misTorneos = S_torneo.obtenerTorneosPorCreador(usuarioActual);
		} else {
			misTorneos = S_torneo.obtenerTorneosPorJugador(usuarioActual);
		}
    	
    	Map<Long, List<Clasificacion>> clasificacionesPorTorneo = new HashMap<>();
        for (Torneo torneo : misTorneos) {
            List<Clasificacion> clasificaciones = S_clasificacion.obtenerClasificacionPorTorneo(torneo.getId());
            clasificacionesPorTorneo.put(torneo.getId(), clasificaciones);
        }
        
        // Mostramos los partidos por jornada
        Map<String, List<Partido>> partidosPorJornada = misTorneos.stream()
            .flatMap(t -> S_partido.obtenerPartidosPorTorneo(t).stream())
            .collect(Collectors.groupingBy(
                p -> p.getFechaHora().format(HORA_FORMAT).split(" ")[0]
            ));
        
        modelo.addAttribute("clasificacionesPorTorneo", clasificacionesPorTorneo);
        modelo.addAttribute("UsuarioActual", nombreUsuario);
        modelo.addAttribute("InicialUsuario", obtenerPrimeraLetra(nombreUsuario));
        modelo.addAttribute("partidosPorJornada", partidosPorJornada);
        modelo.addAttribute("rol", usuarioActual.getRol().toString());
        modelo.addAttribute("misTorneos", misTorneos);
        return "partidos";
    }
    
    /*****************************************************/
	/*         MÉTODOS PARA USUARIO ORGANIZADOR          */
	/*****************************************************/
    
    @PostMapping("/guardar-resultado")
    public String guardarResultado(@RequestParam Long partidoId,
                                   @RequestParam int puntuacionLocal,
                                   @RequestParam int puntuacionVisitante) {
    	String local;
    	String visitante;
        try {
            Optional<Partido> optionalPartido = S_partido.obtenerPartidoPorId(partidoId);

            if (optionalPartido.isEmpty()) {
                System.out.println("Partido no encontrado con ID: " + partidoId);
                return "redirect:/partidos";
            }

            //Obtenemos el nombre de los equipos que dispuntan el partido
            Partido partido = optionalPartido.get();
            local = partido.getEquipoLocal().getNombre();
            visitante = partido.getEquipoVisitante().getNombre();
            
            //Obtenemos el resultado que pasa el administrador del torneo(único con permiso de pasar el resultado)
            Resultado resultado = partido.getResultado();

            if (resultado == null) {
                resultado = new Resultado();
                resultado.setPartido(partido);
            }

            //Obtenemos la como va el marcador del partido y lo guardamos en BD 
            resultado.setPuntuacionLocal(puntuacionLocal);
            resultado.setPuntuacionVisitante(puntuacionVisitante);

            S_resultado.guardarResultado(resultado);
         
            actualizarClasificacionFutbol(partidoId, puntuacionLocal, puntuacionVisitante);

        } catch (Exception e) {
            System.out.println("Error al guardar el resultado: " + e.getMessage());
            return "redirect:/partidos";
        }
        
        System.out.println("Partido: " + local + " contra " + visitante);
        System.out.println("Resultado actualizado correctamente");
        return "redirect:/partidos";
    }
    
    @PostMapping("/actualizar-clasificacion")
    public String actualizarClasificacionFutbol(@RequestParam Long partidoId,
                                          @RequestParam int puntuacionLocal,
                                          @RequestParam int puntuacionVisitante) {
        try {
            Optional<Partido> optionalPartido = S_partido.obtenerPartidoPorId(partidoId);
            if (optionalPartido.isEmpty()) {
                System.out.println("Partido no encontrado con ID: " + partidoId);
                return "redirect:/partidos";
            }

            Partido partido = optionalPartido.get();
            Equipo equipoLocal = partido.getEquipoLocal();
            Equipo equipoVisitante = partido.getEquipoVisitante();
            Torneo torneo = partido.getTorneo();

            Clasificacion clasifLocal = S_clasificacion.obtenerClasificacionPorEquipoYTorneo(equipoLocal, torneo);
            Clasificacion clasifVisitante = S_clasificacion.obtenerClasificacionPorEquipoYTorneo(equipoVisitante, torneo);
            
            if(torneo.getDeporte().equals(TipoDeporte.FUTBOL)) {
            	clasifLocal.setPj(clasifLocal.getPj() + 1);
                clasifVisitante.setPj(clasifVisitante.getPj() + 1);

                clasifLocal.setGf(clasifLocal.getGf() + puntuacionLocal);
                clasifLocal.setGc(clasifLocal.getGc() + puntuacionVisitante);

                clasifVisitante.setGf(clasifVisitante.getGf() + puntuacionVisitante);
                clasifVisitante.setGc(clasifVisitante.getGc() + puntuacionLocal);

                if (puntuacionLocal > puntuacionVisitante) {
                    clasifLocal.setPg(clasifLocal.getPg() + 1);
                    clasifVisitante.setPp(clasifVisitante.getPp() + 1);
                    clasifLocal.setPuntos(clasifLocal.getPuntos() + 3);
                } else if (puntuacionLocal < puntuacionVisitante) {
                    clasifVisitante.setPg(clasifVisitante.getPg() + 1);
                    clasifLocal.setPp(clasifLocal.getPp() + 1);
                    clasifVisitante.setPuntos(clasifVisitante.getPuntos() + 3);
                } else {
                    clasifLocal.setPe(clasifLocal.getPe() + 1);
                    clasifVisitante.setPe(clasifVisitante.getPe() + 1);
                    clasifLocal.setPuntos(clasifLocal.getPuntos() + 1);
                    clasifVisitante.setPuntos(clasifVisitante.getPuntos() + 1);
                }
            }else {
            	clasifLocal.setPj(clasifLocal.getPj() + 1);
                clasifVisitante.setPj(clasifVisitante.getPj() + 1);
                
                clasifLocal.setPf(clasifLocal.getPf() + puntuacionLocal);
                clasifLocal.setPc(clasifLocal.getPc() + puntuacionVisitante);
                clasifLocal.setDif(clasifLocal.getPf() - clasifLocal.getPc());
                
                
                clasifVisitante.setPf(clasifVisitante.getPf() + puntuacionVisitante);
                clasifVisitante.setPc(clasifVisitante.getPc() + puntuacionLocal);
                clasifVisitante.setDif(clasifVisitante.getPf() - clasifVisitante.getPc());
                
                if (puntuacionLocal > puntuacionVisitante) {
                    clasifLocal.setPg(clasifLocal.getPg() + 1);
                    clasifVisitante.setPp(clasifVisitante.getPp() + 1);
                } else if (puntuacionLocal < puntuacionVisitante) {
                    clasifVisitante.setPg(clasifVisitante.getPg() + 1);
                    clasifLocal.setPp(clasifLocal.getPp() + 1);
                }
            }
            
            S_clasificacion.actualizarClasificacion(clasifLocal);
            S_clasificacion.actualizarClasificacion(clasifVisitante);

        } catch (Exception e) {
            System.out.println("Error al actualizar la clasificación: " + e.getMessage());
        }
        return "redirect:/partidos";
    }
    
    /************************************/
	/*       MÉTODOS DE AYUDA           */
	/************************************/

	// Método para obtener la primera letra
	private String obtenerPrimeraLetra(String nombre) {
		if (nombre != null && !nombre.isEmpty()) {
			return String.valueOf(nombre.charAt(0)).toUpperCase();
		}
		return "";
	}
}
