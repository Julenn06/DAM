package com.danielalonso.gestorcursos

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class AdapterCurso(private val items: List<Curso>, private val activity: MainActivity) :
    RecyclerView.Adapter<AdapterCurso.AdapterCursoViewHolder>() {

    class AdapterCursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Encontrar TextView a reeemplazar
        val nombre: TextView = itemView.findViewById(R.id.textViewNombreItem)
        val duracion: TextView = itemView.findViewById(R.id.textViewDuracionItem)
        val estado: TextView = itemView.findViewById(R.id.textViewEstadoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCursoViewHolder {
        // Elegir layout del item y configurar
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.curso_item, parent, false)
        return AdapterCursoViewHolder(view)
    }

    override fun onBindViewHolder(holder:AdapterCursoViewHolder, position: Int) {
        // Reemplazar datos en el item
        holder.nombre.text = items[position].nombre
        holder.duracion.text =holder.itemView.context.getString(R.string.duracion,items[position].duracionHoras )
        holder.estado.text = items[position].estado
        // Si pulsa sobre el item, hacer un intent a DetalleActivity con el ID del curso seleccionado
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, DetalleActivity::class.java)
            intent.putExtra("ID", items[position].id)
            activity.startActivityForResult(intent,1)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}