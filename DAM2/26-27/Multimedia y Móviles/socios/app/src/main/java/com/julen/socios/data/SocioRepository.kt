package com.julen.socios.data

import android.content.Context
import com.julen.socios.data.local.AppDatabase
import com.julen.socios.data.local.SocioDao
import com.julen.socios.data.local.SocioEntity
import com.julen.socios.model.Socio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SocioRepository(context: Context) {
    private val socioDao: SocioDao = AppDatabase.getDatabase(context).socioDao()

    fun getAllSociosFlow(): Flow<List<Socio>> {
        return socioDao.getAllSociosFlow().map { entities ->
            entities.map { it.toSocio() }
        }
    }

    fun getSociosPorSemanaFlow(semanaKey: String): Flow<List<Socio>> {
        return socioDao.getSociosPorSemanaFlow(semanaKey).map { entities ->
            entities.map { it.toSocio() }
        }
    }

    fun getSociosPorDiaFlow(semanaKey: String, diaId: Int): Flow<List<Socio>> {
        return socioDao.getSociosPorDiaFlow(semanaKey, diaId).map { entities ->
            entities.map { it.toSocio() }
        }
    }

    fun getAllSocios(): List<Socio> = runBlocking(Dispatchers.IO) {
        socioDao.getAllSocios().map { it.toSocio() }
    }

    fun getSociosPorSemana(semanaKey: String): List<Socio> = runBlocking(Dispatchers.IO) {
        socioDao.getSociosPorSemana(semanaKey).map { it.toSocio() }
    }

    fun addSocio(socio: Socio) = runBlocking(Dispatchers.IO) {
        socioDao.insertSocio(SocioEntity.fromSocio(socio))
    }

    fun updateSocio(socio: Socio) = runBlocking(Dispatchers.IO) {
        socioDao.updateSocio(SocioEntity.fromSocio(socio))
    }

    fun deleteSocio(socioId: String) = runBlocking(Dispatchers.IO) {
        socioDao.deleteSocio(socioId)
    }

    fun toggleSocioHecho(socioId: String): Boolean = runBlocking(Dispatchers.IO) {
        val existing = socioDao.getSocioById(socioId)
        if (existing != null) {
            val updated = existing.copy(hecho = !existing.hecho)
            socioDao.updateSocio(updated)
            updated.hecho
        } else {
            false
        }
    }

    fun clearSemana(semanaKey: String) = runBlocking(Dispatchers.IO) {
        socioDao.clearSemana(semanaKey)
    }
}
