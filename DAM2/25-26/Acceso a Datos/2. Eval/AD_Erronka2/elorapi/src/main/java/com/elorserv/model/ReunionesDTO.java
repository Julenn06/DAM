package com.elorserv.model;

import java.sql.Timestamp;

public class ReunionesDTO {

    private Integer idReunion;
    private String titulo;
    private String asunto;
    private String aula;
    // fecha as ISO string to match service and JSON input
    private String fecha;
    private String estado;
    private String estadoEus;
    private String idCentro;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer alumnoId;
    private Integer profesorId;

    public ReunionesDTO() {}

    public ReunionesDTO(Integer idReunion, String titulo, String asunto, String aula, String fecha, String estado, String estadoEus, String idCentro, Timestamp createdAt, Timestamp updatedAt, Integer alumnoId, Integer profesorId) {
        this.idReunion = idReunion;
        this.titulo = titulo;
        this.asunto = asunto;
        this.aula = aula;
        this.fecha = fecha;
        this.estado = estado;
        this.estadoEus = estadoEus;
        this.idCentro = idCentro;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.alumnoId = alumnoId;
        this.profesorId = profesorId;
    }

    // getters and setters
    public Integer getIdReunion() { return idReunion; }
    public void setIdReunion(Integer idReunion) { this.idReunion = idReunion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstadoEus() { return estadoEus; }
    public void setEstadoEus(String estadoEus) { this.estadoEus = estadoEus; }

    public String getIdCentro() { return idCentro; }
    public void setIdCentro(String idCentro) { this.idCentro = idCentro; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Integer getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Integer alumnoId) { this.alumnoId = alumnoId; }

    public Integer getProfesorId() { return profesorId; }
    public void setProfesorId(Integer profesorId) { this.profesorId = profesorId; }
}