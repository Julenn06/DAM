package com.danielalonso.ejercicioskotlin.repaso

/*
Crea una función recursiva que calcule el factorial de un número.
Controla el caso base y las entradas negativas.
 */
print("Introduce un número: ")
val n = readLine()!!.toInt()

if (n < 0) {
    println("No se puede calcular el factorial de un número negativo.")
} else {
    var factorial: Long = 1

    for (i in 1..n) {
        factorial *= i
    }

    println("El factorial de $n es $factorial")
}