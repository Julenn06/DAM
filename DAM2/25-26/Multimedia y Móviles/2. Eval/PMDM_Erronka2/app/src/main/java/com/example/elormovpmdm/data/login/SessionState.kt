package com.example.elormovpmdm.data.login

import com.example.elormovpmdm.domain.model.User

/**
 * Jerarquía sellada para representar el estado de la sesión actual.
 * Utilizada para el control de acceso y persistencia de identidad del usuario.
 */
sealed class SessionState {
    /** El sistema está recuperando la sesión de la persistencia local. */
    object Loading: SessionState()

    /** * Usuario autenticado con éxito.
     * @param user Datos del perfil del usuario activo.
     */
    data class Authenticated(val user: User): SessionState()

    /** No existe una sesión activa o el usuario ha cerrado la sesión. */
    object NotAuthenticated: SessionState()
}