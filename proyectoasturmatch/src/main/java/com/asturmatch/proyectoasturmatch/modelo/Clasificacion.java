package com.asturmatch.proyectoasturmatch.modelo;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Clasificacion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int pj;
	private int pg;
	private int pe;
	private int pp;
	private int puntos;
	private int dif;
	private int gf;
	private int gc;
	private int pf;
	private int pc;
	
	@ManyToOne
    private Equipo equipo;

    @ManyToOne
    private Torneo torneo;
	
	public Clasificacion() {
		super();
	}
	
	//CLASIFICACIÓN FÚTBOL

	public Clasificacion(int pj, int pg, int pe, int pp, int puntos, int gf, int gc, Equipo equipo, Torneo torneo) {
		super();
		this.pj = pj;
		this.pg = pg;
		this.pe = pe;
		this.pp = pp;
		this.puntos = puntos;
		this.gf = gf;
		this.gc = gc;
	}
	
	//CLASIFICACIÓN BALONCESTO

	public Clasificacion(Long id, int pj, int pg, int pp, int dif, int pf, int pc, Equipo equipo, Torneo torneo) {
		super();
		this.id = id;
		this.pj = pj;
		this.pg = pg;
		this.pp = pp;
		this.dif = dif;
		this.pf = pf;
		this.pc = pc;
		this.equipo = equipo;
		this.torneo = torneo;
	}

	public int getPj() {
		return pj;
	}

	public void setPj(int pj) {
		this.pj = pj;
	}

	public int getPg() {
		return pg;
	}

	public void setPg(int pg) {
		this.pg = pg;
	}

	public int getPe() {
		return pe;
	}

	public void setPe(int pe) {
		this.pe = pe;
	}

	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getDif() {
		return dif;
	}

	public void setDif(int dif) {
		this.dif = dif;
	}

	public int getGf() {
		return gf;
	}

	public void setGf(int gf) {
		this.gf = gf;
	}

	public int getGc() {
		return gc;
	}

	public void setGc(int gc) {
		this.gc = gc;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Torneo getTorneo() {
		return torneo;
	}

	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}

	public int getPf() {
		return pf;
	}

	public void setPf(int pf) {
		this.pf = pf;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}
}
