package com.danielalonso.ejercicioskotlin.repaso

/*
Declara una variable String nullable. Si es null, muestra "Sin valor"; si no, muestra su longitud.
Usa el operador Elvis (?:) y safe call (?.).
 */
val texto: String? = null
println(texto?.length ?: "Sin valor")