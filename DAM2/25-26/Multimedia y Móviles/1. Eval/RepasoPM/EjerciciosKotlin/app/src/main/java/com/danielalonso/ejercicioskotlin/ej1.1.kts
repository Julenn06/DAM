package com.danielalonso.ejercicioskotlin

val numerosEnteros = arrayOf(1,2,3,4,5,6,7,8,9,0)
print("Introduce un número: ")
val numero = readLine()?.toIntOrNull() ?: 0

var estaenelarray: Boolean = numerosEnteros.contains(numero)
print("$estaenelarray")

