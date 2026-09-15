package dam2.reto1.grupo1

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    private var currentLanguage: String? = null
    private var currentTheme: String? = null

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("language", "es") ?: "es"
        val locale = Locale(langCode)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        currentTheme = getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("theme", "light")
        currentLanguage = getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("language", "es")
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val savedLang = sharedPreferences.getString("language", "es")
        val savedTheme = sharedPreferences.getString("theme", "light")

        if (currentLanguage != savedLang || currentTheme != savedTheme) {
            recreate()
        }
    }

    private fun applyTheme() {
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val theme = sharedPreferences.getString("theme", "light")
        when (theme) {
            "dark" -> setTheme(R.style.AppTheme_Dark)
            else -> setTheme(R.style.AppTheme_Light)
        }
    }
}