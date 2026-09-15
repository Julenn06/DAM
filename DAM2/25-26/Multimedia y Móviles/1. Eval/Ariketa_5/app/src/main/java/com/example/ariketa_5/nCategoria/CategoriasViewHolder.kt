package com.example.ariketa_5.nCategoria

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.ariketa_5.R
import androidx.recyclerview.widget.RecyclerView
import com.example.ariketa_5.Modelo.Categoria


class CategoriasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tVCategoria: TextView = view.findViewById(R.id.tvCategoryName)
    //private val cVCategoria: CardView = view.findViewById(R.id.viewContainer)
    //private val viewdivider: View = view.findViewById(R.id.divider)

    fun render(categoria: Categoria) {
        tVCategoria.text = categoria.name
    }
}