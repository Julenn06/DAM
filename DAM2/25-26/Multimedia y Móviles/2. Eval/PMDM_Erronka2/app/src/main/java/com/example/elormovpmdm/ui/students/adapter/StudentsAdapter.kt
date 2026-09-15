package com.example.elormovpmdm.ui.students.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.R
import com.example.elormovpmdm.domain.model.User

/**
 * Adaptador encargado de gestionar la lista de estudiantes para su visualización en un RecyclerView.
 * Vincula los datos de los objetos User con las celdas individuales definidas en el ViewHolder.
 * * @property userList Lista de usuarios que se mostrarán en la interfaz.
 * @property onItemSelected Función callback que se ejecuta cuando el usuario pulsa un elemento.
 */
class StudentsAdapter(
    private var userList: List<User> = emptyList(),
    private val onItemSelected: (User) -> Unit
): RecyclerView.Adapter<StudentsViewHolder>() {
    /**
     * Crea un nuevo ViewHolder inflando el diseño 'user_card'.
     * Este diseño se utiliza para representar a cada estudiante individualmente.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentsViewHolder {
        return StudentsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.user_card, parent, false)
        )
    }

    /**
     * Actualiza la colección de datos del adaptador y notifica al RecyclerView
     * para que redibuje los cambios en la pantalla.
     * * @param listUpdated La nueva lista de estudiantes obtenida del ViewModel.
     */
    fun updateList(listUpdated: List<User>) {
        userList = listUpdated
        notifyDataSetChanged()
    }

    /**
     * Une los datos de un estudiante específico con su ViewHolder correspondiente
     * según su posición en la lista.
     */
    override fun onBindViewHolder(
        holder: StudentsViewHolder,
        position: Int
    ) {
        holder.render(userList[position], onItemSelected)
    }

    /**
     * Retorna la cantidad total de elementos presentes en la lista actual.
     */
    override fun getItemCount(): Int {
        return userList.size
    }
}