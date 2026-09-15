package com.example.elormovpmdm.ui.schedule.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.databinding.UserCardBinding
import com.example.elormovpmdm.domain.model.User

/**
 * ViewHolder encargado de gestionar la visualización individual de cada usuario (profesor)
 * dentro de la lista de horarios. Vincula los datos del modelo User con las vistas del diseño.
 */
class SchedulesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    /**
     * Referencia a los componentes visuales del layout user_card mediante ViewBinding.
     */
    private val binding = UserCardBinding.bind(view)

    /**
     * Función que asigna los valores del usuario a los elementos de la interfaz.
     * * @param user Objeto que contiene la información del profesor.
     * @param onItemSelected Lambda que se ejecuta al pulsar sobre la tarjeta del usuario.
     */
    fun render(user: User, onItemSelected: (User) -> Unit) {
        val name: String = user.nombre ?: "Sin nombre"
        val surname: String = user.apellidos ?: ""

        binding.tvName.text = "$name $surname"
        binding.tvEmail.text = user.email
        
        binding.root.setOnClickListener { 
            onItemSelected(user)
        }
    }
}