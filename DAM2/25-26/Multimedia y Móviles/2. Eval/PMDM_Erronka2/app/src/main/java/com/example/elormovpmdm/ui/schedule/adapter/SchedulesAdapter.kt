package com.example.elormovpmdm.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.R
import com.example.elormovpmdm.domain.model.User

/**
 * Adaptador encargado de gestionar y mostrar la lista de usuarios (profesores)
 * en el RecyclerView de horarios.
 * * @property userList Lista de usuarios a representar en la interfaz.
 * @property onItemSelected Callback que se dispara cuando se pulsa sobre un usuario.
 */
class SchedulesAdapter(
    private var userList: List<User> = emptyList(),
    private val onItemSelected: (User) -> Unit
): RecyclerView.Adapter<SchedulesViewHolder>() {

    /**
     * Crea y devuelve un nuevo SchedulesViewHolder inflando el diseño 'user_card'.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SchedulesViewHolder {
        return SchedulesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.user_card, parent, false)
        )
    }

    /**
     * Actualiza la colección de datos del adaptador y refresca toda la lista en la UI.
     * * @param listUpdated La nueva lista de usuarios a mostrar.
     */
    fun updateList(listUpdated: List<User>) {
        userList = listUpdated
        notifyDataSetChanged()
    }

    /**
     * Vincula los datos de un usuario en una posición específica con el ViewHolder correspondiente.
     */
    override fun onBindViewHolder(
        holder: SchedulesViewHolder,
        position: Int
    ) {
        holder.render(userList[position], onItemSelected)
    }

    /**
     * Devuelve la cantidad total de elementos presentes en la lista actual.
     */
    override fun getItemCount(): Int {
        return userList.size
    }

}