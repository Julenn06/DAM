package com.example.ariketa02

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultado)

        val tvResultadoFinal = findViewById<TextView>(R.id.tvResultadoFinal)

        // Recogemos el texto que mandamos desde MainActivity
        val resultado = intent.getStringExtra("RESULTADO_IMC")
        tvResultadoFinal.text = resultado
    }
}
