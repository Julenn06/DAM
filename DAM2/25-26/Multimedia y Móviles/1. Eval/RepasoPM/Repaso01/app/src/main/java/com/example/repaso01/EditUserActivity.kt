package com.example.repaso01

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repaso01.databinding.ActivityEditUserBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.launch

/**
 * EditUserActivity - Pantalla para editar/eliminar usuarios existentes
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. BINDING: ViewBinding para acceso a vistas
 * 2. RECYCLERVIEW: Lista de usuarios para seleccionar
 * 3. FIRESTORE: Leer, actualizar y eliminar documentos
 * 4. COROUTINE: Operaciones asíncronas
 * 5. CRASHLYTICS: Logging de operaciones
 * 6. LAMBDA: Click listeners y AlertDialog
 * 7. LISTOF: Transformación de datos con mapNotNull
 * 8. Validación y manejo de estados de UI
 * 9. AlertDialog para confirmación de eliminación
 * 
 * FLUJO DE LA PANTALLA:
 * =====================
 * 1. Mostrar lista de todos los usuarios (RecyclerView)
 * 2. Usuario hace click en un usuario de la lista
 * 3. Se oculta lista y muestra formulario con datos cargados
 * 4. Usuario puede editar campos y actualizar o eliminar
 * 5. Confirmación antes de eliminar
 * 6. Volver a la lista después de actualizar/eliminar
 */
class EditUserActivity : AppCompatActivity() {

    // ============================================================================
    // BINDING: ViewBinding para esta Activity
    // ============================================================================
    private lateinit var binding: ActivityEditUserBinding
    
    // ============================================================================
    // FIRESTORE: Manager para operaciones CRUD
    // ============================================================================
    private val firestore = FirestoreManager()
    
    // ============================================================================
    // CRASHLYTICS: Para logging y monitoreo
    // ============================================================================
    private val crashlytics = FirebaseCrashlytics.getInstance()
    
    // ============================================================================
    // DATASTORE: FavoritesManager - Gestión de favoritos
    // ============================================================================
    private lateinit var favoritesManager: FavoritesManager
    
    // Variable para almacenar IDs favoritos
    private var favoriteIds: Set<String> = emptySet()
    
    // ============================================================================
    // RECYCLERVIEW: Adapter para mostrar lista de usuarios
    // ============================================================================
    private lateinit var userAdapter: UserAdapter
    
    // Variable para guardar el ID del usuario actualmente seleccionado
    private var currentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // BINDING: Inflar layout
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DATASTORE: Inicializar managers
        favoritesManager = FavoritesManager(this)

        setupBackButton()
        setupRecyclerView()
        setupFormButtons()
        observeFavorites()
        loadUsers()
    }

    /**
     * Configura el botón de retroceso con lógica condicional
     * Si el formulario está visible, vuelve a la lista
     * Si la lista está visible, cierra la Activity
     */
    private fun setupBackButton() {
        // LAMBDA: Botón para volver atrás con lógica condicional
        binding.btnBack.setOnClickListener {
            if (binding.scrollViewForm.visibility == View.VISIBLE) {
                // Si el formulario está visible, volver a mostrar la lista
                showUserList()
                currentUserId = null
            } else {
                // Si estamos en la lista, cerrar la Activity
                finish()
            }
        }
    }
    
    /**
     * Configura el RecyclerView con adapter y click listener
     * RECYCLERVIEW: Muestra la lista de usuarios para seleccionar
     */
    private fun setupRecyclerView() {
        // ========================================================================
        // RECYCLERVIEW + LAMBDA: Adapter con click listener
        // ========================================================================
        // Al hacer click en un usuario de la lista:
        // 1. Guarda el ID del usuario seleccionado
        // 2. Carga los datos en el formulario
        // 3. Oculta la lista y muestra el formulario
        userAdapter = UserAdapter(
            // LAMBDA 1: Click en usuario
            onUserClick = { user, userId ->
                // LAMBDA: Callback cuando se hace click en un usuario
                currentUserId = userId
                loadUserIntoForm(user, userId)
                showUserForm()
                
                // CRASHLYTICS: Log de selección
                crashlytics.log("Usuario seleccionado para editar: $userId - ${user.nombre}")
            },
            // LAMBDA 2: Click en botón de favorito
            onFavoriteClick = { userId ->
                lifecycleScope.launch {
                    favoritesManager.toggleFavorite(userId)
                    
                    val isFav = favoritesManager.isFavorite(userId)
                    Toast.makeText(
                        this@EditUserActivity,
                        if (isFav) getString(R.string.added_to_favorites)
                        else getString(R.string.removed_from_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            // LAMBDA 3: Verificar si es favorito
            isFavorite = { userId ->
                userId in favoriteIds
            }
        )
        
        // Configurar RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@EditUserActivity)
            adapter = userAdapter
        }
    }
    
    /**
     * Configura los botones del formulario de edición
     * LAMBDA: Click listeners para actualizar y eliminar
     */
    private fun setupFormButtons() {
        // ========================================================================
        // LAMBDA: Botón para actualizar usuario
        // ========================================================================
        binding.btnUpdate.setOnClickListener {
            updateUser()
        }
        
        // ========================================================================
        // LAMBDA: Botón para eliminar usuario con confirmación
        // ========================================================================
        binding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    /**
     * FLOW + COROUTINE: Observar cambios en favoritos
     * ===============================================
     * Actualiza la lista de favoritos y redibuja el adapter
     */
    private fun observeFavorites() {
        lifecycleScope.launch {
            favoritesManager.favoritesFlow.collect { favorites ->
                favoriteIds = favorites
                // Notificar al adapter para redibujar iconos de favoritos
                userAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * Carga todos los usuarios desde Firestore
     * FIRESTORE + COROUTINE: Operación asíncrona para leer colección
     */
    private fun loadUsers() {
        showLoading(true)

        // COROUTINE: Operación asíncrona
        lifecycleScope.launch {
            try {
                // FIRESTORE: Obtener todos los usuarios
                firestore.getCollection("usuarios") { userList ->
                    lifecycleScope.launch {
                        showLoading(false)
                        
                        if (userList.isEmpty()) {
                            // No hay usuarios
                            binding.tvEmpty.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            // Hay usuarios - mostrar lista
                            binding.tvEmpty.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            
                            // ========================================================
                            // LISTOF: Transformación de Map a objetos User
                            // ========================================================
                            // mapNotNull filtra nulls automáticamente
                            val users = userList.mapIndexedNotNull { index, map ->
                                try {
                                    val nombre = map["nombre"] as? String ?: return@mapIndexedNotNull null
                                    val edad = (map["edad"] as? Long)?.toInt() ?: return@mapIndexedNotNull null
                                    
                                    // Obtener ID del documento
                                    // Los IDs están en el orden de la consulta
                                    val userId = map["__id__"] as? String ?: "user_$index"
                                    
                                    Pair(userId, User(nombre, edad))
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            
                            // Actualizar adapter con la lista
                            userAdapter.submitList(users)
                            
                            // CRASHLYTICS: Log de carga exitosa
                            crashlytics.log("${users.size} usuarios cargados para edición")
                        }
                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch {
                    showLoading(false)
                    Toast.makeText(
                        this@EditUserActivity,
                        getString(R.string.error_generic, e.message),
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // CRASHLYTICS: Reportar error
                    crashlytics.recordException(e)
                }
            }
        }
    }

    /**
     * Carga los datos del usuario seleccionado en el formulario
     * DATA: Muestra información del usuario en los campos
     */
    private fun loadUserIntoForm(user: User, userId: String) {
        // Mostrar ID del usuario seleccionado
        binding.tvSelectedUserId.text = "ID: $userId"
        
        // Cargar datos en los campos
        binding.etNombre.setText(user.nombre)
        binding.etEdad.setText(user.edad.toString())
    }

    /**
     * Muestra el formulario de edición y oculta la lista
     */
    private fun showUserForm() {
        binding.recyclerView.visibility = View.GONE
        binding.tvEmpty.visibility = View.GONE
        binding.scrollViewForm.visibility = View.VISIBLE
    }

    /**
     * Muestra la lista de usuarios y oculta el formulario
     */
    private fun showUserList() {
        binding.scrollViewForm.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        
        // Limpiar formulario
        clearForm()
        
        // Recargar lista por si hubo cambios
        loadUsers()
    }

    /**
     * Actualiza el usuario en Firestore
     * FIRESTORE OPERATION: saveDocument() - Update
     */
    private fun updateUser() {
        val nombre = binding.etNombre.text.toString().trim()
        val edadStr = binding.etEdad.text.toString().trim()
        
        // Validación de campos
        if (nombre.isEmpty()) {
            binding.tilNombre.error = getString(R.string.error_nombre_required)
            return
        }
        
        if (edadStr.isEmpty()) {
            binding.tilEdad.error = getString(R.string.error_edad_required)
            return
        }
        
        val edad = edadStr.toIntOrNull()
        if (edad == null || edad <= 0) {
            binding.tilEdad.error = getString(R.string.error_edad_invalid)
            return
        }
        
        // Limpiar errores
        binding.tilNombre.error = null
        binding.tilEdad.error = null
        
        // COROUTINE: Actualizar en Firestore
        lifecycleScope.launch {
            try {
                showLoading(true)
                
                val user = User(nombre, edad)
                
                // FIRESTORE: saveDocument actualiza el documento
                currentUserId?.let { userId ->
                    firestore.saveDocument("usuarios", userId, user) { success ->
                        lifecycleScope.launch {
                            showLoading(false)
                            
                            if (success) {
                                Toast.makeText(
                                    this@EditUserActivity,
                                    getString(R.string.user_updated_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                
                                // CRASHLYTICS: Log de actualización
                                crashlytics.log("Usuario actualizado: $userId")
                                
                                // Volver a la lista
                                showUserList()
                                currentUserId = null
                            } else {
                                Toast.makeText(
                                    this@EditUserActivity,
                                    getString(R.string.user_updated_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                
                                // CRASHLYTICS: Reportar error
                                crashlytics.recordException(Exception("Error al actualizar usuario: $userId"))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(
                    this@EditUserActivity,
                    getString(R.string.error_generic, e.message),
                    Toast.LENGTH_SHORT
                ).show()
                
                // CRASHLYTICS: Reportar excepción
                crashlytics.recordException(e)
            }
        }
    }

    /**
     * Muestra diálogo de confirmación antes de eliminar
     * ALERTDIALOG + LAMBDA: Diálogo con botones y callbacks
     */
    private fun showDeleteConfirmationDialog() {
        // LAMBDA: AlertDialog con lambdas para botones
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_delete_title))
            .setMessage(getString(R.string.dialog_delete_message))
            .setPositiveButton(getString(R.string.dialog_btn_delete)) { dialog, _ ->
                // LAMBDA: Ejecutada al confirmar eliminación
                deleteUser()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                // LAMBDA: Ejecutada al cancelar
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Elimina el usuario de Firestore
     * FIRESTORE OPERATION: deleteDocument() - Delete
     */
    private fun deleteUser() {
        val userName = binding.etNombre.text.toString() // Guardar nombre antes de eliminar
        
        showLoading(true)
        
        // COROUTINE: Eliminar en Firestore
        lifecycleScope.launch {
            try {
                currentUserId?.let { userId ->
                    // FIRESTORE: Eliminar documento
                    firestore.deleteDocument("usuarios", userId) { success ->
                        lifecycleScope.launch {
                            showLoading(false)
                            
                            if (success) {
                                Toast.makeText(
                                    this@EditUserActivity,
                                    getString(R.string.user_deleted_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                
                                // CRASHLYTICS: Log de eliminación
                                crashlytics.log("Usuario eliminado: $userId")
                                
                                // Volver a la lista
                                showUserList()
                                currentUserId = null
                            } else {
                                Toast.makeText(
                                    this@EditUserActivity,
                                    getString(R.string.user_deleted_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                
                                // CRASHLYTICS: Reportar error
                                crashlytics.recordException(Exception("Error al eliminar usuario: $userId"))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(
                    this@EditUserActivity,
                    getString(R.string.error_generic, e.message),
                    Toast.LENGTH_SHORT
                ).show()
                
                // CRASHLYTICS: Reportar excepción
                crashlytics.recordException(e)
            }
        }
    }

    /**
     * Limpia todos los campos del formulario
     */
    private fun clearForm() {
        binding.etNombre.text?.clear()
        binding.etEdad.text?.clear()
        binding.tvSelectedUserId.text = "ID: "
    }

    /**
     * Muestra/oculta el ProgressBar de carga
     */
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        
        // Deshabilitar interacción durante carga
        if (binding.scrollViewForm.visibility == View.VISIBLE) {
            binding.btnUpdate.isEnabled = !show
            binding.btnDelete.isEnabled = !show
        }
    }
}
