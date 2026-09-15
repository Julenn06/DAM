package com.example.repaso01

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repaso01.databinding.ActivityUserListBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.launch

/**
 * UserListActivity - Pantalla que muestra lista de usuarios
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. RECYCLERVIEW: Lista eficiente con adapter pattern
 * 2. LAMBDA: Callback para clicks en items
 * 3. BINDING: ViewBinding para acceso a vistas
 * 4. FIRESTORE: Lectura de colección completa
 * 5. COROUTINE: Operaciones asíncronas
 * 6. LISTOF: Transformación de datos con mapNotNull
 * 7. CRASHLYTICS: Logging de eventos y errores
 * 8. Manejo de estados: loading, empty, error, success
 */
class UserListActivity : AppCompatActivity() {

    // ============================================================================
    // BINDING: ViewBinding para esta Activity
    // ============================================================================
    // Generada desde activity_user_list.xml
    // Proporciona: recyclerView, progressBar, tvEmpty, btnBack
    private lateinit var binding: ActivityUserListBinding
    
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
    // UserAdapter extiende RecyclerView.Adapter:
    //   - Gestiona la lista de datos
    //   - Crea y reutiliza ViewHolders
    //   - Bindea datos a las vistas
    //   - Maneja clicks mediante lambda
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // BINDING: Inflar layout con ViewBinding
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButton()
        setupRecyclerView()
        loadUsers()
    }

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
     * 4. ItemDecoration: Añade separadores (opcional)
     * 5. ItemAnimator: Anima cambios en la lista (opcional)
     */
    private fun setupRecyclerView() {
        // ========================================================================
        // RECYCLERVIEW + LAMBDA: Crear adapter con callback lambda
        // ========================================================================
        // UserAdapter constructor:
        //   Parámetro: (User, String) -> Unit
        //   - User: objeto clickeado
        //   - String: ID del documento
        //   - Unit: no retorna valor
        // 
        // Esta lambda se ejecuta cuando el usuario hace click en un item
        // Es una función de orden superior (higher-order function):
        //   - Acepta funciones como parámetros
        //   - Permite comportamiento customizable
        userAdapter = UserAdapter(
            // LAMBDA: Click en usuario
            onUserClick = { user, userId ->
                // LAMBDA: Cuerpo de la función lambda
                // Se ejecuta al hacer click en un item del RecyclerView
                
                Toast.makeText(
                    this,
                    getString(R.string.user_clicked, user.nombre),
                    Toast.LENGTH_SHORT
                ).show()
                
                // CRASHLYTICS: Registrar interacción del usuario
                crashlytics.log("Usuario seleccionado: $userId - ${user.nombre}")
            }
        )

        // ========================================================================
        // RECYCLERVIEW: Configuración completa
        // ========================================================================
        // apply {} permite configurar múltiples propiedades sin repetir el objeto
        binding.recyclerView.apply {
            // LinearLayoutManager: items en lista vertical
            // Alternativas:
            //   - GridLayoutManager: cuadrícula
            //   - StaggeredGridLayoutManager: cuadrícula irregular
            layoutManager = LinearLayoutManager(this@UserListActivity)
            
            // Asignar adapter que gestiona los datos
            adapter = userAdapter
        }
    }

    /**
     * Carga usuarios desde Firestore
     * 
     * FLUJO:
     * ======
     * 1. Mostrar loading (ProgressBar visible)
     * 2. Llamar a Firestore con coroutine
     * 3. Recibir lista de Maps (documentos)
     * 4. Transformar Maps a objetos User
     * 5. Enviar lista al adapter
     * 6. Ocultar loading, mostrar datos
     */
    private fun loadUsers() {
        showLoading(true)

        // ========================================================================
        // COROUTINE: Operación asíncrona para no bloquear UI
        // ========================================================================
        lifecycleScope.launch {
            try {
                // ================================================================
                // FIRESTORE: getCollection() - Leer todos los documentos
                // ================================================================
                // getCollection():
                //   1. Hace query a Firestore: db.collection("usuarios").get()
                //   2. Espera respuesta de la red
                //   3. Convierte documentos a List<Map<String, Any>>
                //   4. Ejecuta callback con la lista
                firestore.getCollection("usuarios") { userList ->
                    // COROUTINE: Procesar resultado en coroutine
                    lifecycleScope.launch {
                        if (userList.isEmpty()) {
                            showEmpty(true)
                        } else {
                            showEmpty(false)
                            
                            // ========================================================
                            // LISTOF: Transformación de datos con mapNotNull
                            // ========================================================
                            // mapNotNull():
                            //   - Aplica transformación a cada elemento
                            //   - Filtra elementos null automáticamente
                            //   - Retorna List<Pair<String, User>>
                            // 
                            // Alternativas:
                            //   - map(): incluye nulls
                            //   - filter(): solo filtra
                            //   - flatMap(): aplana listas anidadas
                            val users = userList.mapNotNull { map ->
                                try {
                                    // Extraer datos del Map
                                    // as? = safe cast, retorna null si falla
                                    val nombre = map["nombre"] as? String ?: ""
                                    val edad = (map["edad"] as? Long)?.toInt() ?: 0
                                    val id = map["id"] as? String ?: "user_${System.currentTimeMillis()}"
                                    
                                    // LISTOF: Crear Pair (tupla de 2 elementos)
                                    // Pair se usa para combinar dos valores relacionados
                                    Pair(id, User(nombre, edad))
                                } catch (e: Exception) {
                                    // CRASHLYTICS: Reportar error de parseo
                                    // Si un documento tiene formato incorrecto, se ignora
                                    crashlytics.recordException(e)
                                    null // mapNotNull excluirá este elemento
                                }
                            }

                            // RECYCLERVIEW: Enviar lista al adapter
                            // submitList actualiza los datos y notifica cambios
                            userAdapter.submitList(users)
                            
                            // CRASHLYTICS: Log informativo
                            crashlytics.log("Usuarios cargados: ${users.size}")
                        }
                        showLoading(false)
                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch {
                    showLoading(false)
                    Toast.makeText(
                        this@UserListActivity,
                        "Error al cargar usuarios: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // CRASHLYTICS: Reportar excepción
                    crashlytics.recordException(e)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmpty(show: Boolean) {
        binding.tvEmpty.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}
