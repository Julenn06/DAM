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

class WorkoutAnadir : BaseActivity() {

    private lateinit var etNombreWorkout: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etVideo: EditText
    private lateinit var etNivel: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_formulario)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.anadir_workout_titulo)

        // Configurar botón de volver personalizado
        val btnVolverToolbar: ImageButton = toolbar.findViewById(R.id.btn_volver_toolbar)
        btnVolverToolbar.visibility = View.VISIBLE
        btnVolverToolbar.setOnClickListener {
            finish()
        }

        etNombreWorkout = findViewById(R.id.etNombreWorkout)
        etDescripcion = findViewById(R.id.etDescripcion)
        etVideo = findViewById(R.id.etVideo)
        etNivel = findViewById(R.id.etNivel)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnVolver = findViewById(R.id.btnVolver)

        btnGuardar.setOnClickListener {
            saveWorkout()
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

    private fun saveWorkout() {
        val name = etNombreWorkout.text.toString().trim()
        val description = etDescripcion.text.toString().trim()
        val videoUrl = etVideo.text.toString().trim()
        val levelString = etNivel.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || videoUrl.isEmpty() || levelString.isEmpty()) {
            Toast.makeText(this, getString(R.string.completar_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val level = levelString.toIntOrNull()
        if (level == null || level !in 0..3) {
            Toast.makeText(this, getString(R.string.nivel_numero_0_3), Toast.LENGTH_SHORT).show()
            return
        }

        Workout.crearWorkout(name, description, videoUrl, level, { created ->
            Toast.makeText(this, getString(R.string.workout_guardado), Toast.LENGTH_SHORT).show()
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
        }, { e ->
            Toast.makeText(this, getString(R.string.error_guardar_workout, e.message), Toast.LENGTH_SHORT).show()
        })
    }
}