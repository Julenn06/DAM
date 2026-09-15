package com.danielalonso.gestionusuarios

data class Usuario(
    val nombre: String,
    val correo: String
) {
    override fun toString(): String {
        return "Usuario: $nombre Email: $correo "
    }
}
