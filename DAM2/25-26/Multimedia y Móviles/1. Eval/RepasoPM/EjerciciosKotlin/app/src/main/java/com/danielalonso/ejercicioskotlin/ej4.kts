package com.danielalonso.ejercicioskotlin

data class Persona(var nombre: String, val apellido: String, val edad: Int)

val personas = ArrayList<Persona>()

while (true) {
    println("\n--- MENÚ ---")
    println("1. Crear persona")
    println("2. Mostrar personas")
    println("3. Cambiar nombre de persona")
    println("4. Mostrar personas mayores de edad")
    println("5. Salir")

    print("Elige una opción: ")
    val opcion = readLine()?.toIntOrNull() ?: 0

    when (opcion) {
        1 -> {
            print("Nombre: ")
            val nombre = readLine() ?: ""
            print("Apellido: ")
            val apellido = readLine() ?: ""
            print("Edad: ")
            val edad = readLine()?.toIntOrNull() ?: 0
            personas.add(Persona(nombre, apellido, edad))
        }
        2 -> {
            println("\n--- PERSONAS ---")
            personas.forEach { println(it) }
        }
        3 -> {
            print("Índice de la persona a modificar: ")
            val indice = readLine()?.toIntOrNull()
            if (indice != null && indice in personas.indices) {
                print("Nuevo nombre: ")
                val nuevoNombre = readLine() ?: ""
                personas[indice].nombre = nuevoNombre
            } else {
                println("Índice no válido.")
            }
        }
        4 -> {
            println("\n--- PERSONAS MAYORES DE EDAD ---")
            val mayores = personas.filter { it.edad >= 18 }
            mayores.forEach { println(it) }
        }
        5 -> break
        else -> println("Opción no válida.")
    }
}
