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

class AnadirPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_anadir_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtNombre : EditText = findViewById(R.id.fieldNombre)
        val txtEmail : EditText = findViewById(R.id.fieldEmail)
        var btnAnadir : Button = findViewById(R.id.buttonAnadir)

        btnAnadir.setOnClickListener {
            if(txtNombre.text.isEmpty() ){
                 Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else if (txtEmail.text.isEmpty()) {
                Toast.makeText(this, "El email no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
            else {
                var intent = Intent();
                intent.putExtra("NOMBRE",txtNombre.text.toString());
                intent.putExtra("EMAIL",txtEmail.text.toString());
                setResult(RESULT_OK, intent);
                finish();
            }

        }
    }
}