package com.example.elormovpmdm.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elormovpmdm.data.login.LoginApiService
import com.example.elormovpmdm.data.login.LoginState
import com.example.elormovpmdm.domain.SessionManager
import com.example.elormovpmdm.domain.model.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel encargado de la lógica de autenticación.
 * * Utiliza [HiltViewModel] para la inyección de dependencias y mantiene el estado
 * de la vista de forma persistente a cambios de configuración.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginApiService: LoginApiService,
    private val sessionManager: SessionManager
): ViewModel() {

    // Estado interno mutable que controla la UI del login.
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)

    /**
     * Flujo de estado público que observa la Activity.
     */
    val state: StateFlow<LoginState> = _state

    /**
     * Ejecuta el proceso de inicio de sesión de forma asíncrona.
     * * @param email Correo proporcionado por el usuario.
     * @param password Contraseña proporcionada por el usuario.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.i("GVA", "Corrutina lanzada")

            _state.value = LoginState.Loading

            // Ejecución de la llamada de red en el hilo de IO para no bloquear la UI.
            val result = withContext(Dispatchers.IO) {
                try {
                    loginApiService.login(LoginRequest(email, password))
                } catch (e: Exception) {
                    Log.e("GVA", "EXCEPCIÓN DETECTADA: ${e.javaClass.simpleName}")
                    Log.e("GVA", "MENSAJE: ${e.message}")
                    e.printStackTrace()
                    null
                }
            }

            // Procesamiento de la respuesta de Retrofit.
            if (result != null && result.isSuccessful) {
                val user = result.body()
                if (user != null) {
                    if (user.tipoId == 3 || user.tipoId == 4) {
                        _state.value = LoginState.Success
                        // Persiste el usuario en el SessionManager (DataStore).
                        sessionManager.saveUser(user)
                    } else {
                        _state.value = LoginState.Error("Usuario no autorizado")
                    }
                } else {
                    _state.value = LoginState.Error("Respuesta vacía del servidor")
                }
            } else {
                // Gestión de errores basada en códigos de estado HTTP.
                val errorCode = result?.code()
                val errorContent = result?.errorBody()?.string()

                Log.e("GVA", "DETALLE DEL ERROR: Código $errorCode")
                Log.e("GVA", "CONTENIDO: $errorContent")

                val errorMsg = when(errorCode) {
                    401 -> "Contraseña incorrecta"
                    404 -> "Ruta no encontrada (Revisa LoginApiService)"
                    500 -> "Error interno del servidor Java"
                    else -> "Error desconocido: $errorCode"
                }
                _state.value = LoginState.Error(errorMsg)
            }
        }
    }
}