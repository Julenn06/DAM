package com.example.elormovpmdm.data.students

import com.example.elormovpmdm.domain.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para gestionar consultas de relaciones entre usuarios.
 * Permite navegar la estructura jerárquica de la institución (Profesor <-> Alumnos).
 */
interface UsersApiService {

    /**
     * Recupera la lista de alumnos tutorizados o asignados a un profesor específico.
     * @param id Identificador único del profesor.
     * @return [Response] que contiene la lista de [User] con rol de alumno.
     */
    @GET("users/profesor/{profId}/alumnos")
    suspend fun getStudentsFromTeacher(@Path ("profId") id: Int): Response<List<User>>

    /**
     * Recupera la lista de profesores asignados a un alumno específico.
     * @param id Identificador único del alumno.
     * @return [Response] que contiene la lista de [User] con rol de profesor.
     */
    @GET("users/alumno/{alumId}/profesores")
    suspend fun getTeachersFromStudents(@Path ("alumId") id: Int): Response<List<User>>
}