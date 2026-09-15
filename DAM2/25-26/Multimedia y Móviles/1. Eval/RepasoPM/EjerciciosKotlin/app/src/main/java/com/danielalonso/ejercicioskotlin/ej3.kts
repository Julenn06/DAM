package com.danielalonso.ejercicioskotlin
val nullableList: List<String?> = listOf("Hola", null, "Mundo", null, "!")
val nonNullableList = nullableList.filterNotNull()
println(nonNullableList)

