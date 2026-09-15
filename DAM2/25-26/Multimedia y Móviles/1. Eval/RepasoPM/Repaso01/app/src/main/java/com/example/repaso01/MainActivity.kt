package com.example.repaso01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.repaso01.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * MainActivity - Pantalla principal de la aplicación
 * 
 * Esta clase demuestra el uso de:
 * - ViewBinding: Para acceso seguro a las vistas sin findViewById
 * - Firebase Auth: Autenticación anónima de usuarios
 * - Firebase Crashlytics: Monitoreo y reporte de errores
 * - DataStore: Almacenamiento de preferencias de forma asíncrona
 * - Coroutines: Programación asíncrona con lifecycleScope
 * - Intent: Navegación entre Activities
 * - Activity Result API: Para recibir resultados de otras Activities
 */
class MainActivity : AppCompatActivity() {
    
    // ============================================================================
    // BINDING: ViewBinding - Acceso type-safe a las vistas del layout
    // ============================================================================
    // ViewBinding genera automáticamente una clase de binding por cada XML
    // que tiene <viewBinding enabled="true"> en build.gradle
    // Ventajas:
    //   - Type-safe: errores de compilación en lugar de runtime
    //   - Null-safe: no hay riesgo de NullPointerException
    //   - Más rápido que findViewById (no necesita traversar el árbol de vistas)
    private lateinit var binding: ActivityMainBinding
    
    // ============================================================================
    // AUTH: Firebase Authentication - Sistema de autenticación
    // ============================================================================
    // Firebase Auth proporciona:
    //   - Autenticación anónima (usado aquí)
    //   - Email/Password, Google, Facebook, etc.
    //   - Gestión de sesiones automática
    private lateinit var auth: FirebaseAuth
    
    // ============================================================================
    // CRASHLYTICS: Firebase Crashlytics - Monitoreo de errores en producción
    // ============================================================================
    // Crashlytics permite:
    //   - Rastrear errores y crashes automáticamente
    //   - Añadir logs personalizados con log()
    //   - Reportar excepciones manualmente con recordException()
    //   - Asociar usuarios con setUserId()
    private lateinit var crashlytics: FirebaseCrashlytics
    
    // ============================================================================
    // DATASTORE: DataStore Preferences - Almacenamiento de preferencias moderno
    // ============================================================================
    // DataStore es el reemplazo de SharedPreferences con:
    //   - API asíncrona basada en Coroutines y Flow
    //   - Type-safety con Preferences Keys
    //   - Transacciones atómicas
    //   - Sin bloqueo del hilo principal
    private lateinit var preferencesManager: PreferencesManager
    
    // ============================================================================
    // INTENT + DATA: Activity Result API - Sistema moderno para recibir resultados
    // ============================================================================
    // ActivityResultLauncher reemplaza a startActivityForResult (deprecated)
    // Ventajas:
    //   - Type-safe con contratos predefinidos
    //   - Registrado antes de onCreate (más seguro)
    //   - Separación clara entre lanzar y recibir resultado
    //   - Callback lambda en lugar de onActivityResult
    private val addUserLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // Contrato genérico
    ) { result ->
        // Este callback se ejecuta cuando AddUserActivity termina
        if (result.resultCode == RESULT_OK) {
            // ========================================================================
            // DATA: Extras del Intent - Pasar datos entre Activities
            // ========================================================================
            // putExtra() en origen -> getExtra() en destino
            // Tipos soportados: primitivos, String, Serializable, Parcelable
            result.data?.let { data ->
                val userName = data.getStringExtra(AddUserActivity.EXTRA_USER_NAME)
                val userAge = data.getIntExtra(AddUserActivity.EXTRA_USER_AGE, 0)
                val userId = data.getStringExtra(AddUserActivity.EXTRA_USER_ID)
                
                Toast.makeText(
                    this,
                    "Usuario agregado: $userName ($userAge años)",
                    Toast.LENGTH_SHORT
                ).show()
                
                // CRASHLYTICS: Registrar evento en Crashlytics para análisis
                crashlytics.log("Usuario agregado desde MainActivity: $userId")
            }
        }
    }
    
    /**
     * onCreate - Método del ciclo de vida llamado al crear la Activity
     * Orden del ciclo de vida: onCreate -> onStart -> onResume
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ========================================================================
        // BINDING: Inflado del layout con ViewBinding
        // ========================================================================
        // inflate() genera las vistas del XML y las asocia al objeto binding
        // Proceso: XML -> Parser -> View objects -> Binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView con binding.root establece la vista raíz
        setContentView(binding.root)
        
        // ========================================================================
        // Inicialización de servicios Firebase
        // ========================================================================
        
        // AUTH: Obtener instancia singleton de FirebaseAuth
        // getInstance() devuelve siempre la misma instancia (patrón Singleton)
        auth = FirebaseAuth.getInstance()
        
        // CRASHLYTICS: Obtener instancia de Crashlytics
        // Crashlytics está siempre activo y envía reportes automáticamente
        crashlytics = FirebaseCrashlytics.getInstance()
        
        // DATASTORE: Crear instancia del manager de preferencias
        // El context es necesario para acceder al DataStore
        preferencesManager = PreferencesManager(this)
        
        // Inicializar Firebase Auth anónimo
        initializeFirebaseAuth()
        
        // Observar DataStore
        observeUserData()
        
        // Setup click listeners
        setupClickListeners()
    }
    
    /**
     * Inicializa Firebase Authentication con login anónimo
     * Demuestra el uso de Coroutines para operaciones asíncronas
     */
    private fun initializeFirebaseAuth() {
        showLoading(true)
        
        // ========================================================================
        // COROUTINE: lifecycleScope - Coroutine scope atado al ciclo de vida
        // ========================================================================
        // lifecycleScope.launch crea una coroutine que:
        //   - Se cancela automáticamente cuando la Activity se destruye
        //   - Ejecuta código suspendido sin bloquear el UI thread
        //   - Permite usar funciones suspend como await()
        // Alternativas: viewModelScope (en ViewModels), GlobalScope (no recomendado)
        lifecycleScope.launch {
            try {
                if (auth.currentUser == null) {
                    // ============================================================
                    // AUTH: Autenticación anónima de Firebase
                    // ============================================================
                    // signInAnonymously() retorna un Task<AuthResult>
                    // await() convierte Task en suspend function (requiere kotlinx-coroutines-play-services)
                    // Ventajas del login anónimo:
                    //   - No requiere credenciales del usuario
                    //   - Útil para demos y pruebas
                    //   - Permite aplicar reglas de seguridad en Firestore
                    //   - Se puede migrar a cuenta permanente después
                    val result = auth.signInAnonymously().await()
                    val user = result.user
                    
                    // BINDING: Actualizar TextView usando ViewBinding
                    // binding.tvUserInfo hace referencia a android:id="@+id/tvUserInfo"
                    binding.tvUserInfo.text = "Usuario: ${user?.uid?.take(8) ?: "Anónimo"}"
                    
                    // ============================================================
                    // CRASHLYTICS: Configuración de usuario para tracking
                    // ============================================================
                    // setUserId asocia crashes con usuarios específicos
                    crashlytics.setUserId(user?.uid ?: "unknown")
                    // log() añade breadcrumbs al reporte de crash
                    crashlytics.log("Usuario autenticado anónimamente")
                    
                    Toast.makeText(
                        this@MainActivity,
                        "Autenticado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val user = auth.currentUser
                    binding.tvUserInfo.text = "Usuario: ${user?.uid?.take(8) ?: "Anónimo"}"
                    crashlytics.setUserId(user?.uid ?: "unknown")
                }
                
                showLoading(false)
                
            } catch (e: Exception) {
                showLoading(false)
                binding.tvUserInfo.text = "Usuario: Error de autenticación"
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                
                // CRASHLYTICS: Reportar excepción para análisis de errores
                crashlytics.recordException(e)
            }
        }
    }
    
    /**
     * Observa cambios en los datos del usuario usando Flow de DataStore
     * Demuestra el patrón Observer reactivo con Coroutines
     */
    private fun observeUserData() {
        // ========================================================================
        // COROUTINE + DATASTORE: Flow collector para datos reactivos
        // ========================================================================
        lifecycleScope.launch {
            // ================================================================
            // DATASTORE: Flow - Stream reactivo de datos
            // ================================================================
            // Flow es similar a LiveData pero más potente:
            //   - Parte de Kotlin Coroutines (no solo Android)
            //   - Soporta operadores como map, filter, combine
            //   - Cold stream: solo se activa cuando hay collector
            //   - collect() es una función suspend que se ejecuta continuamente
            // userCount emite un nuevo valor cada vez que cambia en DataStore
            preferencesManager.userCount.collect { count ->
                // Este bloque se ejecuta cada vez que userCount cambia
                Log.d("MainActivity", "Total de usuarios creados: $count")
            }
        }
    }
    
    /**
     * Configura los listeners de los botones
     * Demuestra el uso de lambdas para event handling
     */
    private fun setupClickListeners() {
        // ========================================================================
        // LAMBDA + INTENT: Click listener con expresión lambda
        // ========================================================================
        // setOnClickListener acepta una lambda (función anónima)
        // Sintaxis completa: setOnClickListener { view -> ... }
        // Sintaxis corta: setOnClickListener { ... } (sin parámetro view)
        binding.btnAddUser.setOnClickListener {
            // ================================================================
            // INTENT: Intent explícito para navegar entre Activities
            // ================================================================
            // Tipos de Intent:
            //   - Explícito: especifica la clase destino (usado aquí)
            //   - Implícito: especifica una acción, el sistema elige la app
            // Intent(context, Class) crea un intent explícito
            val intent = Intent(this, AddUserActivity::class.java)
            // launch() reemplaza a startActivityForResult
            addUserLauncher.launch(intent)
            
            // CRASHLYTICS: Registrar navegación para analytics
            crashlytics.log("Navegando a AddUserActivity")
        }
        
        // ========================================================================
        // LAMBDA + INTENT: Segundo botón con otra lambda
        // ========================================================================
        // Cada lambda es independiente y puede tener diferente lógica
        binding.btnViewUsers.setOnClickListener {
            // INTENT: Crear intent para ver lista de usuarios
            val intent = Intent(this, UserListActivity::class.java)
            // startActivity() lanza sin esperar resultado
            startActivity(intent)
            
            // CRASHLYTICS: Registrar navegación
            crashlytics.log("Navegando a UserListActivity")
        }

        // ========================================================================
        // LAMBDA + INTENT: Tercer botón para editar usuario
        // ========================================================================
        binding.btnEditUser.setOnClickListener {
            // INTENT: Crear intent para editar usuario
            val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
            
            // CRASHLYTICS: Registrar navegación
            crashlytics.log("Navegando a EditUserActivity")
        }
        
        // ========================================================================
        // LAMBDA + INTENT: Botón de favoritos
        // ========================================================================
        binding.btnViewFavorites.setOnClickListener {
            // INTENT: Navegar a FavoritesActivity
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            
            // CRASHLYTICS: Registrar navegación
            crashlytics.log("Navegando a FavoritesActivity")
        }
        
        // ========================================================================
        // LAMBDA + INTENT: Botón de filtrar por edad
        // ========================================================================
        binding.btnFilterByAge.setOnClickListener {
            // INTENT: Navegar a FilterByAgeActivity
            val intent = Intent(this, FilterByAgeActivity::class.java)
            startActivity(intent)
            
            // CRASHLYTICS: Registrar navegación
            crashlytics.log("Navegando a FilterByAgeActivity")
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnAddUser.isEnabled = !show
        binding.btnViewUsers.isEnabled = !show
        binding.btnEditUser.isEnabled = !show
        binding.btnViewFavorites.isEnabled = !show
        binding.btnFilterByAge.isEnabled = !show
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // CRASHLYTICS: Registrar destrucción de Activity
        crashlytics.log("MainActivity destruida")
    }
}