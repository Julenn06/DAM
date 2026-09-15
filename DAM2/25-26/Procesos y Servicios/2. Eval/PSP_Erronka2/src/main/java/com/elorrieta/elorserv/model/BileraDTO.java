package com.elorrieta.elorserv.model;

public class BileraDTO {
	private Integer idBilera;
	private String titulo;
	private String asunto;
	private String aula;
	private String fecha;
	private String estado;
	private String estadoEus;
	private String idCentro;
	private Integer ikasleaId;
	private Integer irakasleaId;

	public BileraDTO() {
	}

	public Integer getIdBilera() {
		return idBilera;
	}

	public void setIdBilera(Integer idBilera) {
		this.idBilera = idBilera;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoEus() {
		return estadoEus;
	}

	public void setEstadoEus(String estadoEus) {
		this.estadoEus = estadoEus;
	}

	public String getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}

	public Integer getIkasleaId() {
		return ikasleaId;
	}

	public void setIkasleaId(Integer ikasleaId) {
		this.ikasleaId = ikasleaId;
	}

	public Integer getIrakasleaId() {
		return irakasleaId;
	}

	public void setIrakasleaId(Integer irakasleaId) {
		this.irakasleaId = irakasleaId;
	}
}
