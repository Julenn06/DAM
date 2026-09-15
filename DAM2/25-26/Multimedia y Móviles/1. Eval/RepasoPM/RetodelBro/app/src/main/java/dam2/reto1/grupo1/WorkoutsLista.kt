package dam2.reto1.grupo1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class WorkoutsLista : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterWorkouts
    private lateinit var chipGroup: ChipGroup
    private lateinit var tvEmptyMessage: TextView

    // Ya no se fuerza el tema claro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_workouts)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.listaworkouts)
        // Mostrar nivel del usuario (si hay usuario logueado)
        val toolbarLevel: TextView = findViewById(R.id.toolbar_level)
        val usuario = GymDaji.usuario
        if (usuario != null) {
            toolbarLevel.text = getString(R.string.workout_level, usuario.nivel)
            toolbarLevel.visibility = View.VISIBLE
        } else {
            toolbarLevel.visibility = View.GONE
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

            adapter = AdapterWorkouts(GymDaji.usuario?.workouts ?: emptyList())
            recyclerView.adapter = adapter
        }

        chipGroup = findViewById(R.id.chipGroup)
        @Suppress("DEPRECATION")
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val workoutsFiltrados = when (checkedId) {
                R.id.chipBasico -> GymDaji.usuario?.workouts?.filter { it.workout.nivel == 0 } ?: emptyList()
                R.id.chipPrincipiante -> GymDaji.usuario?.workouts?.filter { it.workout.nivel == 1 } ?: emptyList()
                R.id.chipIntermedio -> GymDaji.usuario?.workouts?.filter { it.workout.nivel == 2 } ?: emptyList()
                R.id.chipAvanzado -> GymDaji.usuario?.workouts?.filter { it.workout.nivel == 3 } ?: emptyList()
                else -> GymDaji.usuario?.workouts ?: emptyList()
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

    override fun onResume() {
        super.onResume()
        val user = GymDaji.usuario
        if (user != null) {
            GymDaji.conseguirdatos(
                activity = this,
                usuario = user,
                exito = {
                    // Actualiza el nivel mostrado en toolbar, si existe
                    val toolbarLevel: TextView = findViewById(R.id.toolbar_level)
                    toolbarLevel.text = getString(R.string.workout_level, GymDaji.usuario?.nivel ?: 0)
                    // Actualiza RV y empty state
                    val lista = GymDaji.usuario?.workouts ?: emptyList()
                    if (lista.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        tvEmptyMessage.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        tvEmptyMessage.visibility = View.GONE
                        if (::adapter.isInitialized) {
                            adapter.updateData(lista)
                        } else {
                            adapter = AdapterWorkouts(lista)
                            recyclerView.adapter = adapter
                        }
                    }
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_ejercicios, menu)
        val actionEntrenador = menu?.findItem(R.id.action_entrenador)
        if (GymDaji.usuario?.tipo == "entrenador") {
            actionEntrenador?.isVisible = true
        } else {
            actionEntrenador?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_entrenador -> {
                val intent = Intent(this, WorkoutsEntrenador::class.java)
                startActivity(intent)
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this@WorkoutsLista, EditarPerfil::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}