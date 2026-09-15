package com.example.elormovpmdm.ui.meetings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elormovpmdm.data.meetings.MeetingsApiService
import com.example.elormovpmdm.data.students.UsersApiService
import com.example.elormovpmdm.domain.SessionManager
import com.example.elormovpmdm.domain.model.Center
import com.example.elormovpmdm.domain.model.Meeting
import com.example.elormovpmdm.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel para la gestión de datos de reuniones, usuarios relacionados y centros.
 * Mantiene flujos reactivos para actualizar la UI en tiempo real.
 */
@HiltViewModel
class MeetingsViewModel @Inject constructor(
    private val meetingsApiService: MeetingsApiService,
    private val usersApiService: UsersApiService,
    private val sessionManager: SessionManager
): ViewModel(){

    // Flujos de estado para la UI
    private val _meetings = MutableStateFlow<MutableList<Meeting>> (mutableListOf())
    val meetings: StateFlow<MutableList<Meeting>> = _meetings

    private val _users = MutableStateFlow<List<User>> (emptyList())
    val users: StateFlow<List<User>> = _users

    private val _centers = MutableStateFlow<List<Center>> (emptyList())
    val centers: StateFlow<List<Center>> = _centers

    // Usuario actualmente autenticado obtenido del Singleton SessionManager
    val user = sessionManager.currentUser.value

    init {
        // Carga inicial de datos al instanciar el ViewModel
        getAllMeetings()
        getAllUsers()
        getAllCenters()
    }

    /**
     * Recupera todas las reuniones asociadas al usuario actual.
     */
    fun getAllMeetings() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    meetingsApiService.getReuniones(sessionManager.currentUser.value!!.id)
                }

                if(response.isSuccessful) {
                    _meetings.value = response.body() as MutableList<Meeting>
                } else {
                    Log.i("GVA", "Error API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.i("GVA", "FALLO TOTAL: ${e.message}")
            }
        }
    }

    /**
     * Recupera la lista de usuarios (estudiantes o profesores) según el rol del usuario actual.
     */
    fun getAllUsers() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    if (sessionManager.currentUser.value!!.tipoId == 4) {
                        usersApiService.getTeachersFromStudents(sessionManager.currentUser.value!!.id)
                    } else {
                        usersApiService.getStudentsFromTeacher(sessionManager.currentUser.value!!.id)
                    }
                }

                if (response.isSuccessful) {
                    _users.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.i("GVA", "FALLO TOTAL: ${e.message}")
            }
        }
    }

    /**
     * Recupera el catálogo de centros disponibles para las reuniones.
     */
    fun getAllCenters() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    meetingsApiService.getCenterList()
                }

                if (response.isSuccessful) {
                    _centers.value = response.body()?.CENTROS ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("GVA", "FALLO TOTAL: ${e.message}")
            }
        }
    }

    /**
     * Envía una solicitud para crear una nueva reunión.
     * @param createMeetingRequest Objeto con los datos de la reunión a persistir.
     */
    fun createMeeting(createMeetingRequest: Meeting) {
        try {
            // Se lanza una nueva corrutina para la ejecución de red
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        meetingsApiService.addMeeting(createMeetingRequest)
                    }
                    _meetings.value.add(response)
                    // Nota: Aquí faltaría actualizar la lista local de reuniones tras el éxito
                } catch (e: Exception) {
                    Log.e("GVA", "Error en addMeeting: ${e.message}")
                }
            }
        } catch (e: HttpException) {
            Log.e("GVA", "Error HTTP: ${e.code()}")
        } catch (e: IOException) {
            Log.e("GVA", "Error de red: ${e.message}")
        } catch (e: Exception) {
            Log.e("GVA", "Error inesperado", e)
        }
    }
    
    fun updateMeetingStatus(id: Int?, estado: String) {
        val map: Map<String, String> = mapOf<String, String>("estado" to estado)
        try {
            viewModelScope.launch { 
                meetingsApiService.updateMeetingStatus(id, map)
            }
            
        } catch (e: Exception) {
            Log.e("GVA", "FALLO TOTAL: ${e.message}")
        }
    }
}