package com.danielalonso.ejercicioskotlin.repaso

/*
Crea un programa que pida por consola el nombre y la edad del usuario y muestre:
"Te llamas <nombre> y tienes <edad> años".
Valida que la edad sea mayor que 0.
*/

print("Introduce un nombre:")
val nombre = readLine() ?: 0

print("Introduce la edad:")
val edad = readLine()?.toIntOrNull() ?: 0

if (edad<=0){
     print("Edad incorrecta.")
} else {
    print("Te llamas $nombre y tienes $edad anos")
}
