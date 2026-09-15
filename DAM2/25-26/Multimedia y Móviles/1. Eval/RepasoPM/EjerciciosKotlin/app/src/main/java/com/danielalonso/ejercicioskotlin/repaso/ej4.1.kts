package com.danielalonso.ejercicioskotlin.repaso
/*
Crea una lista mutable de enteros. Pide al usuario que introduzca números hasta teclear 0. Muestra la media aritmética de los valores introducidos (sin incluir el 0).
 */
val numeros = mutableListOf<Int>()
var num: Int

do {
    print("Introduce un número (0 para terminar): ")
    num = readLine()!!.toInt()
    if (num != 0) numeros.add(num)
} while (num != 0)

if (numeros.isNotEmpty()) {
    val media = numeros.average()
    println("Media: $media")
} else {
    println("No se introdujeron números.")
}