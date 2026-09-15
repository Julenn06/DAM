package com.elorrieta.elorserv.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Zikloak (ciclos taula) - DAM, DAW, ASIR, SMR, etab.
@Entity
@Table(name = "ciclos")
public class Zikloa implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombre;

	@JsonIgnore
	@OneToMany(mappedBy = "zikloa")
	private Set<Modulua> moduluak = new HashSet<>(0);

	@JsonIgnore
	@OneToMany(mappedBy = "zikloa")
	private Set<Matrikulazioa> matrikulazioak = new HashSet<>(0);

	public Zikloa() {
	}

	public Zikloa(String nombre) {
		this.nombre = nombre;
	}

	public Zikloa(String nombre, Set<Modulua> moduluak, Set<Matrikulazioa> matrikulazioak) {
		this.nombre = nombre;
		this.moduluak = moduluak;
		this.matrikulazioak = matrikulazioak;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@JsonIgnore
	public Set<Modulua> getModuluak() {
		return this.moduluak;
	}

	public void setModuluak(Set<Modulua> moduluak) {
		this.moduluak = moduluak;
	}

	@JsonIgnore
	public Set<Matrikulazioa> getMatrikulazioak() {
		return this.matrikulazioak;
	}

	public void setMatrikulazioak(Set<Matrikulazioa> matrikulazioak) {
		this.matrikulazioak = matrikulazioak;
	}

	@Override
	public String toString() {
		return "Zikloa{" + "id=" + id + ", nombre='" + nombre + '\'' + '}';
	}
}
