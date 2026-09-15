package com.danielalonso.ejercicioskotlin.repaso
/*
Define una interfaz Reproducible con los métodos play() y stop().
Implementa la interfaz en dos clases: Cancion y Video, cada una con comportamiento propio.
* */
interface Reproducible {
    fun play()
    fun stop()
}

class Cancion(val titulo: String) : Reproducible {
    override fun play() = println("Reproduciendo canción: $titulo")
    override fun stop() = println("Canción detenida")
}

class Video(val nombre: String) : Reproducible {
    override fun play() = println("Reproduciendo video: $nombre")
    override fun stop() = println("Video detenido")
}
val cancion = Cancion("Mi canción")
val video = Video("Mi video")
cancion.play()
video.play()
cancion.stop()
video.stop()
