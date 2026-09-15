package com.danielalonso.gestionusuarios

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MostrarPersonas : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_personas)

        recyclerView = findViewById(R.id.recylerViewMostrar,);
        recyclerView.layoutManager = LinearLayoutManager(this,);
        if(ListaUsuarios.usuarios.isEmpty()){
            Toast.makeText(this, "No hay usuarios", Toast.LENGTH_SHORT).show()
        } else {
            recyclerView.adapter = AdapterMostrar(ListaUsuarios.usuarios)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnMostrar : Button = findViewById(R.id.buttonMostrar)
        btnMostrar.setOnClickListener {
            finish()
        }
    }
}