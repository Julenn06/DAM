package com.danielalonso.ejercicioskotlin

val numerosEnteros: Array<Number> = arrayOf(1,2,3,4,5,6,7,8,9,0)
print("Introduce un número: ")
val numero = readLine()?.toIntOrNull() ?: 0

var estaenelarray: Boolean = estaenelarray(numerosEnteros, numero);
print("$estaenelarray")
fun estaenelarray(array: Array<Number>, numero: Number): Boolean{
    return numerosEnteros.contains(numero)
}