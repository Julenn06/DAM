package com.example.elormovpmdm.ui.schedule


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elormovpmdm.data.students.UsersApiService
import com.example.elormovpmdm.domain.SessionManager
import com.example.elormovpmdm.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar la lógica de obtención de profesores para la vista de horarios.
 * Actúa como intermediario entre el servicio de red (UsersApiService) y el fragmento,
 * manteniendo el estado de la lista de usuarios de forma reactiva.
 */
@HiltViewModel
class TeachersSchedulesViewModel @Inject constructor(
    private val usersApiService: UsersApiService,
    private val sessionManager: SessionManager
): ViewModel() {
    /**
     * Flujo interno mutable que gestiona la lista de usuarios encontrados.
     */
    private val _users = MutableStateFlow<List<User>> (emptyList())

    /**
     * Flujo de estado público que expone la lista de usuarios para ser observada por la UI.
     */
    val users: StateFlow<List<User>> = _users

    /**
     * Al instanciar el ViewModel, se lanza automáticamente la petición para cargar los usuarios.
     */
    init {
        getAllUsers()
    }

    /**
     * Recupera la lista de profesores asociados al alumno actualmente autenticado.
     * La operación se realiza de forma asíncrona utilizando Corrutinas en el hilo de IO.
     */
    fun getAllUsers() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    usersApiService.getTeachersFromStudents(sessionManager.currentUser.value!!.id)
                }

                if (response.isSuccessful) {
                    _users.value = response.body() ?: emptyList()
                } else {
                    Log.i("GVA", "Error API: ${response.code()} - ${response.errorBody()?.toString()}")
                }
            } catch (e: Exception) {
                Log.i("GVA", "FALLO TOTAL: ${e.message}")
            }
        }
    }
}

