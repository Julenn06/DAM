package com.example.horoscopeapp.ui.horoscope

import androidx.lifecycle.ViewModel
import com.example.horoscopeapp.domain.model.HoroscopeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HoroscopeViewModel @Inject constructor(): ViewModel(){

    //Asi tengo una lista privada _horoscope, que pudede ser modificada desde esta clase
    //y una lista publica que se puede leer desde fuera pero no modificar
    private var _horoscope = MutableStateFlow<List<HoroscopeInfo>> (emptyList())
    val horoscope: StateFlow<List<HoroscopeInfo>> = _horoscope

    init{
        //como el onCreate delas Activitys
        _horoscope.value = listOf(HoroscopeInfo.Escorpio, HoroscopeInfo.Leo, HoroscopeInfo.Piscis)
    }
}