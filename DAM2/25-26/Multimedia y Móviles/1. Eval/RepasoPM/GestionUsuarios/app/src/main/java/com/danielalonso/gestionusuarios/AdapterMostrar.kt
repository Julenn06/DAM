package com.danielalonso.gestionusuarios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterMostrar(private val items: List<Usuario>) :
    RecyclerView.Adapter<AdapterMostrar.AdapterMostrarViewHolder>() {

    class AdapterMostrarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textViewEmailItem)
        val email: TextView = itemView.findViewById(R.id.textViewEmailItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMostrarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mostrar_persona_item, parent, false)
        return AdapterMostrarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterMostrarViewHolder, position: Int) {
        holder.nombre.text = items[position].nombre;
        holder.email.text = items[position].correo ;
    }

    override fun getItemCount(): Int {
        return items.size
    }
}