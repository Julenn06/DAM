package com.example.ariketa_5

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ariketa_5.Modelo.Categoria
import com.example.ariketa_5.Modelo.Tarea
import com.example.ariketa_5.databinding.ActivityTodoBinding
import com.example.ariketa_5.nCategoria.CategoriasAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var categoriasAdapter: CategoriasAdapter

    //array de categorias
    private val categorias = listOf(
        Categoria(1, "Trabajo"),
        Categoria(2, "Personajes"),
        Categoria(3, "Otros")
    )

    //array de tareas
    private val tareas = mutableListOf(
        Tarea("Apintes Kotlin", 1),
        Tarea("Apintes AD", 1),
        Tarea("Visitar Amama", 2),
        Tarea("Cafe con Ainhize", 2),
        Tarea("Sacar al perro", 3),
        Tarea("Renovar DNI", 3),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_todo)
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }
    private fun initUI() {
        //pasamos el array de categorias al adapter
        categoriasAdapter = CategoriasAdapter(categorias)
        binding.rvCategorias.layoutManager = LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriasAdapter
        //pasamos el array de tareas al adapter
        //val tareasAdapter = TareasAdapter(tareas)
        //binding.rvTareas.layoutManager = LinearLayoutManager(this)
        //binding.rvTareas.adapter = tareasAdapter
    }
}