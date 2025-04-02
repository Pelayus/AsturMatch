package com.asturmatch.proyectoasturmatch.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private String contenido;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private TipoMensaje tipoMensaje;

    @ManyToOne
    private Torneo torneo;

	public Mensaje() {
		super();
	}

	public Mensaje(Long id, Usuario usuario, String contenido, LocalDateTime fechaCreacion, TipoMensaje tipoMensaje,
			Torneo torneo) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contenido = contenido;
		this.fechaCreacion = fechaCreacion;
		this.tipoMensaje = tipoMensaje;
		this.torneo = torneo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public TipoMensaje getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(TipoMensaje tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public Torneo getTorneo() {
		return torneo;
	}

	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}

	@Override
	public String toString() {
		return "Mensaje [id=" + id + ", usuario=" + usuario + ", contenido=" + contenido + ", fechaCreacion="
				+ fechaCreacion + ", tipoMensaje=" + tipoMensaje + ", torneo=" + torneo + "]";
	}
}

