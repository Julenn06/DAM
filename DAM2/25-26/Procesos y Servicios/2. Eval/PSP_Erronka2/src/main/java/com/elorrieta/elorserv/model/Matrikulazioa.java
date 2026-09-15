package com.elorrieta.elorserv.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Matrikulazioak (matriculaciones taula) - Ikasleen matrikulazioak
@Entity
@Table(name = "matriculaciones")
public class Matrikulazioa implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ciclo_id")
	private Zikloa zikloa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alum_id")
	private Erabiltzailea erabiltzailea;

	private byte curso;
	private Date fecha;

	public Matrikulazioa() {
	}

	public Matrikulazioa(Zikloa zikloa, Erabiltzailea erabiltzailea, byte curso, Date fecha) {
		this.zikloa = zikloa;
		this.erabiltzailea = erabiltzailea;
		this.curso = curso;
		this.fecha = fecha;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Zikloa getZikloa() {
		return this.zikloa;
	}

	public void setZikloa(Zikloa zikloa) {
		this.zikloa = zikloa;
	}

	public Erabiltzailea getErabiltzailea() {
		return this.erabiltzailea;
	}

	public void setErabiltzailea(Erabiltzailea erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	public byte getCurso() {
		return this.curso;
	}

	public void setCurso(byte curso) {
		this.curso = curso;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Matrikulazioa{" + "id=" + id + ", curso=" + curso + ", fecha=" + fecha + '}';
	}
}
