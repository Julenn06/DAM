package com.danielalonso.ejercicioskotlin.repaso

/*
Crea un objeto Contador con una variable privada total y métodos incrementar() y mostrar().
Cada llamada a incrementar aumenta el contador y muestra su valor actual.
* */

object Contador {
    private var total = 0

    fun incrementar() {
        total++
        mostrar()
    }

    fun mostrar() = println("Contador: $total")
}

Contador.incrementar()
Contador.incrementar()
