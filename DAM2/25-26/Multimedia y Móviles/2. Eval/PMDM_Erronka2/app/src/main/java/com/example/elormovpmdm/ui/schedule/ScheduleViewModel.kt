package com.example.elormovpmdm.ui.schedule.userSchedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elormovpmdm.data.schedule.ScheduleApiService
import com.example.elormovpmdm.domain.SessionManager
import com.example.elormovpmdm.domain.model.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

/**
 * ViewModel encargado de la lógica de negocio para la gestión de horarios.
 * Se comunica con la API para recuperar la información del horario y expone
 * un flujo de estado (StateFlow) para que la UI se actualice reactivamente.
 */
@HiltViewModel
class ScheduleViewModel @Inject constructor(private val scheduleApiService: ScheduleApiService, private val sessionManager: SessionManager): ViewModel() {
    /**
     * Flujo interno mutable que almacena la lista de horarios.
     */
    private val _schedules = MutableStateFlow<List<Schedule>> (emptyList())

    /**
     * Flujo de estado público de solo lectura para ser observado por los fragmentos.
     */
    val schedules: StateFlow<List<Schedule>> = _schedules

    /**
     * Al inicializarse el ViewModel, se lanza automáticamente la petición
     * para obtener los módulos del horario.
     */
    init {
        getAllModules()
    }

    /**
     * Recupera el horario del usuario desde el servidor.
     * La lógica varía según si el usuario actual es un alumno (tipoId == 4)
     * o un profesor, consultando el endpoint correspondiente de la API.
     */
    fun getAllModules() {
        viewModelScope.launch {
            try{
                /**
                 * Ejecuta la llamada a la API en un hilo de entrada/salida (IO)
                 * para no bloquear el hilo principal de la interfaz de usuario.
                 */
                val response = withContext(Dispatchers.IO) {
                    if (sessionManager.currentUser.value!!.tipoId == 4) {
                        scheduleApiService.getStudentHorario(sessionManager.currentUser.value!!.id)
                    } else {
                        scheduleApiService.getHorario(sessionManager.currentUser.value!!.id)
                    }
                }

                Log.i("GVA", "ScheduleApiService llamado")

                if(response.isSuccessful) {
                    _schedules.value = response.body() ?: emptyList()
                } else {
                    Log.i("GVA", "Error API: ${response.code()} - ${response.errorBody()?.toString()}")
                }
            } catch (e: Exception) {
                Log.i("GVA", "Fallo total: ${e.message}")
            }
        }
    }
}