package com.asturmatch.proyectoasturmatch.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Partido partido;

    @Column(name = "puntuacion_local", nullable = false)
    private int puntuacionLocal;

    @Column(name = "puntuacion_visitante", nullable = false)
    private int puntuacionVisitante;

    public Resultado() {
        super();
    }

    public Resultado(Long id, Partido partido, int puntuacionLocal, int puntuacionVisitante) {
        super();
        this.id = id;
        this.partido = partido;
        this.puntuacionLocal = puntuacionLocal;
        this.puntuacionVisitante = puntuacionVisitante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public int getPuntuacionLocal() {
        return puntuacionLocal;
    }

    public void setPuntuacionLocal(int puntuacionLocal) {
        this.puntuacionLocal = puntuacionLocal;
    }

    public int getPuntuacionVisitante() {
        return puntuacionVisitante;
    }

    public void setPuntuacionVisitante(int puntuacionVisitante) {
        this.puntuacionVisitante = puntuacionVisitante;
    }

    /*@Override
    public String toString() {
        return "Resultado [id=" + id + ", partido=" + partido + ", puntuacionLocal=" + puntuacionLocal
                + ", puntuacionVisitante=" + puntuacionVisitante + "]";
    }*/
}
