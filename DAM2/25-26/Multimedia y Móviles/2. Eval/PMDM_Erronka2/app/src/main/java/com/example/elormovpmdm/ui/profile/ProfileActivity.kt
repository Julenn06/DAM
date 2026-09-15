package com.example.elormovpmdm.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.elormovpmdm.BaseActivity
import com.example.elormovpmdm.R
import com.example.elormovpmdm.databinding.ActivityProfileBinding
import com.example.elormovpmdm.domain.model.User
import com.example.elormovpmdm.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Actividad encargada de gestionar el perfil del usuario.
 * Permite visualizar datos personales, cambiar el idioma de la aplicación,
 * alternar el tema (oscuro/claro) y cerrar la sesión actual.
 */
class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponent()
        initUI()
    }

    /**
     * Inicializa los componentes interactivos de la vista y configura sus listeners.
     */
    private fun initComponent() {
        /**
         * Finaliza la actividad actual para regresar a la anterior.
         */
        binding.btnBack.setOnClickListener {
            finish()
        }

        /**
         * Inicia la actividad de cámara personalizada para gestionar fotos de perfil.
         */
        binding.btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        /**
         * Configuración del selector de idiomas (Spinner/AutoCompleteTextView).
         */
        val languageOptions = arrayOf(
            getString(R.string.spanish),
            getString(R.string.basque),
            getString(R.string.english)
        )
        
        val codes = arrayOf("es", "eu", "en")
        
        val languageAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languageOptions)
        
        binding.autoCompleteTextView.setAdapter(languageAdapter)

        /**
         * Guarda el código de idioma seleccionado en el DataStore de forma asíncrona.
         */
        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedCode = codes[position]
            lifecycleScope.launch {
                settingsDataStore.saveLanguage(selectedCode)
            }
        }

        /**
         * Alterna el estado del modo oscuro persistido en las preferencias.
         */
        binding.btnThemeChange.setOnClickListener {
            lifecycleScope.launch {
                val isDark = settingsDataStore.darkModeFlow.first()
                settingsDataStore.saveDarkMode(!isDark)
            }
        }

        /**
         * Limpia la sesión del usuario y redirige a la pantalla de Login.
         */
        binding.btnLogout.setOnClickListener { 
            lifecycleScope.launch { 
                sessionManager.clearSession()
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * Recupera la información del usuario desde el SessionManager y puebla la interfaz.
     */
    private fun initUI() {
        val user: User = sessionManager.currentUser.value!!

        val email: String = user.email
        val userName: String = user.username
        val name: String = user.nombre
        val surname: String = user.apellidos
        val id: Int = user.id
        val address: String = user.direccion
        val phone: String = user.telefono1
        
        binding.nombreProfesor.text = "$name $surname"
        binding.tvUserName.text = userName
        binding.userID.text = id.toString()
        binding.userAddress.text = address
        binding.userPhone.text = phone
        binding.userEmail.text = email
    }
}