package com.example.elormovpmdm.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.elormovpmdm.BaseActivity
import com.example.elormovpmdm.R
import com.example.elormovpmdm.databinding.ActivityMainBinding
import com.example.elormovpmdm.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal que actúa como contenedor de la navegación por fragmentos.
 * Hereda de [BaseActivity] para mantener la consistencia de sesión y configuración.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de paddings para respetar las barras del sistema (Status y Navigation bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()
    }

    private fun initUI() {
        initNavigation()
    }

    /**
     * Configura el componente Navigation basándose en el rol del usuario.
     * Selecciona el grafo XML y el menú de la BottomBar correspondientes.
     */
    private fun initNavigation() {
        // Obtiene el tipo de usuario desde la sesión inyectada por BaseActivity.
        val userType = sessionManager.currentUser.value!!.tipoId

        val navHost: NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController

        // Selección dinámica del grafo de navegación.
        val navGraph = if (userType == 4) {
            navController.navInflater.inflate(R.navigation.students_graph)
        } else {
            navController.navInflater.inflate(R.navigation.teachers_graph)
        }

        // Personalización de la barra inferior y destino inicial según el rol (4 = Alumno).
        if (userType == 4) {
            binding.bottomBar.menu.clear()
            binding.bottomBar.inflateMenu(R.menu.student_bottom_menu)
            navGraph.setStartDestination(R.id.scheduleFragment)
        } else {
            binding.bottomBar.menu.clear()
            binding.bottomBar.inflateMenu(R.menu.teacher_bottom_menu)
            navGraph.setStartDestination(R.id.scheduleFragment)
        }

        navController.graph = navGraph
        binding.bottomBar.setupWithNavController(navController)

        // Escuchador para actualizar el título del Toolbar según el fragmento visible.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.scheduleFragment -> binding.tvToolbarTitle.text = getString(R.string.schedule)
                R.id.studentsFragment -> binding.tvToolbarTitle.text = getString(R.string.students)
                R.id.meetingsFragment -> binding.tvToolbarTitle.text = getString(R.string.meetings)
            }
        }
    }

    private fun initComponents() {
        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}