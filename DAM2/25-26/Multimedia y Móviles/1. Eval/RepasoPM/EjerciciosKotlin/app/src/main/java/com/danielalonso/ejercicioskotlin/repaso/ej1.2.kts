package com.danielalonso.ejercicioskotlin.repaso

/*
Declara tres variables: una constante de tipo Double, otra mutable Int y una String.
Muestra por pantalla el tipo y el valor de cada una usando string templates.
*/

val const: Double=0.5;
var mutable:Int=1;
var mutableStr: String="Hola";

print("$const ${const::class.simpleName} ")
print("$mutable ${mutable::class.simpleName} ")
print("$mutableStr ${mutableStr::class.simpleName} ")

