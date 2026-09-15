package com.danielalonso.ejercicioskotlin

open class Trabajador(val nombre: String, val apellidos: String, val sueldoBase: Double) {

    open fun calcularBonus(): Double {
        return sueldoBase * 0.10
    }

    open fun mostrar() {
        println("--- Trabajador ---")
        println("Nombre: $nombre $apellidos")
        println("Sueldo Base: $sueldoBase")
        println("Bonus: ${calcularBonus()}")
    }
}

class Programador(
    nombre: String,
    apellidos: String,
    sueldoBase: Double,
    val lenguajes: List<String>
) : Trabajador(nombre, apellidos, sueldoBase) {

    override fun calcularBonus(): Double {
        return if (lenguajes.size > 2) {
            sueldoBase * 0.15
        } else {
            super.calcularBonus()
        }
    }

    override fun mostrar() {
        println("--- Programador ---")
        println("Nombre: $nombre $apellidos")
        println("Sueldo Base: $sueldoBase")
        println("Lenguajes: ${lenguajes.joinToString(", ")}")
        println("Bonus: ${calcularBonus()}")
    }
}


val trabajadorGenerico = Trabajador("Pepe", "García", 20000.0)
val programadorJunior = Programador("Ana", "López", 25000.0, listOf("Kotlin", "Java"))
val programadorSenior = Programador("Luis", "Martínez", 30000.0, listOf("Kotlin", "Java", "Python"))

trabajadorGenerico.mostrar()
println()
programadorJunior.mostrar()
println()
programadorSenior.mostrar()

