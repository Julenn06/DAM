package com.example.elormovpmdm.ui.students.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.databinding.UserCardBinding
import com.example.elormovpmdm.domain.model.User

/**
 * ViewHolder encargado de gestionar la lógica de pintado y eventos de una celda individual
 * en la lista de estudiantes. Vincula los datos del modelo User con el layout user_card.
 */
class StudentsViewHolder(view: View): RecyclerView.ViewHolder(view) {
    /**
     * Referencia a las vistas del diseño mediante ViewBinding para evitar el uso de findViewById.
     */
    private val binding = UserCardBinding.bind(view)

    /**
     * Función que asigna los valores del estudiante a los componentes visuales.
     * * @param user Objeto que contiene la información del alumno (nombre, apellidos, email, etc.).
     * @param onItemSelected Callback que notifica al fragmento cuando se hace click en la tarjeta.
     */
    fun render(user: User, onItemSelected: (User) -> Unit) {
        val name: String = user.nombre
        val surname: String = user.apellidos

        binding.tvName.text = "$name $surname"
        binding.tvEmail.text = user.email

        /**
         * Establece el listener de click en la vista raíz del ViewHolder para
         * disparar la acción definida en el adaptador (abrir el BottomSheet).
         */
        binding.root.setOnClickListener {
            onItemSelected(user)
        }
    }
}