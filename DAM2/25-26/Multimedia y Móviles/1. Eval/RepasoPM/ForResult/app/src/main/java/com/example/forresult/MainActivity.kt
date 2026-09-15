package com.example.forresult

import android.content.Intent
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
        val btnPrimerActivity: Button = findViewById(R.id.btnPrimerActivity)
        btnPrimerActivity.setOnClickListener {
            var intent = Intent(this, SecondActivity::class.java);
            startActivityForResult(intent, 150  )

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val lblPrimerActivity: TextView = findViewById(R.id.lblPrimerActivity)
        if (requestCode == 150 &&  resultCode == RESULT_OK) {
                lblPrimerActivity.text = data?.getStringExtra("TXT")
        }
    }
}