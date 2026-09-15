package com.example.a1ebalazterketaapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a1ebalazterketaapp.modelo.Book

class BookDetailActivity : AppCompatActivity() {
    private lateinit var tvTitulo: TextView
    private lateinit var tvAutor: TextView
    private lateinit var tvAno: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        tvTitulo = findViewById(R.id.tvTitulo)
        tvAutor = findViewById(R.id.tvAutor)
        tvAno = findViewById(R.id.tvAno)
        
        val book = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("book", Book::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("book")
        }
        
        book?.let {
            displayBookDetails(it)
        }
    }
    
    private fun displayBookDetails(book: Book) {
        tvTitulo.text = book.titulo
        tvAutor.text = book.autor
        tvAno.text = book.ano.toString()
    }
}