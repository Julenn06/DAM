package com.elorrieta.elorserv.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

// Ordutegia (horarios taula) - Irakasleen asteko ordutegia
@Entity
@Table(name = "horarios")
public class Ordutegia implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modulo_id")
	private Modulua modulua;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profe_id")
	@JsonBackReference(value = "erabiltzailea-ordutegia")
	private Erabiltzailea erabiltzailea;

	private String dia;
	private byte hora;
	private String aula;
	private String observaciones;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	public Ordutegia() {
	}

	public Ordutegia(Modulua modulua, Erabiltzailea erabiltzailea, String dia, byte hora) {
		this.modulua = modulua;
		this.erabiltzailea = erabiltzailea;
		this.dia = dia;
		this.hora = hora;
	}

	public Ordutegia(Modulua modulua, Erabiltzailea erabiltzailea, String dia, byte hora, String aula,
			String observaciones, Timestamp createdAt, Timestamp updatedAt) {
		this.modulua = modulua;
		this.erabiltzailea = erabiltzailea;
		this.dia = dia;
		this.hora = hora;
		this.aula = aula;
		this.observaciones = observaciones;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Modulua getModulua() {
		return this.modulua;
	}

	public void setModulua(Modulua modulua) {
		this.modulua = modulua;
	}

	public Erabiltzailea getErabiltzailea() {
		return this.erabiltzailea;
	}

	public void setErabiltzailea(Erabiltzailea erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	public String getDia() {
		return this.dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public byte getHora() {
		return this.hora;
	}

	public void setHora(byte hora) {
		this.hora = hora;
	}

	public String getAula() {
		return this.aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Ordutegia{" + "id=" + id + ", dia='" + dia + '\'' + ", hora=" + hora + ", aula='" + aula + '\'' + '}';
	}
}
