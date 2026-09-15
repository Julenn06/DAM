package com.example.elormovpmdm

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.elormovpmdm.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensión de Context para delegar la instancia de DataStore.
 * "settings_prefs" es el nombre del archivo de preferencias en disco.
 */
private val Context.dataStore by preferencesDataStore(name = "settings_prefs")

/**
 * Clase encargada de la persistencia de configuraciones y datos de usuario.
 * Utiliza [Gson] para la serialización de objetos complejos.
 */
class SettingsDataStore(private val context: Context) {

    private val gson = Gson()

    companion object {
        /** Claves de tipado fuerte para acceder a las preferencias */
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val LANGUAGE_KEY = stringPreferencesKey("language_code")
        val USER_DATA_KEY = stringPreferencesKey("user_data")
    }

    /**
     * Persiste la preferencia de tema visual.
     * @param enabled True para activar el modo noche.
     */
    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    /**
     * Persiste el código de idioma seleccionado.
     * @param langCode Código ISO del idioma (ej: "es", "en").
     */
    suspend fun saveLanguage(langCode: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = langCode
        }
    }

    /**
     * Serializa y guarda el objeto [User] en formato JSON.
     * @param user Objeto de usuario o null para eliminar la información.
     */
    suspend fun saveLoggedUser(user: User?) {
        context.dataStore.edit { prefs ->
            prefs[USER_DATA_KEY] = gson.toJson(user)
        }
    }

    /**
     * Flujo reactivo que emite el estado del modo oscuro.
     * Por defecto es false.
     */
    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[DARK_MODE_KEY] ?: false
    }

    /**
     * Flujo reactivo que emite el código de idioma.
     * Por defecto es "es" (Español).
     */
    val languageFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LANGUAGE_KEY] ?: "es"
    }

    /**
     * Flujo reactivo que emite el objeto [User] deserializado.
     * Emite null si no existen datos guardados.
     */
    val userFlow: Flow<User?> = context.dataStore.data.map { prefs ->
        val json = prefs[USER_DATA_KEY]
        if (!json.isNullOrEmpty()) {
            gson.fromJson(json, User::class.java)
        } else {
            null
        }
    }
}