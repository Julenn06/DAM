package com.danielalonso.gestionusuarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BorrarPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_borrar_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtNombre : EditText = findViewById(R.id.fieldNombreBorrar)
        var btnBorrar : Button = findViewById(R.id.buttonBorrar)

        btnBorrar.setOnClickListener {
            if(txtNombre.text.isEmpty() ){
                 Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
            else if (ListaUsuarios.usuarios.find { it.nombre == txtNombre.text.toString() } == null){
                Toast.makeText(this, "No existe ninguna persona con ese nombre", Toast.LENGTH_SHORT).show()
            }
            else {
                var intent = Intent();
                intent.putExtra("NOMBRE",txtNombre.text.toString());
                setResult(RESULT_OK, intent);
                finish();
            }

        }
    }
}