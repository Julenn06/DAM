package com.example.elormovpmdm.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.elormovpmdm.BaseActivity
import com.example.elormovpmdm.ui.main.MainActivity
import com.example.elormovpmdm.R
import com.example.elormovpmdm.data.login.LoginState
import com.example.elormovpmdm.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Pantalla de inicio de sesión.
 * Hereda de [BaseActivity] para aprovechar el control de sesión y configuración global.
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val _other = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el diseño de borde a borde (Edge-to-Edge).
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gestión de insets para evitar que la UI quede bajo las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initUI()
    }

    /**
     * Inicializa los escuchadores de eventos de los componentes de la vista.
     */
    private fun initComponents() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Define la navegación hacia la pantalla principal tras un login exitoso.
     */
    private fun handleLoginSuccess () {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Configura la observación reactiva de flujos de datos (States).
     */
    private fun initUI() {
        // Observación exhaustiva del estado del login mediante LoginState.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect { state ->
                    when (state) {
                        is LoginState.Loading -> {
                            binding.btnLogin.isEnabled = false
                        }
                        is LoginState.Success -> {
                            handleLoginSuccess()
                        }
                        is LoginState.Error -> {
                            binding.btnLogin.isEnabled = true
                            Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                        is LoginState.Idle -> {
                            binding.btnLogin.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}