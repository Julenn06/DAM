package com.julen.calculadora

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var primerNumero: Double = 0.0
    private var segundoNumero: Double = 0.0
    private var summary: Boolean = false
    private var restart: Boolean = false
    private var divider: Boolean = false
    private var multiplication: Boolean = false
    private var resultant: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val resultantEvacuation: EditText = findViewById(R.id.editTextNumber)
        val btnBoar: Button = findViewById(R.id.borrar)
        val btnSummary: Button = findViewById(R.id.sumar)
        val btnRestart: Button = findViewById(R.id.restar)
        val btnDivider: Button = findViewById(R.id.dividir)
        val btnMultiplication: Button = findViewById(R.id.multiplicar)
        val btnIgual: Button = findViewById(R.id.enviar)

        val btnNumero0: Button = findViewById(R.id.num0)
        val btnNumero1: Button = findViewById(R.id.num1)
        val btnNumero2: Button = findViewById(R.id.num2)
        val btnNumero3: Button = findViewById(R.id.num3)
        val btnNumero4: Button = findViewById(R.id.num4)
        val btnNumero5: Button = findViewById(R.id.num5)
        val btnNumero6: Button = findViewById(R.id.num6)
        val btnNumero7: Button = findViewById(R.id.num7)
        val btnNumero8: Button = findViewById(R.id.num8)
        val btnNumero9: Button = findViewById(R.id.num9)

        @SuppressLint("SetTextI18n")
        fun agreeNumero(numero: String) {
            val textoActual = resultantEvacuation.text.toString()
            if (textoActual == "0") {
                resultantEvacuation.setText(numero)
            } else {
                resultantEvacuation.setText(textoActual + numero)
            }
        }

        btnNumero0.setOnClickListener { agreeNumero("0") }
        btnNumero1.setOnClickListener { agreeNumero("1") }
        btnNumero2.setOnClickListener { agreeNumero("2") }
        btnNumero3.setOnClickListener { agreeNumero("3") }
        btnNumero4.setOnClickListener { agreeNumero("4") }
        btnNumero5.setOnClickListener { agreeNumero("5") }
        btnNumero6.setOnClickListener { agreeNumero("6") }
        btnNumero7.setOnClickListener { agreeNumero("7") }
        btnNumero8.setOnClickListener { agreeNumero("8") }
        btnNumero9.setOnClickListener { agreeNumero("9") }

        fun preparerOperation() {
            val text = resultantEvacuation.text.toString()
            if (text.isNotEmpty()) {
                primerNumero = text.toDouble()
                summary = false
                restart = false
                divider = false
                multiplication = false
                resultantEvacuation.setText("")
            }
        }

        btnSummary.setOnClickListener {
            preparerOperation()
            summary = true
        }
        btnRestart.setOnClickListener {
            preparerOperation()
            restart = true
        }
        btnDivider.setOnClickListener {
            preparerOperation()
            divider = true
        }
        btnMultiplication.setOnClickListener {
            preparerOperation()
            multiplication = true
        }

        btnIgual.setOnClickListener {
            val text = resultantEvacuation.text.toString()
            if (text.isNotEmpty()) {
                segundoNumero = text.toDouble()

                if (summary) {
                    resultant = primerNumero + segundoNumero
                } else if (restart) {
                    resultant = primerNumero - segundoNumero
                } else if (multiplication) {
                    resultant = primerNumero * segundoNumero
                } else if (divider) {
                    resultant = if (segundoNumero != 0.0) {
                        primerNumero / segundoNumero
                    } else {
                        0.0
                    }
                }

                resultantEvacuation.setText(resultant.toString())

                summary = false
                restart = false
                divider = false
                multiplication = false
            }
        }

        btnBoar.setOnClickListener {
            resultantEvacuation.setText("")
            primerNumero = 0.0
            segundoNumero = 0.0
            resultant = 0.0
            summary = false
            restart = false
            divider = false
            multiplication = false
        }
    }
}
