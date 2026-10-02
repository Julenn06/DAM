package com.example.a1ebalazterketaapp.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a1ebalazterketaapp.R
import com.example.a1ebalazterketaapp.modelo.Book

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
    private val tvAutor: TextView = itemView.findViewById(R.id.tvAutor)
    private val tvAno: TextView = itemView.findViewById(R.id.tvAno)
    
    fun bind(book: Book, onClick: (Book) -> Unit) {
        tvTitulo.text = book.titulo
        tvAutor.text = book.autor
        tvAno.text = book.ano.toString()
        
        itemView.setOnClickListener {
            onClick(book)
        }
    }
}