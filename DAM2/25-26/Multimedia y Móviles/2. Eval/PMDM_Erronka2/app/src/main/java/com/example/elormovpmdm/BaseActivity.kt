package com.example.elormovpmdm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.example.elormovpmdm.data.login.SessionState
import com.example.elormovpmdm.domain.SessionManager
import com.example.elormovpmdm.ui.login.LoginActivity
import com.example.elormovpmdm.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Actividad base del proyecto que centraliza la lógica de configuración y seguridad.
 * Utiliza [AndroidEntryPoint] para permitir la inyección de dependencias de Hilt.
 */
@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    protected lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Observa el flujo de modo oscuro.
         * Aplica MODE_NIGHT_YES o MODE_NIGHT_NO según el estado del DataStore.
         */
        lifecycleScope.launch {
            settingsDataStore.darkModeFlow.collect { isDark ->
                val mode = if (isDark) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }

                if(AppCompatDelegate.getDefaultNightMode() != mode) {
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }

        /**
         * Observa el flujo de idioma.
         * Actualiza los locales de la aplicación dinámicamente mediante [LocaleListCompat].
         */
        lifecycleScope.launch {
            settingsDataStore.languageFlow.collect { langCode ->
                val appLocale = LocaleListCompat.forLanguageTags(langCode)

                if(AppCompatDelegate.getApplicationLocales() != appLocale) {
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }
        }

        /**
         * Observa el estado de la sesión ([SessionState]).
         * Gestiona la navegación automática entre [LoginActivity] y [MainActivity]
         * para asegurar que el acceso a las actividades respete la autenticación.
         */
        lifecycleScope.launch {
            sessionManager.sessionState.collect { state ->
                when (state) {
                    is SessionState.Loading -> {

                    }
                    is SessionState.Authenticated -> {
                        if (this@BaseActivity is LoginActivity) {
                            val intent = Intent(this@BaseActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    is SessionState.NotAuthenticated -> {
                        if (this@BaseActivity !is LoginActivity) {
                            val intent = Intent(this@BaseActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}