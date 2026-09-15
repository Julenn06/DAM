package dam2.reto1.grupo1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class WorkoutsEntrenador : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterWorkoutsEntrenador
    private lateinit var buttonAnadirWorkout: Button
    private lateinit var chipGroup: ChipGroup
    private lateinit var tvEmptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entrenador)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.entrenador)

        // Configurar botón de volver personalizado
        val btnVolverToolbar: ImageButton = toolbar.findViewById(R.id.btn_volver_toolbar)
        btnVolverToolbar.visibility = View.VISIBLE
        btnVolverToolbar.setOnClickListener {
            finish()
        }

        buttonAnadirWorkout = findViewById(R.id.buttonAnadirWorkout)
        buttonAnadirWorkout.setOnClickListener { 
            val intent = Intent(this, WorkoutAnadir::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recylerViewMostrar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)

        if(GymDaji.workouts.isEmpty()){
            recyclerView.visibility = View.GONE
            tvEmptyMessage.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvEmptyMessage.visibility = View.GONE
            adapter = AdapterWorkoutsEntrenador(GymDaji.workouts.toMutableList())
            recyclerView.adapter = adapter
        }

        chipGroup = findViewById(R.id.chipGroup)
        @Suppress("DEPRECATION")
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val workoutsFiltrados = when (checkedId) {
                R.id.chipBasico -> GymDaji.workouts.filter { it.nivel == 0 }
                R.id.chipPrincipiante -> GymDaji.workouts.filter { it.nivel == 1 }
                R.id.chipIntermedio -> GymDaji.workouts.filter { it.nivel == 2 }
                R.id.chipAvanzado -> GymDaji.workouts.filter { it.nivel == 3 }
                else -> GymDaji.workouts
            }

            if (workoutsFiltrados.isEmpty()) {
                recyclerView.visibility = View.GONE
                tvEmptyMessage.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                tvEmptyMessage.visibility = View.GONE
            }

            adapter.updateData(workoutsFiltrados)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_entrenador, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        val user = GymDaji.usuario
        if (user != null) {
            GymDaji.conseguirdatos(
                activity = this,
                usuario = user,
                exito = {
                    if (GymDaji.workouts.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        tvEmptyMessage.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        tvEmptyMessage.visibility = View.GONE
                        if (::adapter.isInitialized) {
                            adapter.updateData(GymDaji.workouts)
                        } else {
                            adapter = AdapterWorkoutsEntrenador(GymDaji.workouts.toMutableList())
                            recyclerView.adapter = adapter
                        }
                    }
                }
            )
        }
    }
}