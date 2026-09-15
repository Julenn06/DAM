package com.elorrieta.elorserv.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Moduluak (modulos taula) - Ziklo bateko moduluak
@Entity
@Table(name = "modulos")
public class Modulua implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ciclo_id")
	private Zikloa zikloa;

	private String nombre;

	@Column(name = "nombre_eus")
	private String nombreEus;

	private int horas;

	private byte curso;

	@JsonIgnore
	@OneToMany(mappedBy = "modulua")
	private Set<Ordutegia> ordutegia = new HashSet<>(0);

	public Modulua() {
	}

	public Modulua(Zikloa zikloa, String nombre, int horas, byte curso) {
		this.zikloa = zikloa;
		this.nombre = nombre;
		this.horas = horas;
		this.curso = curso;
	}

	public Modulua(Zikloa zikloa, String nombre, String nombreEus, int horas, byte curso, Set<Ordutegia> ordutegia) {
		this.zikloa = zikloa;
		this.nombre = nombre;
		this.nombreEus = nombreEus;
		this.horas = horas;
		this.curso = curso;
		this.ordutegia = ordutegia;
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreEus() {
		return this.nombreEus;
	}

	public void setNombreEus(String nombreEus) {
		this.nombreEus = nombreEus;
	}

	public int getHoras() {
		return this.horas;
	}

	public void setHoras(int horas) {
		this.horas = horas;
	}

	public byte getCurso() {
		return this.curso;
	}

	public void setCurso(byte curso) {
		this.curso = curso;
	}

	@JsonIgnore
	public Set<Ordutegia> getOrdutegia() {
		return this.ordutegia;
	}

	public void setOrdutegia(Set<Ordutegia> ordutegia) {
		this.ordutegia = ordutegia;
	}

	@Override
	public String toString() {
		return "Modulua{" + "id=" + id + ", nombre='" + nombre + '\'' + ", nombreEus='" + nombreEus + '\'' + ", horas="
				+ horas + ", curso=" + curso + '}';
	}
}
