package com.example.ariketa00

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.content.Intent
import android.view.View
import android.widget.TextView


class FirstActivity : AppCompatActivity() {

    lateinit var myButton: Button
    lateinit var etName: EditText
    lateinit var tVNum: TextView
    lateinit var btnSumar: Button
    lateinit var btnRestar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //initComponents()
        //initListeners()
    }
    // Iniciar los componentes
    fun initComponents(){

        tVNum = findViewById<TextView>(R.id.texto)
        btnSumar = findViewById<Button>(R.id.sumar)
        btnRestar = findViewById<Button>(R.id.restar)

    }
    // Mandar el texto del edit text a la otro activity cuando se pulse el boton
    fun initListeners(){

        btnSumar.setOnClickListener { view: View ->
            tVNum.text = (tVNum.text.toString().toInt() + 1).toString()
        }
        btnRestar.setOnClickListener { view: View ->
            tVNum.text = (tVNum.text.toString().toInt() - 1).toString()
        }
    }

}