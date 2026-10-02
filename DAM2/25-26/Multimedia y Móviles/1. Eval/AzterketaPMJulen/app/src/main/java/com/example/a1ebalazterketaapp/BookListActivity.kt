package com.example.a1ebalazterketaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1ebalazterketaapp.adapter.BookAdapter
import com.example.a1ebalazterketaapp.modelo.Book
import com.example.a1ebalazterketaapp.repository.BookRepository
import kotlinx.coroutines.launch

class BookListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private val bookRepository = BookRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupRecyclerView()
        loadBooks()
    }
    
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        bookAdapter = BookAdapter(emptyList()) { book ->
            openBookDetail(book)
        }
        recyclerView.adapter = bookAdapter
    }
    
    private fun loadBooks() {
        lifecycleScope.launch {
            bookRepository.getAllBooks()
                .onSuccess { books ->
                    bookAdapter.updateBooks(books)
                }
                .onFailure { e ->
                    Toast.makeText(this@BookListActivity, "Error al cargar libros: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    
    private fun openBookDetail(book: Book) {
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra("book", book)
        startActivity(intent)
    }
    
    override fun onResume() {
        super.onResume()
        loadBooks()
    }
}