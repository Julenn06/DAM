package com.danielalonso.ejercicioskotlin.repaso
/*
Define una clase Persona con nombre y edad.
Crea una lista de tres personas y muestra quién es el mayor.
* */
class Persona(val nombre: String, val edad: Int)

val personas = listOf(
    Persona("Ana", 20),
    Persona("Luis", 31),
    Persona("Maria", 27)
)

val mayor = personas.maxBy { it.edad }
println("La persona mayor es: ${mayor.nombre} con ${mayor.edad} años")