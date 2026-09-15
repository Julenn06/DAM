package com.example.elormovpmdm.ui.meetings.addAdapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.R
import com.example.elormovpmdm.databinding.UserCardBinding
import com.example.elormovpmdm.domain.model.User

/**
 * ViewHolder para la lista de selección de usuarios en el diálogo de reuniones.
 * Maneja el feedback visual de selección y la limpieza de componentes no necesarios.
 */
class AddDialogViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = UserCardBinding.bind(view)

    /**
     * @param user Datos del usuario (alumno/profesor).
     * @param isSelected Define si esta tarjeta debe mostrarse como seleccionada.
     * @param onMeetingUserSelected Notifica la selección al adaptador.
     */
    fun render(user: User, isSelected: Boolean, onMeetingUserSelected: (User) -> Unit) {
        // Concatenación de nombre completo
        binding.tvName.text = "${user.nombre} ${user.apellidos}"
        binding.tvEmail.text = user.email

        // Reutilización de layout: Oculta elementos que no se necesitan en el diálogo
        // pero que sí existen en el diseño general de user_card.
        binding.sivProfile.visibility = View.INVISIBLE
        binding.btnBack.visibility = View.INVISIBLE

        /**
         * Feedback visual de selección:
         * Si está seleccionado, aplica el color corporativo (naranja).
         * Si no, aplica el color de divisor para un aspecto neutro.
         */
        binding.root.setCardBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                if(isSelected) R.color.orange else R.color.divider
            )
        )

        // Listener de click para disparar la lógica de selección única del adaptador
        binding.root.setOnClickListener {
            onMeetingUserSelected(user)
        }
    }
}