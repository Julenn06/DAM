package dam2.reto1.grupo1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WorkoutListaEjercicios : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterEjercicios
    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarLevel: TextView
    private var tvEmptyMessage: TextView? = null
    private lateinit var workoutId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        try {
            setContentView(R.layout.activity_ejercicios)
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            toolbarTitle = findViewById(R.id.toolbar_title)
            toolbarLevel = findViewById(R.id.toolbar_level)
            tvEmptyMessage = findViewById(R.id.tvEmptyMessage)

            // Mostrar "Ejercicios de" en toolbar_level
            toolbarLevel.text = getString(R.string.ejercicios_de)
            toolbarLevel.visibility = View.VISIBLE

            // Configurar botón de volver personalizado
            val btnVolverToolbar: ImageButton? = toolbar.findViewById(R.id.btn_volver_toolbar)
            btnVolverToolbar?.visibility = View.VISIBLE
            btnVolverToolbar?.setOnClickListener {
                finish()
            }

            recyclerView = findViewById(R.id.recyclerViewExercises)
            recyclerView.layoutManager = LinearLayoutManager(this)

            workoutId = intent.getStringExtra("WORKOUT_ID") ?: ""
            Log.d("WorkoutListaEjercicios", "WORKOUT_ID recibido: $workoutId")

            if (workoutId.isNotEmpty()) {
                cargarWorkout()
            } else {
                Toast.makeText(this, getString(R.string.error_id_workout), Toast.LENGTH_SHORT).show()
                finish()
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        } catch (e: Exception) {
            Log.e("WorkoutListaEjercicios", "Error en onCreate", e)
            Toast.makeText(this, getString(R.string.error_inicializar, e.message), Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun cargarWorkout() {
        try {
            Workout.cargarWorkoutPorId(workoutId, { workout ->
                toolbarTitle.text = workout.nombre
                cargarEjercicios(workout)
            }, { error ->
                Log.e("WorkoutListaEjercicios", "Error al cargar workout", error)
                Toast.makeText(this, getString(R.string.error_cargar_workout, error.message), Toast.LENGTH_SHORT).show()
                toolbarTitle.text = getString(R.string.ejercicios)
                tvEmptyMessage?.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            })
        } catch (e: Exception) {
            Log.e("WorkoutListaEjercicios", "Error en cargarWorkout", e)
            Toast.makeText(this, getString(R.string.error_mensaje, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarEjercicios(workout: Workout) {
        try {
            Ejercicio.getEjerciciosWorkout(workout) { ejercicios ->
                if (ejercicios.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    tvEmptyMessage?.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    tvEmptyMessage?.visibility = View.GONE
                    adapter = AdapterEjercicios(ejercicios)
                    recyclerView.adapter = adapter
                }
            }
        } catch (e: Exception) {
            Log.e("WorkoutListaEjercicios", "Error en cargarEjercicios", e)
            Toast.makeText(this, getString(R.string.error_cargar_ejercicios, e.message), Toast.LENGTH_SHORT).show()
        }
    }
}
