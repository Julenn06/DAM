package com.danielalonso.ejercicioskotlin.repaso
/*
Implementa una clase Rectangulo con base y altura.
Añade métodos para calcular el área y el perímetro y prueba su uso.
 */

class Rectangulo(val base: Double, val altura: Double) {
    fun area() = base * altura
    fun perimetro() = 2 * (base + altura)
}
val r = Rectangulo(5.0, 3.0)
println("Área: ${r.area()}")
println("Perímetro: ${r.perimetro()}")
