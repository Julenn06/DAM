package com.example.elormovpmdm.data.schedule

import com.example.elormovpmdm.domain.model.Schedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para la recuperación de calendarios y horarios académicos.
 */
interface ScheduleApiService {

    /**
     * Obtiene el listado de horarios generales asociados a un identificador.
     * @param id Identificador del recurso (profesor, grupo o aula).
     * @return [Response] con una lista de objetos [Schedule].
     */
    @GET("horarios/getHorarios/{id}")
    suspend fun getHorario(@Path("id") id: Int): Response<List<Schedule>>

    /**
     * Obtiene el listado de horarios específicos desde la perspectiva del alumno.
     * @param id Identificador único del estudiante.
     * @return [Response] con una lista de objetos [Schedule] filtrada por su matrícula.
     */
    @GET("horarios/getHorariosIkasle/{id}")
    suspend fun getStudentHorario(@Path("id") id: Int): Response<List<Schedule>>
}