package com.example.elormovpmdm.domain.model

data class User(
    val id: Int,
    val email: String,
    val username: String,
    val nombre: String,
    val apellidos: String,
    val tipoId: Int,
    val direccion: String,
    val telefono1: String
)
