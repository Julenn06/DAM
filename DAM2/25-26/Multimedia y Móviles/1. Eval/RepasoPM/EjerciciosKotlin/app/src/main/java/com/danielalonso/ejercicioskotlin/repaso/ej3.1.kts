package com.danielalonso.ejercicioskotlin.repaso

/*
Define una función que reciba dos números enteros y devuelva su máximo.
No uses la función max integrada.
 */
println("Introduce N1:")
val n1 = readLine()?.toIntOrNull() ?: 0
println("Introduce N2:")
val n2 = readLine()?.toIntOrNull() ?: 0

if (n1>n2){
    println("N1 es mayor a N2")
} else if (n1==n2){
    println("N1 es igual a N2")
} else {
    println("N2 es mayor a N1")
}
