package com.danielalonso.ejercicioskotlin.repaso

/*
Crea una función que reciba una lista de Strings nullable y devuelva
una lista con solo los elementos no nulos, en mayúsculas.
* */
val lista = listOf("hola", null, "kotlin");
lista.filterNotNull().map { it.uppercase() }
