package com.example.elormovpmdm.data.login

import com.example.elormovpmdm.domain.model.LoginRequest
import com.example.elormovpmdm.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de Retrofit que define los endpoints relacionados con la autenticación.
 */
interface LoginApiService {
    /**
     * Realiza una petición POST al endpoint "login".
     * * @param request Objeto [LoginRequest] que contiene las credenciales, serializado como JSON en el cuerpo.
     * @return Un objeto [Response] que envuelve al usuario [User] si la operación tiene éxito.
     */
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<User>
}