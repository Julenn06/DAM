package com.example.a1ebalazterketaapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    // Usuario actual
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    
    // Verificar si hay sesión activa
    val isLoggedIn: Boolean
        get() = currentUser != null
    
    /**
     * Realiza login con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result con el FirebaseUser si es exitoso o Exception si falla
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Usuario no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        auth.signOut()
    }
    
    /**
     * Registra un nuevo usuario con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result con el FirebaseUser si es exitoso o Exception si falla
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Error al crear usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Envía un email de recuperación de contraseña
     * @param email Email del usuario
     * @return Result indicando éxito o fallo
     */
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
