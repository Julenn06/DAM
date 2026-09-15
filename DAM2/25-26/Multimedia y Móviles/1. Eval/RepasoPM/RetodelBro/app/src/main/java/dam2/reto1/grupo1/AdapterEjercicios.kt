package dam2.reto1.grupo1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterEjercicios(private var items: List<Ejercicio>) :
    RecyclerView.Adapter<AdapterEjercicios.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtExerciseName)
        val description: TextView = itemView.findViewById(R.id.txtExerciseDescription)
        val series: TextView = itemView.findViewById(R.id.txtExerciseSeries)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ejercicio_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ejercicio = items[position]
        holder.name.text = ejercicio.nombre
        holder.description.text = ejercicio.descripcion
        holder.series.text = "Series: ${ejercicio.series.size}"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<Ejercicio>) {
        items = newItems
        notifyDataSetChanged()
    }
}
