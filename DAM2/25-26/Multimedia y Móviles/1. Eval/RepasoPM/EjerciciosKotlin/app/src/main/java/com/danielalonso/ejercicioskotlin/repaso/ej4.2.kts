package com.danielalonso.ejercicioskotlin.repaso
/*
Crea un conjunto (Set) de nombres sin duplicados y permite añadir y eliminar elementos desde consola hasta que el usuario escriba "fin". Muestra el resultado final
 */

val nombres = mutableSetOf<String>()
var input: String

do {
    print("Introduce un nombre (fin para terminar): ")
    input = readLine()!!.lowercase()

    if (input != "fin") {
        if (!nombres.add(input)) println("Ese nombre ya existe.")
    }
} while (input != "fin")

println("Nombres finales: $nombres")
