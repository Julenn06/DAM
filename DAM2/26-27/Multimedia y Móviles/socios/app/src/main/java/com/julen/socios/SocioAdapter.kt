package com.julen.socios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.julen.socios.databinding.ItemSocioBinding
import com.julen.socios.model.DiaSemana
import com.julen.socios.model.Socio
import com.julen.socios.util.CalculoComisiones
import java.util.Locale

class SocioAdapter(
    private val onToggleHecho: (Socio) -> Unit,
    private val onEdit: (Socio) -> Unit,
    private val onDelete: (Socio) -> Unit
) : ListAdapter<Socio, SocioAdapter.SocioViewHolder>(SocioDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val binding = ItemSocioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SocioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SocioViewHolder(private val binding: ItemSocioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(socio: Socio) {
            val context = binding.root.context

            // Importes
            val colaboracionFormatted = String.format(Locale.getDefault(), "%.0f €", socio.colaboracion)
            val baseX2 = CalculoComisiones.calcularBaseX2(socio.colaboracion)
            val baseX2Formatted = String.format(Locale.getDefault(), "+%.0f € base", baseX2)

            binding.tvColaboracionAmount.text = colaboracionFormatted
            binding.tvBaseX2Amount.text = baseX2Formatted

            // Día de la semana
            val diaSemana = DiaSemana.fromId(socio.diaSemanaId)
            binding.tvDiaBadge.text = diaSemana.nombreCompleto

            // Nombre
            if (socio.nombreSocio.isNotBlank()) {
                binding.tvNombreSocio.text = socio.nombreSocio
            } else {
                binding.tvNombreSocio.text = "Socio ${socio.colaboracion.toInt()}€"
            }

            // Notas
            if (socio.notas.isNotBlank()) {
                binding.tvNotas.visibility = View.VISIBLE
                binding.tvNotas.text = "Nota: ${socio.notas}"
            } else {
                binding.tvNotas.visibility = View.GONE
            }

            // Estado
            binding.switchHecho.setOnCheckedChangeListener(null)
            binding.switchHecho.isChecked = socio.hecho

            if (socio.hecho) {
                binding.tvEstadoTexto.text = "✓ Socio Conseguido (+${baseX2.toInt()}€ base)"
                binding.tvEstadoTexto.setTextColor(ContextCompat.getColor(context, R.color.primary))
            } else {
                binding.tvEstadoTexto.text = "⏳ No Hecho (Pendiente)"
                binding.tvEstadoTexto.setTextColor(ContextCompat.getColor(context, R.color.orange_pending))
            }

            // Listeners
            binding.switchHecho.setOnCheckedChangeListener { _, _ ->
                onToggleHecho(socio)
            }

            binding.btnEditar.setOnClickListener {
                onEdit(socio)
            }

            binding.btnEliminar.setOnClickListener {
                onDelete(socio)
            }
        }
    }

    class SocioDiffCallback : DiffUtil.ItemCallback<Socio>() {
        override fun areItemsTheSame(oldItem: Socio, newItem: Socio): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Socio, newItem: Socio): Boolean {
            return oldItem == newItem
        }
    }
}
