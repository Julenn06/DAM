package com.danielalonso.gestionusuarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnAnadir: Button = findViewById(R.id.btnAnadir)
        val btnBorrar: Button = findViewById(R.id.btnBorrar)
        val btnMostrar: Button = findViewById(R.id.btnMostrar)

        btnAnadir.setOnClickListener {
            var intent = Intent(this, AnadirPersona::class.java);
            startActivityForResult(intent, 1  )
        }
        btnBorrar.setOnClickListener {
            var intent = Intent(this, BorrarPersona::class.java);
            startActivityForResult(intent, 2  )
        }
        btnMostrar.setOnClickListener {
            var intent = Intent(this, MostrarPersonas::class.java);
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 &&  resultCode == RESULT_OK) {
            val nombre = data?.getStringExtra("NOMBRE")
            val email = data?.getStringExtra("EMAIL")
            if (nombre != null && email != null) {
                val persona = Usuario(nombre, email)
                ListaUsuarios.usuarios.add(persona)
            }
        } else if (requestCode == 2 &&  resultCode == RESULT_OK) {
            val nombre = data?.getStringExtra("NOMBRE")
            ListaUsuarios.usuarios.removeIf { it.nombre == nombre }
        }
    }
}