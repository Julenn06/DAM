package com.example.elormovpmdm.ui.meetings.addAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.R
import com.example.elormovpmdm.domain.model.User

/**
 * Adaptador especializado para la selección de un usuario dentro de un diálogo.
 * Gestiona el estado de selección única entre los elementos de la lista.
 */
class AddDialogAdapter(
    private var userList: List<User> = emptyList(),
    private val onMeetingUserSelected: (User) -> Unit
): RecyclerView.Adapter<AddDialogViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddDialogViewHolder {
        return AddDialogViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.user_card, parent, false)
        )
    }

    /**
     * Actualiza la colección de usuarios y refresca la vista.
     */
    fun updateList(listUpdated: List<User>) {
        userList = listUpdated
        notifyDataSetChanged()
    }

    // Almacena el índice del elemento seleccionado actualmente.
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onBindViewHolder(
        holder: AddDialogViewHolder,
        position: Int
    ) {
        holder.render(
            userList[position],
            isSelected = position == selectedPosition,
            onMeetingUserSelected = { user ->
                val currentPosition = holder.adapterPosition
                if (currentPosition == RecyclerView.NO_POSITION) return@render

                // Guardamos la posición anterior para desmarcarla
                val previousPosition = selectedPosition
                selectedPosition = currentPosition

                // Notificamos cambios solo en los elementos afectados para optimizar el rendimiento
                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition)
                }
                notifyItemChanged(currentPosition)

                // Comunicamos la selección al fragmento/diálogo
                onMeetingUserSelected(user)
            }
        )
    }

    override fun getItemCount(): Int = userList.size
}