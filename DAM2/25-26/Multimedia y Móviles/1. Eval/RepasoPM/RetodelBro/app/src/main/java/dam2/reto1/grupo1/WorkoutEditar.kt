package dam2.reto1.grupo1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WorkoutEditar : BaseActivity() {

    private lateinit var etNombreWorkout: EditText
    private lateinit var etVideo: EditText
    private lateinit var etNivel: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: Button

    private lateinit var workoutId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_formulario)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.editar_workout)

        // Configurar botón de volver personalizado
        val btnVolverToolbar: ImageButton = toolbar.findViewById(R.id.btn_volver_toolbar)
        btnVolverToolbar.visibility = View.VISIBLE
        btnVolverToolbar.setOnClickListener {
            finish()
        }

        etNombreWorkout = findViewById(R.id.etNombreWorkout)
        etVideo = findViewById(R.id.etVideo)
        etNivel = findViewById(R.id.etNivel)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnVolver = findViewById(R.id.btnVolver)

        workoutId = intent.getStringExtra("workoutId") ?: ""

        if (workoutId.isNotEmpty()) {
            cargarDatosWorkout()
        }

        btnGuardar.setOnClickListener {
            guardarCambios()
        }

        btnVolver.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarDatosWorkout() {
        Workout.cargarWorkoutPorId(
            workoutId,
            exito = { workout ->
                etNombreWorkout.setText(workout.nombre)
                etVideo.setText(workout.video)
                etNivel.setText(workout.nivel.toString())
                etDescripcion.setText(workout.descripcion)
            },
            fallo = { e ->
                Toast.makeText(this, getString(R.string.error_cargar_workout, e.message), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun guardarCambios() {
        val nombre = etNombreWorkout.text.toString()
        val video = etVideo.text.toString()
        val nivel = etNivel.text.toString().toIntOrNull() ?: 0
        val descripcion = etDescripcion.text.toString()

        if (nombre.isNotBlank() && video.isNotBlank() && descripcion.isNotBlank()) {
            Workout.actualizarWorkout(
                workoutId,
                nombre,
                video,
                nivel,
                descripcion,
                exito = {
                    Toast.makeText(this, getString(R.string.workout_actualizado), Toast.LENGTH_SHORT).show()
                    val user = GymDaji.usuario
                    if (user != null) {
                        GymDaji.conseguirdatos(
                            activity = this,
                            usuario = user,
                            exito = {
                                finish()
                            }
                        )
                    } else {
                        finish()
                    }
                },
                fallo = { e ->
                    Toast.makeText(this, getString(R.string.error_actualizar, e.message), Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(this, getString(R.string.completar_todos_campos), Toast.LENGTH_SHORT).show()
        }
    }
}