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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "deporte", nullable = false, length = 20)
    private TipoDeporte deporte;
    
    @Enumerated(EnumType.STRING)
	@Column(name = "tipo_torneo", nullable = false, length = 20)
	private TipoTorneo tipoTorneo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "ubicacion", length = 200, nullable = false)
    private String ubicacion;

    @OneToMany(mappedBy = "torneo")
    private List<Equipo> equipos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoTorneo estado;
    
    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    public Torneo() {
        super();
    }

    public Torneo(Long id, String nombre, TipoDeporte deporte, TipoTorneo tipoTorneo, LocalDate fechaInicio,
			LocalDate fechaFin, String ubicacion, List<Equipo> equipos, EstadoTorneo estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.deporte = deporte;
		this.tipoTorneo = tipoTorneo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.ubicacion = ubicacion;
		this.equipos = equipos;
		this.estado = estado;
	}

	public Torneo(Long id, String nombre, TipoDeporte deporte, TipoTorneo tipoTorneo, LocalDate fechaInicio,
			LocalDate fechaFin, String ubicacion, List<Equipo> equipos, EstadoTorneo estado, Usuario creador) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.deporte = deporte;
		this.tipoTorneo = tipoTorneo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.ubicacion = ubicacion;
		this.equipos = equipos;
		this.estado = estado;
		this.creador = creador;
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

    public TipoDeporte getDeporte() {
        return deporte;
    }

    public void setDeporte(TipoDeporte deporte) {
        this.deporte = deporte;
    }

    public TipoTorneo getTipoTorneo() {
		return tipoTorneo;
	}

	public void setTipoTorneo(TipoTorneo tipoTorneo) {
		this.tipoTorneo = tipoTorneo;
	}

	public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
    }
    
    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    @Override
    public String toString() {
        return "Torneo [id=" + id + ", nombre=" + nombre + ", deporte=" + deporte + ", fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin + ", ubicacion=" + ubicacion + ", equipos=" + equipos + ", estado=" + estado
                + "]";
    }
}
