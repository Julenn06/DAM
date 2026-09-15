package com.example.ariketa01

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var btnNext: Button
    lateinit var btnPrevius: Button
    lateinit var btnTrue: Button
    lateinit var btnFalse: Button
    lateinit var tvPregunta: TextView
    lateinit var toast: Toast
    lateinit var th: IntArray

    var indice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        th = intArrayOf(
            R.string.pregunta1,
            R.string.pregunta2,
            R.string.pregunta3,
            R.string.pregunta4,
            R.string.pregunta5
        )

        btnNext = findViewById<Button>(R.id.Next)
        btnPrevius = findViewById<Button>(R.id.Previus)
        btnTrue = findViewById<Button>(R.id.True)
        btnFalse = findViewById<Button>(R.id.False)
        tvPregunta = findViewById<TextView>(R.id.texto)

        btnNext.setOnClickListener { view ->
            if (indice == 4)
                indice = 0
            else
                indice++
            tvPregunta.setText(th[indice])
        }

        btnPrevius.setOnClickListener { view ->
            if (indice == 0)
                indice = 4
            else
                indice--
            tvPregunta.setText(th[indice])
        }

        btnTrue.setOnClickListener { view ->
            Toast.makeText(this, R.string.True, Toast.LENGTH_SHORT).show()
        }

        btnFalse.setOnClickListener { view ->
            Toast.makeText(this, R.string.False, Toast.LENGTH_SHORT).show()
        }
    }
}