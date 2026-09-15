package com.danielalonso.ejercicioskotlin

val numerosEnteros: Array<Number> = arrayOf(1,2,3,4,5,6,7,8,9,0)

val numerosEnterosMayor5 = numerosEnteros.filter { numero -> numero.toInt()>5 };
print(numerosEnterosMayor5)
val numerosEnterosMayor5x2 = numerosEnterosMayor5.map { numero -> numero.toInt()*2 };
print(numerosEnterosMayor5x2)