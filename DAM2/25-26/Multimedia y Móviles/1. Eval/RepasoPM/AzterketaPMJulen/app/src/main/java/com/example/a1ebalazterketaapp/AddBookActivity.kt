package com.example.a1ebalazterketaapp

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
    }
    
    private fun addBook() {
        val titulo = etTitulo.text.toString().trim()
        val autor = etAutor.text.toString().trim()
        val anoStr = etAno.text.toString().trim()
        
        if (titulo.isEmpty() || autor.isEmpty() || anoStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        
        val ano = anoStr.toIntOrNull()
        if (ano == null) {
            Toast.makeText(this, "El año debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }
        
        val book = Book(titulo = titulo, autor = autor, ano = ano)
        
        lifecycleScope.launch {
            bookRepository.addBook(book)
                .onSuccess {
                    Toast.makeText(this@AddBookActivity, "Libro añadido correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .onFailure { e ->
                    Toast.makeText(this@AddBookActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}