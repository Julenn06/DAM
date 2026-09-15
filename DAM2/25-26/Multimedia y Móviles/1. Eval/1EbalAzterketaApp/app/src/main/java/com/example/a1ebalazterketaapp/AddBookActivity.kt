package com.example.a1ebalazterketaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a1ebalazterketaapp.modelo.Book
import com.example.a1ebalazterketaapp.repository.BookRepository
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {
    private lateinit var etTitulo: EditText
    private lateinit var etAutor: EditText
    private lateinit var etAno: EditText

    private lateinit var btnClose: Button

    private val bookRepository = BookRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_book)
        
        etTitulo = findViewById(R.id.etTitulo)
        etAutor = findViewById(R.id.etAutor)
        etAno = findViewById(R.id.etAno)
        
        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            addBook()
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        btnClose = findViewById(R.id.btnBack)
    }

    private fun setupListeners() {
        btnClose.setOnClickListener {
            close()
        }
    }

    private fun close() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun addBook() {
        val titulo = etTitulo.text.toString().trim()
        val autor = etAutor.text.toString().trim()
        val anoStr = etAno.text.toString().trim()
        
        if (titulo.isEmpty() || autor.isEmpty() || anoStr.isEmpty()) {
            Toast.makeText(this, "Datu guztiak bete", Toast.LENGTH_SHORT).show()
            return
        }
        
        val ano = anoStr.toIntOrNull()
        if (ano == null) {
            Toast.makeText(this, "Urte egokia sartu", Toast.LENGTH_SHORT).show()
            return
        }
        
        val book = Book(titulo = titulo, autor = autor, ano = ano)
        
        lifecycleScope.launch {
            bookRepository.addBook(book)
                .onSuccess {
                    Toast.makeText(this@AddBookActivity, "Liburua ondo gehituta", Toast.LENGTH_SHORT).show()
                    close()
                    finish()
                }
                .onFailure { e ->
                    Toast.makeText(this@AddBookActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}