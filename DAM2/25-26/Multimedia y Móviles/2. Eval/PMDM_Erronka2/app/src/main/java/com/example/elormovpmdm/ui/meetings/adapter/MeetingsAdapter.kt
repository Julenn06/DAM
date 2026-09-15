package com.example.elormovpmdm.ui.meetings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.elormovpmdm.R
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.domain.model.Meeting

/**
 * Adaptador para mostrar la lista de reuniones.
 * * @property meetingList Lista de reuniones a mostrar.
 * @property onItemSelected Callback que se ejecuta al pulsar sobre una reunión.
 */
class MeetingsAdapter(
    private var meetingList: List<Meeting> = emptyList(),
    private val onItemSelected: (Meeting) -> Unit
): RecyclerView.Adapter<MeetingsViewHolder>() {

    /**
     * Infla el diseño de la celda (meeting_card) y crea el ViewHolder.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MeetingsViewHolder {
        return MeetingsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.meeting_card, parent, false)
        )
    }

    /**
     * Actualiza la lista de datos y refresca el RecyclerView.
     * @param listUpdated Nueva lista de reuniones.
     */
    fun updateList(listUpdated: List<Meeting>) {
        meetingList = listUpdated
        // Notifica que todo el conjunto de datos ha cambiado para redibujar la lista.
        notifyDataSetChanged()
    }

    /**
     * Vincula los datos de una reunión específica con su ViewHolder correspondiente.
     */
    override fun onBindViewHolder(
        holder: MeetingsViewHolder,
        position: Int
    ) {
        holder.render(meetingList[position], onItemSelected)
    }

    /**
     * Devuelve el número total de elementos en la lista.
     */
    override fun getItemCount(): Int {
        return meetingList.size
    }
}