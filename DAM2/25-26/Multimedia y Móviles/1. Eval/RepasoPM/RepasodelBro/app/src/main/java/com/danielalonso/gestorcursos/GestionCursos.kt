package com.danielalonso.gestorcursos

object GestionCursos {
    val cursos = ArrayList<Curso>()
    // Carga de los 5 cursos inciales
    fun cargarCursosInicial(){
        val cursosIniciales = listOf(
            Curso("A1","Angular desde cero", 25, "Acabado"),
            Curso("D2","Kotlin desde cero", 12, "Pendiente"),
            Curso("C3","HTML desde cero", 5, "Pendiente"),
            Curso("J4","LINQ desde cero", 25, "Acabado"),
            Curso("AAA5","Java desde cero", 25, "Pendiente")
        )
        cursos.addAll(cursosIniciales)
    }
    // Del ArrayList, conseguir un curso en especifico por id
    fun conseguirCurso(idCurso: String): Curso{
        return cursos.first { (id) -> id == idCurso }
    }
    // Cambiar estado de un curso del ArrayList, segun el id
    // Solo puede ser o "Acabado" o "Pendiente"
    fun cambiarEstado(idCurso: String?, estado: String?) {
        if(estado == "Pendiente" || estado == "Acabado"){
            cursos.first { (id) -> id == idCurso }.estado = estado
        }
    }
}