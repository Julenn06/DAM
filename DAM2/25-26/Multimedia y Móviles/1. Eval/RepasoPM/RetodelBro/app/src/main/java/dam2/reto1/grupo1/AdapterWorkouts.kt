package dam2.reto1.grupo1

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.net.toUri
import java.util.Locale
import kotlin.math.roundToInt

class AdapterWorkouts(private var items: List<UsuWorkout>) : // Change to var
    RecyclerView.Adapter<AdapterWorkouts.AdapterMostrarViewHolder>() {

    class AdapterMostrarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.txtWKNombre)
        val descripcion: TextView = itemView.findViewById(R.id.txtWKDescripcion)
        val nivel: TextView = itemView.findViewById(R.id.txtWKNivel)
        val tiempototal: TextView = itemView.findViewById(R.id.txtWKTotalTime)
        val tiempoprevisto: TextView = itemView.findViewById(R.id.txtWKEstimatedTime)
        val fecha: TextView = itemView.findViewById(R.id.txtWKDate)
        val porcentaje: TextView = itemView.findViewById(R.id.txtWKCompletedPercentage)

        val video: Button = itemView.findViewById(R.id.btnPlayVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMostrarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_item, parent, false)
        return AdapterMostrarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterMostrarViewHolder, position: Int) {
        val workout = items[position]
        holder.nombre.text = workout.workout.nombre
        holder.descripcion.text = workout.workout.descripcion
        holder.nivel.text = holder.itemView.context.getString(R.string.workout_level, workout.workout.nivel)
        val minutos = workout.tiempo_total / 60
        val segundos = workout.tiempo_total % 60
        val tiempoFormateado = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos)
        holder.tiempototal.text = holder.itemView.context.getString(R.string.tiempo_total, tiempoFormateado)


        val minutosPrevisto = workout.workout.getTiempoPrevistoSegundos() / 60
        val segundosPrevisto =  workout.workout.getTiempoPrevistoSegundos() % 60
        val tiempoFormateadoPrevisto = String.format(Locale.getDefault(), "%02d:%02d", minutosPrevisto, segundosPrevisto)
        holder.tiempoprevisto.text = holder.itemView.context.getString(R.string.tiempo_previsto, tiempoFormateadoPrevisto)

        val porcentaje = ((workout.ejercicioscompletados * 100.0) / workout.workout.ejercicios.size).roundToInt()
        holder.porcentaje.text = holder.itemView.context.getString(R.string.completado, porcentaje)
        val formatoFecha =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.fecha.text = holder.itemView.context.getString(R.string.fecha, formatoFecha.format(workout.fecha))
        holder.video.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, workout.workout.video.toUri())
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WorkoutListaEjercicios::class.java)
            intent.putExtra("WORKOUT_ID", workout.workout.idWorkout)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<UsuWorkout>) {
        items = newItems
        notifyDataSetChanged()
    }
}
