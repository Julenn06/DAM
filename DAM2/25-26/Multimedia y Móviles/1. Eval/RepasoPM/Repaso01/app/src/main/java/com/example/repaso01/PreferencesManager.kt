package com.example.repaso01

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ============================================================================
 * DATASTORE: Preferences DataStore - Sistema moderno de persistencia
 * ============================================================================
 * 
 * DataStore reemplaza a SharedPreferences con:
 * ============================================
 * 1. API asíncrona basada en Coroutines (no bloquea UI thread)
 * 2. Type-safety con PreferencesKey<T>
 * 3. Transacciones atómicas (no hay estados inconsistentes)
 * 4. API reactiva con Flow (observa cambios automáticamente)
 * 5. Manejo de errores más robusto
 * 
 * CUÁNDO USAR DATASTORE:
 * ======================
 * - Preferencias de usuario (tema, idioma, etc.)
 * - Settings de la app
 * - Datos pequeños key-value
 * - Cuando necesitas observar cambios reactivamente
 * 
 * CUÁNDO NO USAR:
 * ===============
 * - Datos grandes (usar Room o Firestore)
 * - Datos complejos relacionales (usar Room)
 * - Datos que requieren queries (usar Room)
 */

// ================================================================================
// DATASTORE: Extension property para crear DataStore singleton
// ================================================================================
// by preferencesDataStore:
//   - Crea DataStore lazy (solo cuando se accede por primera vez)
//   - Singleton: una sola instancia por contexto
//   - name: nombre del archivo de preferencias
//   - Archivo guardado en: /data/data/<package>/files/datastore/user_preferences.preferences_pb
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * PreferencesManager - Clase wrapper para DataStore
 * 
 * PROPÓSITO:
 * ==========
 * - Encapsular la lógica de DataStore
 * - Proporcionar API simple y type-safe
 * - Centralizar todas las keys en un lugar
 * - Facilitar testing con dependency injection
 */
class PreferencesManager(private val context: Context) {

    companion object {
        // ====================================================================
        // DATASTORE: Preferences Keys - Type-safe keys para acceder a datos
        // ====================================================================
        // PreferencesKey<T> garantiza type-safety:
        //   - stringPreferencesKey retorna PreferencesKey<String>
        //   - intPreferencesKey retorna PreferencesKey<Int>
        //   - Solo puedes guardar/leer el tipo correcto
        // 
        // Convención de nombres:
        //   - UPPER_SNAKE_CASE para constantes
        //   - Sufijo _KEY para claridad
        //   - Nombre descriptivo del dato
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_COUNT_KEY = intPreferencesKey("user_count")
        private val LAST_USER_ID_KEY = stringPreferencesKey("last_user_id")
    }

    // ============================================================================
    // DATASTORE + COROUTINE: Funciones suspend para escritura
    // ============================================================================
    
    /**
     * Guarda el nombre del usuario
     * 
     * DATASTORE: edit {} - Transacción atómica
     * ========================================
     * - suspend function: debe llamarse desde coroutine
     * - edit {} agrupa cambios en una transacción
     * - Si hay error, se hace rollback automático
     * - Garantiza consistencia de datos
     * 
     * Ejemplo de uso:
     * ```
     * lifecycleScope.launch {
     *     preferencesManager.saveUserName("Juan")
     * }
     * ```
     */
    suspend fun saveUserName(name: String) {
        // edit recibe lambda que opera sobre MutablePreferences
        context.dataStore.edit { preferences ->
            // Sintaxis: preferences[KEY] = value
            // Type-safe: solo puedes asignar String a USER_NAME_KEY
            preferences[USER_NAME_KEY] = name
        }
    }

    /**
     * Retorna Flow que emite el nombre del usuario
     * 
     * DATASTORE: Flow - Stream reactivo de datos
     * ==========================================
     * Flow características:
     * - Cold stream: solo se activa cuando hay collector
     * - Emite nuevo valor cada vez que el dato cambia
     * - collect() es suspend function
     * - Cancela automáticamente con el scope
     * 
     * Flow vs LiveData:
     * - Flow: parte de Kotlin, funciona en cualquier contexto
     * - LiveData: específico de Android, atado al lifecycle
     * - Flow: más operadores (map, filter, combine, etc.)
     * 
     * Ejemplo de uso:
     * ```
     * lifecycleScope.launch {
     *     preferencesManager.userName.collect { name ->
     *         textView.text = name
     *     }
     * }
     * ```
     */
    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        // map transforma cada emisión del Flow
        // ?: proporciona valor por defecto si la key no existe
        preferences[USER_NAME_KEY] ?: "Anónimo"
    }

    /**
     * Incrementa el contador de usuarios
     * 
     * DATASTORE: Actualización atómica
     * =================================
     * - Lee el valor actual
     * - Incrementa
     * - Guarda el nuevo valor
     * - Todo en una transacción atómica
     */
    suspend fun incrementUserCount() {
        context.dataStore.edit { preferences ->
            val currentCount = preferences[USER_COUNT_KEY] ?: 0
            preferences[USER_COUNT_KEY] = currentCount + 1
        }
    }

    /**
     * Flow que emite el contador de usuarios
     * Útil para mostrar estadísticas en tiempo real
     */
    val userCount: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[USER_COUNT_KEY] ?: 0
    }

    /**
     * Guarda el ID del último usuario creado
     * Útil para tracking o deshacer operaciones
     */
    suspend fun saveLastUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_USER_ID_KEY] = userId
        }
    }

    /**
     * Flow que emite el último ID de usuario
     */
    val lastUserId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LAST_USER_ID_KEY] ?: ""
    }

    /**
     * Limpia todas las preferencias
     * 
     * DATASTORE: clear() borra todas las keys
     * ========================================
     * - Útil para logout
     * - Reset de configuración
     * - Limpiar datos de testing
     */
    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
