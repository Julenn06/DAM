package com.example.repaso01

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repaso01.databinding.ActivityFilterByAgeBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.launch

/**
 * FilterByAgeActivity - Pantalla que filtra usuarios por edad mínima
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. RECYCLERVIEW: Lista eficiente con adapter pattern
 * 2. LAMBDA: Callback para clicks en items
 * 3. BINDING: ViewBinding para acceso a vistas
 * 4. FIRESTORE: Lectura de colección completa
 * 5. COROUTINE: Operaciones asíncronas
 * 6. FILTER: Filtrado de lista con expresión lambda
 * 7. EDITTEXT: Input de datos del usuario
 * 8. CRASHLYTICS: Logging de eventos y errores
 * 9. Manejo de estados: loading, empty, error, success
 * 10. VALIDATION: Validación de entrada del usuario
 */
class FilterByAgeActivity : AppCompatActivity() {

    // ============================================================================
    // BINDING: ViewBinding para esta Activity
    // ============================================================================
    // Generada desde activity_filter_by_age.xml
    // Proporciona: etMinAge, btnFilter, recyclerView, progressBar, tvEmpty, btnBack
    private lateinit var binding: ActivityFilterByAgeBinding
    
    // ============================================================================
    // FIRESTORE: Manager para leer datos
    // ============================================================================
    private val firestore = FirestoreManager()
    
    // ============================================================================
    // CRASHLYTICS: Monitoreo y logging
    // ============================================================================
    private val crashlytics = FirebaseCrashlytics.getInstance()
    
    // ============================================================================
    // RECYCLERVIEW: Adapter personalizado para mostrar usuarios
    // ============================================================================
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // BINDING: Inflar layout con ViewBinding
        binding = ActivityFilterByAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CRASHLYTICS: Registrar evento de apertura
        crashlytics.log("User opened FilterByAge screen")

        setupBackButton()
        setupRecyclerView()
        setupFilterButton()
    }

    /**
     * Configura el botón de retroceso
     */
    private fun setupBackButton() {
        // LAMBDA: Botón para volver a MainActivity
        binding.btnBack.setOnClickListener {
            finish() // Cierra la Activity y vuelve a la anterior
        }
    }

    /**
     * Configura el RecyclerView con su adapter y layout manager
     * 
     * COMPONENTES DE RECYCLERVIEW:
     * ============================
     * 1. Adapter: Gestiona datos y crea vistas (UserAdapter)
     * 2. ViewHolder: Contiene referencias a vistas de cada item
     * 3. LayoutManager: Define cómo se organizan los items
     */
    private fun setupRecyclerView() {
        // ========================================================================
        // RECYCLERVIEW + LAMBDA: Crear adapter con callback lambda
        // ========================================================================
        userAdapter = UserAdapter(
            // LAMBDA: Click en usuario
            onUserClick = { user, userId ->
                Toast.makeText(
                    this,
                    getString(R.string.user_clicked, user.nombre),
                    Toast.LENGTH_SHORT
                ).show()
                
                // CRASHLYTICS: Registrar interacción del usuario
                crashlytics.log("Usuario filtrado seleccionado: $userId - ${user.nombre}")
            }
        )

        // ========================================================================
        // RECYCLERVIEW: Configuración completa
        // ========================================================================
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FilterByAgeActivity)
            adapter = userAdapter
        }
    }

    /**
     * Configura el botón de filtrado
     * Valida la entrada y carga usuarios filtrados
     */
    private fun setupFilterButton() {
        // LAMBDA: Click en botón de filtrar
        binding.btnFilter.setOnClickListener {
            // ====================================================================
            // VALIDATION: Validar entrada del usuario
            // ====================================================================
            val minAgeText = binding.etMinAge.text.toString().trim()
            
            if (minAgeText.isEmpty()) {
                // EDITTEXT: Mostrar error en el campo
                binding.etMinAge.error = getString(R.string.error_min_age_required)
                return@setOnClickListener
            }
            
            // Convertir texto a número
            val minAge = minAgeText.toIntOrNull()
            
            if (minAge == null || minAge < 0) {
                binding.etMinAge.error = getString(R.string.error_min_age_invalid)
                return@setOnClickListener
            }
            
            // CRASHLYTICS: Log del filtro aplicado
            crashlytics.log("Filtering users with age >= $minAge")
            
            // Cargar y filtrar usuarios
            loadAndFilterUsers(minAge)
        }
    }

    /**
     * Carga usuarios desde Firestore y los filtra por edad mínima
     * 
     * FLUJO:
     * ======
     * 1. Mostrar loading (ProgressBar visible)
     * 2. Llamar a Firestore con coroutine
     * 3. Recibir lista de Maps (documentos)
     * 4. Transformar Maps a objetos User
     * 5. Filtrar por edad mínima con filter()
     * 6. Enviar lista filtrada al adapter
     * 7. Ocultar loading, mostrar datos
     */
    private fun loadAndFilterUsers(minAge: Int) {
        showLoading(true)
        hideInstruction()

        // ========================================================================
        // COROUTINE: Operación asíncrona para no bloquear UI
        // ========================================================================
        lifecycleScope.launch {
            try {
                // ================================================================
                // FIRESTORE: getCollection() - Leer todos los documentos
                // ================================================================
                firestore.getCollection("usuarios") { userList ->
                    // COROUTINE: Procesar resultado en coroutine
                    lifecycleScope.launch {
                        if (userList.isEmpty()) {
                            showEmpty(true)
                        } else {
                            // ========================================================
                            // LISTOF: Transformación y filtrado de datos
                            // ========================================================
                            // mapNotNull(): transforma y filtra nulls
                            // filter(): filtra por condición (edad >= minAge)
                            val allUsers = userList.mapNotNull { map ->
                                try {
                                    // Extraer datos del Map
                                    val nombre = map["nombre"] as? String ?: ""
                                    val edad = (map["edad"] as? Long)?.toInt() ?: 0
                                    val id = map["id"] as? String ?: "user_${System.currentTimeMillis()}"
                                    
                                    // LISTOF: Crear Pair (tupla de 2 elementos)
                                    Pair(id, User(nombre, edad))
                                } catch (e: Exception) {
                                    // CRASHLYTICS: Reportar error de parseo
                                    crashlytics.recordException(e)
                                    null // mapNotNull excluirá este elemento
                                }
                            }
                            
                            // ========================================================
                            // FILTER: Filtrar usuarios por edad mínima
                            // ========================================================
                            // filter() acepta una lambda que retorna Boolean
                            // Se ejecuta para cada elemento
                            // Solo mantiene elementos donde la lambda retorna true
                            val filteredUsers = allUsers.filter { (_, user) ->
                                // Destructuring: (id, user) descompone el Pair
                                // user.edad >= minAge: condición de filtrado
                                user.edad >= minAge
                            }

                            if (filteredUsers.isEmpty()) {
                                showEmpty(true)
                                Toast.makeText(
                                    this@FilterByAgeActivity,
                                    getString(R.string.filter_no_results, minAge),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                showEmpty(false)
                                
                                // RECYCLERVIEW: Enviar lista filtrada al adapter
                                userAdapter.submitList(filteredUsers)
                                
                                // Mostrar RecyclerView
                                binding.recyclerView.visibility = View.VISIBLE
                                
                                // CRASHLYTICS: Log de resultados
                                crashlytics.log("Usuarios filtrados: ${filteredUsers.size} de ${allUsers.size}")
                                
                                Toast.makeText(
                                    this@FilterByAgeActivity,
                                    getString(R.string.filter_results, filteredUsers.size, minAge),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        showLoading(false)
                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch {
                    showLoading(false)
                    Toast.makeText(
                        this@FilterByAgeActivity,
                        "Error al cargar usuarios: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // CRASHLYTICS: Reportar excepción
                    crashlytics.recordException(e)
                }
            }
        }
    }

    /**
     * Muestra/oculta el ProgressBar de carga
     */
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnFilter.isEnabled = !show
    }

    /**
     * Muestra/oculta mensaje de lista vacía
     */
    private fun showEmpty(show: Boolean) {
        binding.tvEmpty.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    /**
     * Oculta el mensaje de instrucciones inicial
     */
    private fun hideInstruction() {
        binding.tvInstruction.visibility = View.GONE
    }
}
