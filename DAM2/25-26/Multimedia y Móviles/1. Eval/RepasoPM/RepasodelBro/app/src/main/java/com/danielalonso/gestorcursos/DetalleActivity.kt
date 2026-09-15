package com.danielalonso.gestorcursos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleActivity : AppCompatActivity() {
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Conseguir datos del curso
        id = intent.getStringExtra("ID") ?: "0"
        val curso = GestionCursos.conseguirCurso(id)
        // Reemplazar datos
        val nombre: TextView = findViewById(R.id.textViewNombre)
        val estado: TextView = findViewById(R.id.textViewEstado)
        val duracion: TextView = findViewById(R.id.textViewDuracion)
        val btnCambiar: Button = findViewById(R.id.btnCambiar)
        val btnVolver: Button = findViewById(R.id.btnVolver)
        nombre.text = curso.nombre
        estado.text = curso.estado
        duracion.text =  getString(R.string.duracion,curso.duracionHoras )
        // Cambio del boton, segun en que este el estado
        if(curso.estado == getString(R.string.acabado)){
            btnCambiar.text =  getString(R.string.cambiarpendiente)
        } else {
            btnCambiar.text =  getString(R.string.cambiaracabado)
        }
        // Cambiar estado al contrario en la activity
        btnCambiar.setOnClickListener {
            if(estado.text == getString(R.string.pendiente)){
                estado.text = getString(R.string.acabado)
                btnCambiar.text = getString(R.string.cambiarpendiente)

            } else {
                estado.text = getString(R.string.pendiente)
                btnCambiar.text = getString(R.string.cambiaracabado)
            }
        }
        // Pasar datos de ID y ESTADO en un intent y cerrar la ventana
        btnVolver.setOnClickListener {
            val intent = Intent()
            intent.putExtra("ID",curso.id)
            intent.putExtra("ESTADO",estado.text)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}