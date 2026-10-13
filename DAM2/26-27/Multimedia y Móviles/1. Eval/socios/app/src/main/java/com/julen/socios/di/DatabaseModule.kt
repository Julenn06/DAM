package com.julen.socios.di

import android.content.Context
import com.julen.socios.data.local.AppDatabase
import com.julen.socios.data.local.BonoRegaloDao
import com.julen.socios.data.local.SocioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideSocioDao(database: AppDatabase): SocioDao {
        return database.socioDao()
    }

    @Provides
    fun provideBonoRegaloDao(database: AppDatabase): BonoRegaloDao {
        return database.bonoRegaloDao()
    }
}
