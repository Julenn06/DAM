package com.example.elormovpmdm.ui.students

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
 * ViewModel encargado de la lógica de negocio para la gestión de alumnos.
 * Facilita la obtención de la lista de estudiantes asociados a un profesor específico
 * y expone los datos de forma reactiva mediante StateFlow.
 */
@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val usersApiService: UsersApiService,
    private val sessionManager: SessionManager
): ViewModel() {
    /**
     * Flujo interno mutable que almacena la lista de alumnos.
     */
    private val _users = MutableStateFlow<List<User>> (emptyList())

    /**
     * Flujo de estado público de solo lectura para ser observado por el fragmento.
     */
    val users: StateFlow<List<User>> = _users

    /**
     * Bloque de inicialización que dispara la carga de alumnos al crear el ViewModel.
     */
    init {
        getAllStudents()
    }

    /**
     * Realiza una petición asíncrona para obtener todos los alumnos vinculados
     * al ID del profesor actual (obtenido del SessionManager).
     */
    fun getAllStudents() {
        viewModelScope.launch {
            try {
                /**
                 * Ejecuta la llamada a la API en el hilo de IO para optimizar el rendimiento.
                 */
                val response = withContext(Dispatchers.IO) {
                    usersApiService.getStudentsFromTeacher(sessionManager.currentUser.value!!.id)
                }

                if (response.isSuccessful) {
                    _users.value = response.body() ?: emptyList()
                } else {
                    Log.i("GVA", "Error API: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.i("GVA", "FALLO TOTAL: ${e.message}")
            }
        }
    }

}