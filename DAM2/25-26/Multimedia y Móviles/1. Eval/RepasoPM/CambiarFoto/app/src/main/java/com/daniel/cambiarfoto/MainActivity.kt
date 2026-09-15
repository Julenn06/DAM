package com.daniel.cambiarfoto

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        val imgV= findViewById<ImageView>(R.id.imgV)
        val btnF1 = findViewById<Button>(R.id.btnF1)
        val btnF2 = findViewById<Button>(R.id.btnF2)
        btnF1.setOnClickListener {
            imgV.setImageResource(R.drawable.f1);
        }
        btnF2.setOnClickListener {
            imgV.setImageResource(R.drawable.f2);
        }


    }
}