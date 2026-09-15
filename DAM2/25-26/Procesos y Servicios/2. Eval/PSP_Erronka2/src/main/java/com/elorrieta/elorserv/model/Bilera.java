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

// Bilerak (reuniones taula) - Irakasle-ikasle bilerak kudeatzeko
@Entity
@Table(name = "reuniones")
public class Bilera implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reunion")
	private Integer idBilera;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alumno_id")
	private Erabiltzailea ikaslea;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profesor_id")
	private Erabiltzailea irakaslea;

	@Column
	private String estado;

	@Column(name = "estado_eus")
	private String estadoEus;

	@Column(name = "id_centro")
	private String idCentro;

	@Column
	private String titulo;

	@Column
	private String asunto;

	@Column
	private String aula;

	@Column
	private Timestamp fecha;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	public Bilera() {
	}

	public Bilera(Erabiltzailea ikaslea, Erabiltzailea irakaslea, String estado, String estadoEus, String idCentro,
			String titulo, String asunto, String aula, Timestamp fecha, Timestamp createdAt, Timestamp updatedAt) {
		this.ikaslea = ikaslea;
		this.irakaslea = irakaslea;
		this.estado = estado;
		this.estadoEus = estadoEus;
		this.idCentro = idCentro;
		this.titulo = titulo;
		this.asunto = asunto;
		this.aula = aula;
		this.fecha = fecha;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getIdBilera() {
		return this.idBilera;
	}

	public void setIdBilera(Integer idBilera) {
		this.idBilera = idBilera;
	}

	public Erabiltzailea getIkaslea() {
		return this.ikaslea;
	}

	public void setIkaslea(Erabiltzailea ikaslea) {
		this.ikaslea = ikaslea;
	}

	public Erabiltzailea getIrakaslea() {
		return this.irakaslea;
	}

	public void setIrakaslea(Erabiltzailea irakaslea) {
		this.irakaslea = irakaslea;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoEus() {
		return this.estadoEus;
	}

	public void setEstadoEus(String estadoEus) {
		this.estadoEus = estadoEus;
	}

	public String getIdCentro() {
		return this.idCentro;
	}

	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return this.aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
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
		return "Bilera{" + "idBilera=" + idBilera + ", estado='" + estado + '\'' + ", estadoEus='" + estadoEus + '\''
				+ ", titulo='" + titulo + '\'' + ", fecha=" + fecha + '}';
	}
}
