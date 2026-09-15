package com.example.elormovpmdm.data.login

/**
 * Representa los estados finitos de la interfaz durante el flujo de inicio de sesión.
 * Facilita la implementación de una arquitectura basada en estados (MVI/MVVM).
 */
sealed class LoginState {
    /** Indica que la petición de login está en proceso. */
    object Loading: LoginState()

    /** Indica que la autenticación se ha completado con éxito. */
    object Success: LoginState()

    /** * Indica un fallo en el proceso.
     * @param message Descripción técnica o de usuario sobre el error ocurrido.
     */
    data class Error(val message: String): LoginState()

    /** Estado inicial o de reposo, previo a cualquier acción del usuario. */
    object Idle: LoginState()
}