package com.example.horoscopeapp.domain.model

import com.example.horoscopeapp.R

sealed class HoroscopeInfo (
    val title: Int,
    val image: Int){

    data object Aries: HoroscopeInfo(R.string.aries, R.drawable.aries)
    data object Tauro: HoroscopeInfo(R.string.taurus, R.drawable.tauro)
    data object Geminis: HoroscopeInfo(R.string.gemini, R.drawable.geminis)
    data object Cancer: HoroscopeInfo(R.string.cancer, R.drawable.cancer)
    data object Leo: HoroscopeInfo(R.string.leo, R.drawable.leo)
    data object Virgo: HoroscopeInfo(R.string.virgo, R.drawable.virgo)
    data object Libra: HoroscopeInfo(R.string.libra, R.drawable.libra)
    data object Escorpio: HoroscopeInfo(R.string.scorpio, R.drawable.escorpio)
    data object Sagitario: HoroscopeInfo(R.string.sagittarius, R.drawable.sagitario)
    data object Capricornio: HoroscopeInfo(R.string.capricorn, R.drawable.capricornio)
    data object Aquario: HoroscopeInfo(R.string.aquarius, R.drawable.aquario)
    data object Piscis: HoroscopeInfo(R.string.pisces, R.drawable.piscis)
}