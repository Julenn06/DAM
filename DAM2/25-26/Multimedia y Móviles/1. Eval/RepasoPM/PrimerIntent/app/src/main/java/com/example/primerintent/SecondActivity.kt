package com.example.primerintent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textViewGreeting: TextView = findViewById(R.id.textViewGreeting)

        // Null safety avanzado: getStringExtra devuelve String?
        // Usamos let + elvis operator para manejar null/blank de forma elegante
        val name: String = intent.getStringExtra(VALUES.EXTRA_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: "Invitado"

        textViewGreeting.text = "Hola, $name"

        val editTextName: EditText = findViewById(R.id.editTextName2)
        val buttonSend: Button = findViewById(R.id.buttonSend2)

        buttonSend.setOnClickListener {
            val name: String = editTextName.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(this, "Introduce un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(VALUES.EXTRA_NAME, name)
            }

            startActivity(intent)
        }
    }
}