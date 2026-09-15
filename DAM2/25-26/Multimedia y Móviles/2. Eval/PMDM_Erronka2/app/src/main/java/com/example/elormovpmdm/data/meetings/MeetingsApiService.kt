package com.example.elormovpmdm.data.meetings

import com.example.elormovpmdm.domain.model.CenterListResponse
import com.example.elormovpmdm.domain.model.Meeting
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para la gestión de reuniones y datos geográficos/administrativos.
 */
interface MeetingsApiService {

    /**
     * Recupera el listado de reuniones asociadas a un identificador específico.
     * @param id Identificador único (normalmente del usuario o tutor).
     * @return [Response] que contiene una lista de objetos [Meeting].
     */
    @GET("reuniones/getReuniones/{id}")
    suspend fun getReuniones(@Path ("id") id: Int): Response<MutableList<Meeting>>

    /**
     * Registra una nueva reunión en el servidor.
     * @param request Objeto [Meeting] con los detalles de la reunión.
     * @return El objeto [Meeting] persistido devuelto por el servidor.
     * Nota: A diferencia de otros métodos, este no usa el envoltorio [Response].
     */
    @POST("reuniones/create")
    suspend fun addMeeting(@Body request: Meeting): Meeting
    
    @PUT("reuniones/updateEstado/{id}")
    suspend fun updateMeetingStatus(@Path ("id") id: Int?, @Body body: Map<String, String>): Meeting
    
    /**
     * Obtiene el listado completo de centros disponibles.
     * @return [Response] con la estructura [CenterListResponse].
     */
    @GET("getCenterList")
    suspend fun getCenterList(): Response<CenterListResponse>
}