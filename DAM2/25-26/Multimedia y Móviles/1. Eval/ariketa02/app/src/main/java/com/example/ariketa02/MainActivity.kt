package com.example.ariketa02

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.slider.Slider
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    // Grupo de género
    lateinit var generoGroup: MaterialButtonToggleGroup

    // Botón Calcular
    lateinit var btnCalcular: MaterialButton

    // Sliders y TextViews de altura y peso
    lateinit var sliderAltura: Slider
    lateinit var tvAltura: TextView
    lateinit var sliderPeso: Slider
    lateinit var tvPeso: TextView

    // Texto para mostrar resultado
    lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Referencias a elementos del layout
        generoGroup = findViewById(R.id.generoGroup)
        btnCalcular = findViewById(R.id.btnCalcular)
    sliderAltura = findViewById(R.id.sliderAltura)
    sliderPeso = findViewById(R.id.numeroPeso)
    // tvAltura = findViewById(R.id.tvAltura) // Si tienes un TextView para mostrar la altura, crea uno en el XML y descomenta esta línea

        btnCalcular.setOnClickListener {
            val altura = sliderAltura.value / 100.0f
            val peso = sliderPeso.value
            val imc = peso / (altura * altura)

            val resultado = when (generoGroup.checkedButtonId) {
                R.id.btnHombre -> {
                    when {
                        imc < 20.7 -> getString(R.string.title_bajo_peso)
                        imc <= 26.4 -> getString(R.string.title_peso_normal)
                        imc <= 31.1 -> getString(R.string.title_sobrepeso)
                        else -> getString(R.string.title_obesidad)
                    }
                }
                R.id.btnMujer -> {
                    when {
                        imc < 19.1 -> getString(R.string.title_bajo_peso)
                        imc <= 25.8 -> getString(R.string.title_peso_normal)
                        imc <= 32.3 -> getString(R.string.title_sobrepeso)
                        else -> getString(R.string.title_obesidad)
                    }
                }
                else -> getString(R.string.error)
            }

            // Lanzar nueva Activity con Intent
            val intent = Intent(this, ResultadoActivity::class.java)
            intent.putExtra("RESULTADO_IMC", resultado)
            startActivity(intent)
        }
    }
}
