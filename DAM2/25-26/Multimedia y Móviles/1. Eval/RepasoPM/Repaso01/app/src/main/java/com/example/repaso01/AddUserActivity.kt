package com.example.repaso01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.repaso01.databinding.ActivityAddUserBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.launch
/**
 * AddUserActivity - Pantalla para añadir nuevos usuarios
 * 
 * CONCEPTOS DEMOSTRADOS:
 * ======================
 * 1. BINDING: ViewBinding para acceso type-safe a vistas
 * 2. FIRESTORE: Guardar documentos con ID autogenerado
 * 3. CRASHLYTICS: Logging de eventos y errores
 * 4. DATASTORE: Almacenar preferencias asíncrono
 * 5. COROUTINE: Operaciones asíncronas con lifecycleScope
 * 6. INTENT + DATA: Devolver datos a la Activity anterior
 * 7. LAMBDA: Click listeners con expresiones lambda
 * 8. Validación de formularios
 * 9. Manejo de estados de carga (loading)
 */
class AddUserActivity : AppCompatActivity() {

    // ============================================================================
    // BINDING: ViewBinding - Acceso type-safe a las vistas
    // ============================================================================
    // ActivityAddUserBinding se genera automáticamente desde activity_add_user.xml
    // Proporciona acceso directo a: btnSave, btnCancel, etNombre, etEdad, etc.
    private lateinit var binding: ActivityAddUserBinding
    
    // ============================================================================
    // FIRESTORE: FirestoreManager - Operaciones de base de datos
    // ============================================================================
    // Encapsula la lógica de Firestore:
    //   - addDocument(): Añade documento con ID autogenerado por Firestore
    //   - saveDocument(): Guarda con ID específico
    //   - getDocument/getCollection(): Lectura de datos
    private val firestore = FirestoreManager()
    
    // ============================================================================
    // CRASHLYTICS: Monitoreo de errores y eventos
    // ============================================================================
    // getInstance() retorna singleton que:
    //   - Captura crashes automáticamente
    //   - Permite logging manual con log()
    //   - Reporta excepciones con recordException()
    private val crashlytics = FirebaseCrashlytics.getInstance()
    
    // ============================================================================
    // DATASTORE: PreferencesManager - Almacenamiento de preferencias
    // ============================================================================
    // Alternativa moderna a SharedPreferences con:
    //   - API asíncrona (no bloquea UI thread)
    //   - Type-safety con PreferencesKey
    //   - Soporte para Flow (reactive streams)
    private lateinit var preferencesManager: PreferencesManager

    companion object {
        // ========================================================================
        // DATA: Constantes para pasar datos mediante Intent extras
        // ========================================================================
        // Buenas prácticas:
        //   - Usar constantes en companion object
        //   - Prefijo descriptivo (EXTRA_)
        //   - Nombres claros y específicos
        // Uso: intent.putExtra(EXTRA_USER_NAME, "Juan")
        const val EXTRA_USER_NAME = "extra_user_name"
        const val EXTRA_USER_AGE = "extra_user_age"
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // BINDING: Inflar layout con ViewBinding
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DATASTORE: Inicializar managers
        preferencesManager = PreferencesManager(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // LAMBDA: Botón para volver atrás
        binding.btnBack.setOnClickListener {
            finish() // Cierra la Activity y vuelve a la anterior
        }
        
        // LAMBDA: Expresión lambda para click listener del botón guardar
        binding.btnSave.setOnClickListener {
            saveUser()
        }

        // LAMBDA: Expresión lambda para botón cancelar
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    /**
     * Guarda un nuevo usuario en Firestore
     * 
     * FLUJO DE LA FUNCIÓN:
     * ====================
     * 1. Validar datos del formulario
     * 2. Crear objeto User (data class)
     * 3. Guardar en Firestore con ID autogenerado
     * 4. Guardar metadata en DataStore
     * 5. Registrar evento en Crashlytics
     * 6. Devolver resultado a MainActivity
     */
    private fun saveUser() {
        val nombre = binding.etNombre.text.toString().trim()
        val edadStr = binding.etEdad.text.toString().trim()

        // ========================================================================
        // Validación de formulario con feedback visual
        // ========================================================================
        // trim() elimina espacios al inicio/final
        // TextInputLayout.error muestra mensaje de error bajo el campo
        if (nombre.isEmpty()) {
            binding.tilNombre.error = getString(R.string.error_nombre_required)
            return // Salir de la función si hay error
        }

        if (edadStr.isEmpty()) {
            binding.tilEdad.error = getString(R.string.error_edad_required)
            return
        }

        // toIntOrNull() convierte String a Int, retorna null si falla
        val edad = edadStr.toIntOrNull()
        if (edad == null || edad <= 0) {
            binding.tilEdad.error = getString(R.string.error_edad_invalid)
            return
        }

        // Limpiar errores previos si la validación pasa
        binding.tilNombre.error = null
        binding.tilEdad.error = null

        // ========================================================================
        // COROUTINE: Operación asíncrona para guardar usuario
        // ========================================================================
        // lifecycleScope.launch:
        //   - Crea coroutine atada al ciclo de vida de la Activity
        //   - Se cancela automáticamente en onDestroy
        //   - Permite llamar funciones suspend
        lifecycleScope.launch {
            try {
                showLoading(true)

                // Crear data class User
                val user = User(nombre, edad)

                // ================================================================
                // FIRESTORE: addDocument() - Guardar con ID autogenerado
                // ================================================================
                // addDocument() internamente usa collection.add():
                //   1. Firestore genera un ID aleatorio único (ej: "7xK3mP9qR2aL")
                //   2. Guarda el documento en la colección
                //   3. Retorna el ID generado en el callback
                // 
                // Ventaja vs saveDocument():
                //   - No necesitas generar IDs manualmente
                //   - Firestore garantiza unicidad del ID
                //   - Más simple y menos propenso a errores
                firestore.addDocument("usuarios", user) { success, userId ->
                    if (success && userId != null) {
                        // COROUTINE + DATASTORE: Guardar datos en DataStore
                        lifecycleScope.launch {
                            // DATASTORE: Guardar último userId y actualizar contador
                            preferencesManager.saveLastUserId(userId)
                            preferencesManager.incrementUserCount()

                            // CRASHLYTICS: Registrar evento de creación de usuario
                            crashlytics.log("Usuario creado: $userId - $nombre")
                            
                            showLoading(false)
                            Toast.makeText(
                                this@AddUserActivity,
                                "Usuario guardado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            // INTENT + DATA: Devolver resultado con putExtra
                            val resultIntent = Intent().apply {
                                putExtra(EXTRA_USER_NAME, nombre)
                                putExtra(EXTRA_USER_AGE, edad)
                                putExtra(EXTRA_USER_ID, userId)
                            }
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }
                    } else {
                        showLoading(false)
                        Toast.makeText(
                            this@AddUserActivity,
                            "Error al guardar usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                        
                        // CRASHLYTICS: Reportar error a Crashlytics
                        crashlytics.recordException(Exception("Error al guardar usuario: $userId"))
                    }
                }

            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(this@AddUserActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                
                // CRASHLYTICS: Reportar excepción
                crashlytics.recordException(e)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSave.isEnabled = !show
        binding.btnCancel.isEnabled = !show
    }
}
