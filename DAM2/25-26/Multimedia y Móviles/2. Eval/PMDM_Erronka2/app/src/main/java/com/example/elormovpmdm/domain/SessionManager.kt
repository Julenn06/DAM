package com.example.elormovpmdm.domain

import com.example.elormovpmdm.SettingsDataStore
import com.example.elormovpmdm.data.login.SessionState
import com.example.elormovpmdm.data.modules.ApplicationScope
import com.example.elormovpmdm.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de sesión centralizado.
 * Controla el acceso del usuario y expone su estado de forma reactiva a toda la App.
 *
 * @property dataStore Fuente de persistencia para el usuario.
 * @property externalScope Ámbito de corrutina global para observar cambios sin interrupciones.
 */
@Singleton
class SessionManager @Inject constructor(
    private val dataStore: SettingsDataStore,
    @ApplicationScope private val externalScope: CoroutineScope
) {
    // Mantiene el objeto usuario actual.
    private var _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Representa el estado actual de la sesión (Loading, Authenticated, NotAuthenticated).
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        /**
         * Inicialización: Observa permanentemente el flujo del DataStore.
         * Actualiza el estado de la sesión automáticamente ante cualquier cambio en el disco.
         */
        externalScope.launch {
            dataStore.userFlow.collect { user ->
                _sessionState.value = if (user != null) {
                    SessionState.Authenticated(user)
                } else {
                    SessionState.NotAuthenticated
                }
                _currentUser.value = user
            }
        }
    }

    /**
     * Persiste los datos del usuario logueado en el almacenamiento.
     */
    suspend fun saveUser(user: User) {
        dataStore.saveLoggedUser(user)
    }

    /**
     * Elimina los datos del usuario del almacenamiento, forzando el estado NotAuthenticated.
     */
    suspend fun clearSession() {
        dataStore.saveLoggedUser(null)
    }
}