package com.danielalonso.gestorcursos

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Configurar RecylerView
        recyclerView = findViewById(R.id.recylerViewCursos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdapterCurso(GestionCursos.cursos,this)
        // Ejecutar rellenado de ArrayList inicial
        GestionCursos.cargarCursosInicial()
    }
    // Gestionar devolucion de datos de las Activity
    // Si el codigo es 1, lee el extra ID y ESTADo y ejecuta el cambio de estado del ArrayList
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Codigo 1 y estado OK
        if (requestCode == 1 &&  resultCode == RESULT_OK) {
            // Conseguir extras
            val id = data?.getStringExtra("ID")
            val estado = data?.getStringExtra("ESTADO")
            // Ejecutar cambio de estado en ArrayList
            GestionCursos.cambiarEstado(id, estado)
            // Forzar actualizacion del RecyclerView
            recyclerView.adapter = AdapterCurso(GestionCursos.cursos, this)
        }
    }
}