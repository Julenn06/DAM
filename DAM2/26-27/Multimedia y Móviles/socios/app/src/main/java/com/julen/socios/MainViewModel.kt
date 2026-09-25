package com.julen.socios

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.julen.socios.data.SocioRepository
import com.julen.socios.model.Socio
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.core.content.edit

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SocioRepository(application)
    private val prefs = application.getSharedPreferences("socios_settings", Context.MODE_PRIVATE)

    private val _weekOffset = MutableStateFlow(0)
    val weekOffset: StateFlow<Int> = _weekOffset

    private val _selectedDiaFiltroId = MutableStateFlow<Int>(DateUtils.getDiaSemanaActualId().coerceIn(1, 5))
    val selectedDiaFiltroId: StateFlow<Int> = _selectedDiaFiltroId

    private val _irpfPorcentaje = MutableStateFlow(
        prefs.getFloat("key_irpf", CalculoComisiones.IRPF_PORCENTAJE.toFloat()).toDouble()
    )
    val irpfPorcentaje: StateFlow<Double> = _irpfPorcentaje

    // Todos los socios globales
    val allSocios: StateFlow<List<Socio>> = repository.getAllSociosFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 1. Llamada para todos los socios de la semana activa (para totales y contadores de chips)
    val sociosSemana: StateFlow<List<Socio>> = _weekOffset.flatMapLatest { offset ->
        val semanaKey = DateUtils.getSemanaKey(offset)
        repository.getSociosPorSemanaFlow(semanaKey)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. Llamada específica por día seleccionado (1..5)
    val sociosFiltrados: StateFlow<List<Socio>> = _weekOffset.flatMapLatest { offset ->
        val semanaKey = DateUtils.getSemanaKey(offset)
        _selectedDiaFiltroId.flatMapLatest { diaId ->
            repository.getSociosPorDiaFlow(semanaKey, diaId)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setWeekOffset(offset: Int) {
        _weekOffset.value = offset
    }

    fun setSelectedDiaFiltroId(diaId: Int) {
        _selectedDiaFiltroId.value = diaId
    }

    fun setIrpfPorcentaje(porcentaje: Double) {
        _irpfPorcentaje.value = porcentaje
        prefs.edit { putFloat("key_irpf", porcentaje.toFloat()) }
    }

    fun addSocio(socio: Socio) {
        viewModelScope.launch {
            repository.addSocio(socio)
        }
    }

    fun updateSocio(socio: Socio) {
        viewModelScope.launch {
            repository.updateSocio(socio)
        }
    }

    fun deleteSocio(socioId: String) {
        viewModelScope.launch {
            repository.deleteSocio(socioId)
        }
    }

    fun toggleSocioHecho(socioId: String) {
        viewModelScope.launch {
            repository.toggleSocioHecho(socioId)
        }
    }

    fun clearSemana(semanaKey: String) {
        viewModelScope.launch {
            repository.clearSemana(semanaKey)
        }
    }
}
