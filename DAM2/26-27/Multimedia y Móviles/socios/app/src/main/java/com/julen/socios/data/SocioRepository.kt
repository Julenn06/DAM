package com.julen.socios.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.julen.socios.model.Socio

class SocioRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("socios_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val keySocios = "KEY_SOCIOS_LIST"

    fun getAllSocios(): List<Socio> {
        val json = prefs.getString(keySocios, null) ?: return emptyList()
        val type = object : TypeToken<List<Socio>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getSociosPorSemana(semanaKey: String): List<Socio> {
        return getAllSocios().filter { it.semanaKey == semanaKey }
    }

    fun addSocio(socio: Socio) {
        val list = getAllSocios().toMutableList()
        list.add(0, socio)
        saveAll(list)
    }

    fun updateSocio(socio: Socio) {
        val list = getAllSocios().toMutableList()
        val index = list.indexOfFirst { it.id == socio.id }
        if (index != -1) {
            list[index] = socio
            saveAll(list)
        }
    }

    fun deleteSocio(socioId: String) {
        val list = getAllSocios().toMutableList()
        list.removeAll { it.id == socioId }
        saveAll(list)
    }

    fun toggleSocioHecho(socioId: String): Boolean {
        val list = getAllSocios().toMutableList()
        val index = list.indexOfFirst { it.id == socioId }
        if (index != -1) {
            val updated = list[index].copy(hecho = !list[index].hecho)
            list[index] = updated
            saveAll(list)
            return updated.hecho
        }
        return false
    }

    fun clearSemana(semanaKey: String) {
        val list = getAllSocios().toMutableList()
        list.removeAll { it.semanaKey == semanaKey }
        saveAll(list)
    }

    private fun saveAll(list: List<Socio>) {
        val json = gson.toJson(list)
        prefs.edit().putString(keySocios, json).apply()
    }
}
