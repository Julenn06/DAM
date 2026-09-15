package com.example.elormovpmdm.data.modules

import com.example.elormovpmdm.data.login.LoginApiService
import com.example.elormovpmdm.data.meetings.MeetingsApiService
import com.example.elormovpmdm.data.schedule.ScheduleApiService
import com.example.elormovpmdm.data.students.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo de Hilt para la configuración de servicios de red.
 * Provee la instancia de Retrofit y sus servicios asociados como Singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Configura y provee la instancia base de Retrofit.
     * @return Cliente HTTP configurado con la URL base y el conversor de JSON (Gson).
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val ipClase: String = "192.168.0.12"
        return Retrofit.Builder()
            .baseUrl("http://$ipClase:8080/") // Se define el punto de acceso al backend.
            .addConverterFactory(
                GsonConverterFactory.create())
            .build()
    }

    /** Provee el servicio de API encargado de la autenticación. */
    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    /** Provee el servicio de API para la gestión de usuarios/estudiantes. */
    @Provides
    @Singleton
    fun provideStudentsApiService(retrofit: Retrofit): UsersApiService {
        return retrofit.create(UsersApiService::class.java)
    }

    /** Provee el servicio de API para la gestión de horarios. */
    @Provides
    @Singleton
    fun provideScheduleApiService(retrofit: Retrofit): ScheduleApiService {
        return retrofit.create(ScheduleApiService::class.java)
    }

    /** Provee el servicio de API para la gestión de reuniones y tutorías. */
    @Provides
    @Singleton
    fun provideMeetingsApiService(retrofit: Retrofit): MeetingsApiService {
        return retrofit.create(MeetingsApiService::class.java)
    }
}