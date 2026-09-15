package com.danielalonso.ejercicioskotlin.repaso

/*
Escribe un programa que reciba un número entero y diga si es primo o no.
Usa un bucle for y una variable booleana auxiliar.
 */

print("Introduce un numero entero")
val numero:Int= readLine()?.toIntOrNull()?:0;
var esPrimo:Boolean=true;

if (numero <= 1) {
    esPrimo = false
} else {
    for (i in 2 until numero) {
        if (numero % i == 0) {
            esPrimo = false
            break
        }
    }
}

if (esPrimo) {
    println("$numero es primo")
} else {
    println("$numero NO es primo")
}