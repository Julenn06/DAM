package com.daniel.cambiartexto

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
        val labelEjemplo = findViewById<TextView>(R.id.labelEjemplo)
        val btnCambiar = findViewById<Button>(R.id.btnCambiar)
        val txtName = findViewById<TextView>(R.id.txtName)
        btnCambiar.setOnClickListener {
            labelEjemplo.text= txtName.text
        }

    }
}