package com.danielalonso.ejercicioskotlin.repaso

/*
Crea una data class Producto con nombre, precio y stock.
Genera tres productos y muestra el total del inventario.
 */

data class Producto(val nombre: String, val precio: Double, val stock: Int)

val productos = listOf(
    Producto("Teclado", 25.0, 10),
    Producto("Ratón", 15.0, 20),
    Producto("Monitor", 150.0, 5)
)
val total = productos.sumOf { it.precio * it.stock }
println("Total inventario: $total €")
