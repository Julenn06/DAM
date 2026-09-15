package com.example.elormovpmdm.domain.model

data class Meeting (
    val idReunion: Int? = null,
    val titulo: String? = null,
    val asunto: String? = null,
    val estado: String,
    val estadoEus: String? = null,
    val aula: String,
    val fecha: String,
    val idCentro: String,
    val alumnoId: Int,
    val profesorId: Int,
    val usersByAlumnoId: User? = null,
    val usersByProfesorId: User? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)