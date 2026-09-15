package com.example.repaso01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repaso01.databinding.ActivityFavoritesBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * FavoritesActivity - Activity para mostrar usuarios favoritos
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. RECYCLERVIEW: Muestra lista de usuarios favoritos
 * 2. FIRESTORE: Lee usuarios desde Firestore
 * 3. DATASTORE: Lee IDs favoritos con FavoritesManager
 * 4. COROUTINE: Operaciones asíncronas con lifecycleScope
 * 5. FLOW: Observación reactiva de favoritos
 * 6. BINDING: ViewBinding para acceso a vistas
 * 7. LAMBDA: Callbacks para clicks y favoritos
 * 8. LISTOF: Filtra lista de usuarios por favoritos
 * 9. INTENT: Compartir usuarios favoritos con ACTION_SEND
 * 10. DATA: Pasa datos de usuarios en Intents
 * 11. CONSTRAINTLAYOUT: Layout con RecyclerView
 * 12. CRASHLYTICS: Log de eventos
 */
class FavoritesActivity : AppCompatActivity() {

    // ============================================================================
    // BINDING: ViewBinding para acceso type-safe a vistas
    // ============================================================================
    private lateinit var binding: ActivityFavoritesBinding
    
    // ============================================================================
    // DATASTORE: Manager para leer favoritos
    // ============================================================================
    private lateinit var favoritesManager: FavoritesManager
    
    // ============================================================================
    // FIRESTORE: Manager para leer usuarios
    // ============================================================================
    private lateinit var firestoreManager: FirestoreManager
    
    // ============================================================================
    // RECYCLERVIEW: Adapter para mostrar usuarios
    // ============================================================================
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // BINDING: Inflar layout con ViewBinding
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // ============================================================================
        // CRASHLYTICS: Registrar evento de visualización de favoritos
        // ============================================================================
        FirebaseCrashlytics.getInstance().log("User opened Favorites screen")
        
        // ============================================================================
        // DATASTORE + FIRESTORE: Inicializar managers
        // ============================================================================
        favoritesManager = FavoritesManager(this)
        firestoreManager = FirestoreManager()
        
        // ============================================================================
        // LAMBDA: Botón de retroceso
        // ============================================================================
        setupBackButton()
        
        // ============================================================================
        // RECYCLERVIEW: Configurar RecyclerView con lambdas
        // ============================================================================
        setupRecyclerView()
        
        // ============================================================================
        // FLOW + COROUTINE: Observar cambios en favoritos
        // ============================================================================
        observeFavorites()
    }

    /**
     * LAMBDA: Configurar botón de retroceso
     */
    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish() // Cierra la Activity y vuelve a MainActivity
        }
    }

    /**
     * RECYCLERVIEW + LAMBDA: Configurar RecyclerView con callbacks
     * ===========================================================
     * - onUserClick: Click normal en usuario (compartir)
     * - onFavoriteClick: Click en botón de favorito (toggle)
     * - isFavorite: Verificar si usuario es favorito
     */
    private fun setupRecyclerView() {
        // LAMBDA: Crear adapter con 3 callbacks
        adapter = UserAdapter(
            // LAMBDA 1: Click en usuario - compartir información
            onUserClick = { user, userId ->
                shareUser(user, userId)
            },
            // LAMBDA 2: Click en botón de favorito - quitar de favoritos
            onFavoriteClick = { userId ->
                toggleFavorite(userId)
            },
            // LAMBDA 3: Verificar si es favorito (siempre true aquí)
            isFavorite = { true } // Todos los mostrados son favoritos
        )
        
        binding.rvFavorites.adapter = adapter
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
    }

    /**
     * FLOW + COROUTINE: Observar Flow de favoritos y cargar usuarios
     * ==============================================================
     * Combina Flow de favoritos con datos de Firestore
     */
    private fun observeFavorites() {
        // COROUTINE: Lanzar coroutine en el scope del Activity
        lifecycleScope.launch {
            // FLOW: Colectar (escuchar) cambios en favoritos
            favoritesManager.favoritesFlow.collect { favoriteIds ->
                // LISTOF: Set de IDs favoritos
                loadFavoriteUsers(favoriteIds)
            }
        }
    }

    /**
     * FIRESTORE + LISTOF: Cargar usuarios favoritos desde Firestore
     * =============================================================
     * Filtra usuarios por IDs favoritos
     */
    private fun loadFavoriteUsers(favoriteIds: Set<String>) {
        if (favoriteIds.isEmpty()) {
            // Mostrar estado vacío
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
            return
        }
        
        // FIRESTORE: Obtener todos los usuarios con callback
        firestoreManager.getCollection("usuarios") { allUsers ->
            // LISTOF: Filtrar usuarios por IDs favoritos
            val favoriteUsers = allUsers
                .filter { userMap ->
                    // Obtener ID del documento desde el campo especial
                    val userId = userMap["__id__"] as? String ?: ""
                    userId in favoriteIds
                }
                .mapNotNull { userMap ->
                    val userId = userMap["__id__"] as? String ?: return@mapNotNull null
                    val nombre = userMap["nombre"] as? String ?: return@mapNotNull null
                    val edad = (userMap["edad"] as? Long)?.toInt() ?: return@mapNotNull null
                    Pair(userId, User(nombre, edad))
                }
            
            updateUI(favoriteUsers)
        }
    }

    /**
     * LISTOF: Actualizar UI con lista de usuarios favoritos
     */
    private fun updateUI(users: List<Pair<String, User>>) {
        if (users.isEmpty()) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
            
            // RECYCLERVIEW: Actualizar adapter
            adapter.submitList(users)
        }
    }

    /**
     * DATASTORE + COROUTINE: Toggle favorito
     * ======================================
     * Quita usuario de favoritos
     */
    private fun toggleFavorite(userId: String) {
        lifecycleScope.launch {
            // DATASTORE: Quitar de favoritos
            favoritesManager.removeFromFavorites(userId)
            
            Toast.makeText(
                this@FavoritesActivity,
                getString(R.string.removed_from_favorites),
                Toast.LENGTH_SHORT
            ).show()
            
            // CRASHLYTICS: Log de acción
            FirebaseCrashlytics.getInstance().log("User removed from favorites: $userId")
        }
    }

    /**
     * INTENT: Compartir información de usuario
     * ========================================
     * Usa Intent.ACTION_SEND para compartir texto
     */
    private fun shareUser(user: User, userId: String) {
        // DATA: Formatear información del usuario para compartir
        val shareText = getString(
            R.string.share_user_text,
            user.nombre,
            user.edad,
            userId
        )
        
        // INTENT: Crear Intent implícito para compartir
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_user_title))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        
        // INTENT: Lanzar chooser para que usuario elija app
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_user_title)))
        
        // CRASHLYTICS: Log de compartir
        FirebaseCrashlytics.getInstance().log("User shared: $userId")
    }
}
