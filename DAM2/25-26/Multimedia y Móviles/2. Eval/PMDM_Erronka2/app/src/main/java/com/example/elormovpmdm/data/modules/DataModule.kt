package com.example.elormovpmdm.data.modules

import android.content.Context
import com.example.elormovpmdm.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Calificador personalizado para identificar el CoroutineScope global.
 * Evita colisiones de tipos si existen múltiples definiciones de CoroutineScope.
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

/**
 * Módulo de Hilt encargado de proveer instancias relacionadas con datos y ciclos de vida.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provee una instancia única de [SettingsDataStore].
     * @param context Contexto de la aplicación inyectado automáticamente por Hilt.
     */
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
    }

    /**
     * Crea un [CoroutineScope] con ciclo de vida global.
     * Utiliza [SupervisorJob] para que el fallo de una corrutina hija no cancele el scope completo.
     * Se ejecuta por defecto en [Dispatchers.Default] (optimizado para tareas de CPU).
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}