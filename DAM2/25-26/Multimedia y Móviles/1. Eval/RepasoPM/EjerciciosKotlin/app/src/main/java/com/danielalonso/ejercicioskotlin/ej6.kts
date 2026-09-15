package com.danielalonso.ejercicioskotlin

data class Libro(var nombre: String, val autor: String, var prestado: Boolean = false)

val libros: ArrayList<Libro> = arrayListOf()

while (true) {
    println("\n--- MENÚ ---")
    println("1. Nuevo libro")
    println("2. Buscar libro")
    println("3. Pedir prestado")
    println("4. Devolver")
    println("5. Salir")

    print("Elige una opción: ")
    val opcion = readLine()?.toIntOrNull() ?: 0

    when (opcion) {
        1 -> {
            print("Nombre: ")
            val nombre = readLine() ?: "Sin nombre"
            print("Autor: ")
            val autor = readLine() ?: "Sin autor"
            libros.add(Libro(nombre, autor))
            println("Libro '${nombre}' añadido.")
        }
        2 -> {
            println("\n--- CATÁLOGO DE LIBROS ---")
            libros.forEachIndexed { index, libro ->
                val estado = if (libro.prestado) " (Prestado)" else " (Disponible)"
                println("$index. '${libro.nombre}' de ${libro.autor}$estado")
            }
        }
        3 -> {
            print("Índice del libro a pedir prestado: ")
            val indice = readLine()?.toIntOrNull()
            if (indice != null && indice in libros.indices) {
                if (!libros[indice].prestado) {
                    libros[indice].prestado = true
                    println("Has pedido prestado '${libros[indice].nombre}'.")
                } else {
                    println("El libro ya está prestado.")
                }
            } else {
                println("Índice no válido.")
            }
        }
        4 -> {
            print("Índice del libro a devolver: ")
            val indice = readLine()?.toIntOrNull()
            if (indice != null && indice in libros.indices) {
                libros[indice].prestado = false
                println("Has devuelto '${libros[indice].nombre}'.")
            } else {
                println("Índice no válido.")
            }
        }
        5 -> break
        else -> println("Opción no válida.")
    }
}
