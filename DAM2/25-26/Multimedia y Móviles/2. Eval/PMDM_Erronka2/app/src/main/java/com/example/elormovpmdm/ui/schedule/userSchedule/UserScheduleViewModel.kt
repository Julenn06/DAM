package com.example.elormovpmdm.ui.schedule.userSchedule

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elormovpmdm.data.schedule.ScheduleApiService
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
 * ViewModel específico para la gestión del horario de un usuario concreto.
 * Utiliza SavedStateHandle para recuperar parámetros de navegación y expone
 * el estado del horario a través de un StateFlow reactivo.
 */
@HiltViewModel
class UserScheduleViewModel @Inject constructor(private val scheduleApiService: ScheduleApiService, savedStateHandle: SavedStateHandle): ViewModel() {
    /**
     * Flujo interno mutable que contiene la lista de horarios del usuario.
     */
    private val _schedules = MutableStateFlow<List<Schedule>> (emptyList())

    /**
     * Flujo de estado público expuesto a la Activity para su observación.
     */
    val schedules: StateFlow<List<Schedule>> = _schedules

    /**
     * Identificador del usuario recuperado automáticamente de los argumentos
     * de navegación (Safe Args) mediante el SavedStateHandle.
     */
    private val userId: Int = savedStateHandle["user_id"] ?: 0

    /**
     * Al inicializar el ViewModel, se dispara la carga de los módulos del horario.
     */
    init {
        getAllModules()
    }

    /**
     * Realiza la petición al servicio de API para obtener el horario del usuario.
     * La operación se ejecuta de forma asíncrona en el hilo de IO.
     */
    fun getAllModules() {
        viewModelScope.launch {
            try{
                val response = withContext(Dispatchers.IO) {
                    scheduleApiService.getHorario(userId)
                }

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