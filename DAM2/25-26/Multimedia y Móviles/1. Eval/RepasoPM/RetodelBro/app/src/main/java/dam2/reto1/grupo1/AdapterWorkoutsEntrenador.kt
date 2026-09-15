package dam2.reto1.grupo1

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class AdapterWorkoutsEntrenador(private var items: MutableList<Workout>) : // Change to var and MutableList
    RecyclerView.Adapter<AdapterWorkoutsEntrenador.AdapterMostrarViewHolder>() {

    class AdapterMostrarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.txtWKNombre)
        val descripcion: TextView = itemView.findViewById(R.id.txtWKDescripcion)
        val nivel: TextView = itemView.findViewById(R.id.txtWKNivel)
        val eliminar: ImageButton = itemView.findViewById(R.id.btnEliminarWorkout)
        val modificar: ImageButton = itemView.findViewById(R.id.btnModificarWorkout)
        val video: Button = itemView.findViewById(R.id.btnReproducirVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMostrarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_item_entrenador, parent, false)
        return AdapterMostrarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterMostrarViewHolder, position: Int) {
        val item = items[position]
        holder.nombre.text = item.nombre
        holder.descripcion.text = item.descripcion
        holder.nivel.text = holder.itemView.context.getString(R.string.workout_level, item.nivel)

        holder.eliminar.setOnClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.confirmar_eliminacion))
                .setMessage(context.getString(R.string.seguro_borrar_workout))
                .setPositiveButton(context.getString(R.string.si)) { _, _ ->
                    // Delegar borrado a Modelos.Workout.deleteWorkoutById
                    Workout.borrarWorkout(item.idWorkout, {
                        Toast.makeText(context, context.getString(R.string.workout_eliminado), Toast.LENGTH_SHORT).show()
                        items.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, items.size)
                        // Refrescar datos globales para otros RecyclerViews
                        val user = GymDaji.usuario
                        if (user != null && context is BaseActivity) {
                            GymDaji.conseguirdatos(
                                activity = context,
                                usuario = user,
                                exito = {
                                    // No acción adicional aquí
                                }
                            )
                        }
                    }, { e ->
                        Toast.makeText(context, context.getString(R.string.error_eliminar, e.message), Toast.LENGTH_SHORT).show()
                    })
                }
                .setNegativeButton(context.getString(R.string.no), null)
                .show()
        }

        holder.modificar.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, WorkoutEditar::class.java)
            intent.putExtra("workoutId", item.idWorkout)
            context.startActivity(intent)
        }
        holder.video.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, item.video.toUri())
            holder.itemView.context.startActivity(intent)
        }
        // Clic en el item completo para ver ejercicios
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WorkoutListaEjercicios::class.java)
            intent.putExtra("WORKOUT_ID", item.idWorkout)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<Workout>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
