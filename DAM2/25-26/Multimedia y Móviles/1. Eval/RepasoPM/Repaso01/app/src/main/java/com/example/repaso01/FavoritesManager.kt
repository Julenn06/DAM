package com.example.repaso01

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore extension para FavoritesManager
private val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites_preferences")

/**
 * FavoritesManager - Gestiona usuarios favoritos con DataStore
 * 
 * DATASTORE PREFERENCES: Almacenamiento de favoritos
 * ====================================================
 * Usa DataStore para guardar Set<String> de IDs favoritos
 * - Persistente entre sesiones
 * - Asíncrono con Flow
 * - Type-safe con PreferencesKey
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. DATASTORE: Preferencias con Flow reactivo
 * 2. COROUTINE: Operaciones suspend para I/O
 * 3. FLOW: Observación reactiva de cambios
 * 4. Set<String>: Colección sin duplicados
 */
class FavoritesManager(private val context: Context) {

    companion object {
        // PreferencesKey para el Set de IDs favoritos
        private val FAVORITES_KEY = stringSetPreferencesKey("favorite_user_ids")
    }

    /**
     * FLOW: Observa cambios en favoritos en tiempo real
     * ==================================================
     * Flow es un stream reactivo que emite valores cuando cambian
     * - Asíncrono y no bloqueante
     * - Se actualiza automáticamente
     * - Lifecycle-aware en Activities
     */
    val favoritesFlow: Flow<Set<String>> = context.favoritesDataStore.data
        .map { preferences ->
            // Obtener Set de favoritos, o Set vacío si no existe
            preferences[FAVORITES_KEY] ?: emptySet()
        }

    /**
     * COROUTINE: Añade un usuario a favoritos
     * ========================================
     * suspend fun: Función que puede pausarse sin bloquear el thread
     * Se ejecuta en coroutine (lifecycleScope.launch)
     */
    suspend fun addToFavorites(userId: String) {
        context.favoritesDataStore.edit { preferences ->
            // Obtener Set actual
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            
            // Crear nuevo Set con el usuario añadido
            // toMutableSet() porque Set es inmutable
            val updatedFavorites = currentFavorites.toMutableSet().apply {
                add(userId)
            }
            
            // Guardar Set actualizado
            preferences[FAVORITES_KEY] = updatedFavorites
        }
    }

    /**
     * Elimina un usuario de favoritos
     */
    suspend fun removeFromFavorites(userId: String) {
        context.favoritesDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            
            // Crear nuevo Set sin el usuario
            val updatedFavorites = currentFavorites.toMutableSet().apply {
                remove(userId)
            }
            
            preferences[FAVORITES_KEY] = updatedFavorites
        }
    }

    /**
     * Verifica si un usuario es favorito
     * suspend: necesita leer de DataStore
     */
    suspend fun isFavorite(userId: String): Boolean {
        var result = false
        context.favoritesDataStore.data.map { preferences ->
            val favorites = preferences[FAVORITES_KEY] ?: emptySet()
            result = userId in favorites
        }.collect { }
        return result
    }

    /**
     * Alterna el estado de favorito (toggle)
     * Si es favorito lo quita, si no lo es lo añade
     */
    suspend fun toggleFavorite(userId: String): Boolean {
        var isFav = false
        context.favoritesDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            val updatedFavorites = currentFavorites.toMutableSet()
            
            if (userId in currentFavorites) {
                updatedFavorites.remove(userId)
                isFav = false
            } else {
                updatedFavorites.add(userId)
                isFav = true
            }
            
            preferences[FAVORITES_KEY] = updatedFavorites
        }
        return isFav
    }

    /**
     * Limpia todos los favoritos
     */
    suspend fun clearFavorites() {
        context.favoritesDataStore.edit { preferences ->
            preferences.remove(FAVORITES_KEY)
        }
    }
}
