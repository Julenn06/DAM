package com.elorrieta.elorserv.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Erabiltzaile motak (tipos taula) - god, administrador, profesor, alumno
@Entity
@Table(name = "tipos")
public class Mota implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(name = "name_eu")
	private String nameEu;

	@JsonIgnore
	@OneToMany(mappedBy = "mota")
	private Set<Erabiltzailea> erabiltzaileak = new HashSet<>(0);

	public Mota() {
	}

	public Mota(String name) {
		this.name = name;
	}

	public Mota(String name, String nameEu, Set<Erabiltzailea> erabiltzaileak) {
		this.name = name;
		this.nameEu = nameEu;
		this.erabiltzaileak = erabiltzaileak;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEu() {
		return this.nameEu;
	}

	public void setNameEu(String nameEu) {
		this.nameEu = nameEu;
	}

	@JsonIgnore
	public Set<Erabiltzailea> getErabiltzaileak() {
		return this.erabiltzaileak;
	}

	public void setErabiltzaileak(Set<Erabiltzailea> erabiltzaileak) {
		this.erabiltzaileak = erabiltzaileak;
	}

	@Override
	public String toString() {
		return "Mota{" + "id=" + id + ", name='" + name + '\'' + ", nameEu='" + nameEu + '\'' + '}';
	}
}
