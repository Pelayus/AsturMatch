package com.asturmatch.proyectoasturmatch.modelo;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Equipo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;
	
	@Column(name = "descripcion", length = 500)
	private String descripcion;
	
	@Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
	
	@Column(name = "localidad", length = 100)
	private String localidad;
	
	@Column(name = "color_principal", length = 10)
	private String colorPrincipal;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_equipo", nullable = false, length = 20)
	private TipoEquipo tipoEquipo;

	@Enumerated(EnumType.STRING)
	@Column(name = "deporte", nullable = false, length = 20)
	private TipoDeporte deporte;

	@ManyToMany
	private List<Usuario> jugadores;

	@ManyToOne
	private Torneo torneo;

	public Equipo() {
		super();
	}

	public Equipo(Long id, String nombre, List<Usuario> jugadores, Torneo torneo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.jugadores = jugadores;
		this.torneo = torneo;
	}

	public Equipo(Long id, String nombre, String descripcion, LocalDate fechaCreacion, String localidad,
			String colorPrincipal, TipoEquipo tipoEquipo, TipoDeporte deporte, List<Usuario> jugadores, Torneo torneo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.localidad = localidad;
		this.colorPrincipal = colorPrincipal;
		this.tipoEquipo = tipoEquipo;
		this.deporte = deporte;
		this.jugadores = jugadores;
		this.torneo = torneo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getColorPrincipal() {
		return colorPrincipal;
	}

	public void setColorPrincipal(String colorPrincipal) {
		this.colorPrincipal = colorPrincipal;
	}

	public TipoEquipo getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(TipoEquipo tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
	}

	public TipoDeporte getDeporte() {
		return deporte;
	}

	public void setDeporte(TipoDeporte deporte) {
		this.deporte = deporte;
	}

	public List<Usuario> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<Usuario> jugadores) {
		this.jugadores = jugadores;
	}

	public Torneo getTorneo() {
		return torneo;
	}

	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}

	/*@Override
	public String toString() {
		return "Equipo [id=" + id + ", nombre=" + nombre + ", tipoEquipo=" + tipoEquipo + ", deporte=" + deporte
				+ ", jugadores=" + jugadores + ", torneo=" + torneo + "]";
	}*/
}
