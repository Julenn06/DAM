package com.example.ariketa_5.nCategoria

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ariketa_5.R
import androidx.recyclerview.widget.RecyclerView
import com.example.ariketa_5.Modelo.Categoria


class CategoriasAdapter (private val categoriasList: List<Categoria>) : RecyclerView.Adapter<CategoriasViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriasViewHolder {
        //crea el viewHolder y lo devuelve
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriasViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CategoriasViewHolder,
        position: Int
    ) {
        //pasa el viewHolder que lo tenemos en una posicion concreta del array
        holder.render(categoriasList[position])
    }

    override fun getItemCount(): Int {
        // Devuelve el número de elementos en la lista
        return categoriasList.size
    }

}