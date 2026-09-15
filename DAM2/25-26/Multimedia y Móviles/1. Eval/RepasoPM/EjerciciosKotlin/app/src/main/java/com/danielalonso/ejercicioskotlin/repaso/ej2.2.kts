package com.danielalonso.ejercicioskotlin.repaso

/*
Simula un menú con opciones 1–3 usando when:
1: "Alta de usuario", 2: "Baja de usuario", 3: "Salir".
Muestra el mensaje correspondiente hasta que se elija 3.
 */
var activo: Boolean = true;
while (activo){
    println("1.Alta de usuario")
    println("2.Baja de usuario")
    println("3.Salir")
    println("ELIGE UNA OPCION:")
    val opcion = readLine()?.toIntOrNull()?:0;
    when(opcion){
     1 -> {
         println("Bienvenido a alta")
     }
     2 -> {
         println("Bienvenido a baja")
     }
     3 ->{
         activo=false;
         println("Salido")
     }

    }
}