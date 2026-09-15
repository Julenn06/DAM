package com.example.forresult

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val txtSegundo : EditText = findViewById(R.id.editTextText)
        var btnSegundo : Button = findViewById(R.id.buttonEnviar)
        
        btnSegundo.setOnClickListener {
           var intent = Intent();
            intent.putExtra("TXT",txtSegundo.text.toString());
            setResult(RESULT_OK, intent);
            finish();
        }

    }
}