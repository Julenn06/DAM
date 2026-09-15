package com.danielalonso.ejercicioskotlin.repaso

/*
Crea una clase Vehiculo con propiedad velocidad y un método acelerar().
Hereda una clase Coche que redefine el método para incrementar la velocidad en +20.
 */
open class Vehiculo(var velocidad: Int = 0) {
    open fun acelerar() {
        velocidad += 10
    }
}
class Coche : Vehiculo() {
    override fun acelerar() {
        velocidad += 20
    }
}
val coche = Coche()
coche.acelerar()
println("Velocidad del coche: ${coche.velocidad}")