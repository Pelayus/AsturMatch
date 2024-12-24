package com.asturmatch.proyectoasturmatch.modelo;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Override
    public String toString() {
        return "Equipo [id=" + id + ", nombre=" + nombre + ", jugadores=" + jugadores + ", torneo=" + torneo + "]";
    }
}
