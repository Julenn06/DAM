package com.julen.socios.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.julen.socios.model.BonoRegaloInfo

class BonoRegaloRepository(context: Context) {
    private val prefs = context.getSharedPreferences("bonos_regalo_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getBonoRegalo(semanaKey: String): BonoRegaloInfo {
        val json = prefs.getString("bono_$semanaKey", null)
        return if (json != null) {
            try {
                gson.fromJson(json, BonoRegaloInfo::class.java)
            } catch (_: Exception) {
                BonoRegaloInfo(semanaKey = semanaKey)
            }
        } else {
            BonoRegaloInfo(semanaKey = semanaKey)
        }
    }

    fun saveBonoRegalo(bonoInfo: BonoRegaloInfo) {
        val json = gson.toJson(bonoInfo)
        prefs.edit { putString("bono_${bonoInfo.semanaKey}", json) }
    }

    fun deleteBonoRegalo(semanaKey: String) {
        prefs.edit { remove("bono_$semanaKey") }
    }

    fun getAllBonosRegalo(): Map<String, BonoRegaloInfo> {
        val map = mutableMapOf<String, BonoRegaloInfo>()
        for ((key, value) in prefs.all) {
            if (key.startsWith("bono_") && value is String) {
                val semanaKey = key.removePrefix("bono_")
                try {
                    val bono = gson.fromJson(value, BonoRegaloInfo::class.java)
                    if (bono.activo) {
                        map[semanaKey] = bono
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return map
    }
}
