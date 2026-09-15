package com.example.elormovpmdm.ui.meetings.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elormovpmdm.R
import com.example.elormovpmdm.databinding.MeetingCardBinding
import com.example.elormovpmdm.domain.model.Meeting
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ViewHolder que gestiona la representación visual de una sola reunión.
 */
class MeetingsViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = MeetingCardBinding.bind(view)

    /**
     * Vincula los datos del modelo con las vistas de la tarjeta.
     * @param meeting Objeto con la información de la reunión.
     * @param onItemSelected Acción a ejecutar al pulsar la tarjeta.
     */
    fun render(meeting: Meeting, onItemSelected: (Meeting) -> Unit) {
        val studentName = meeting.usersByAlumnoId?.nombre
        val classroom = meeting.aula

        // Tratamiento de fechas mediante kotlinx-datetime
        val fullDate = meeting.fecha
        val instant = Instant.parse(fullDate)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
        val onlyDate = localDateTime.date
        val onlyTime = localDateTime.time

        binding.tvMeetingTitle.text = studentName
        binding.tvClassroom.text = classroom
        binding.tvDate.text = onlyDate.toString()
        binding.tvHour.text = onlyTime.toString()

        // Configura el evento de click en toda la tarjeta
        binding.root.setOnClickListener {
            onItemSelected(meeting)
        }

        /**
         * Lógica de semáforo visual:
         * Cambia el color de fondo de la tarjeta según el estado de la reunión.
         */
        binding.root.setCardBackgroundColor(
            if (meeting.estado.equals("aceptada", ignoreCase = true)) {
                ContextCompat.getColor(itemView.context, R.color.agreed)
            } else if (meeting.estado.equals("pendiente", ignoreCase = true)) {
                ContextCompat.getColor(itemView.context, R.color.pending)
            } else {
                ContextCompat.getColor(itemView.context, R.color.refused)
            }
        )
    }
}