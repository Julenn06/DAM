package com.julen.socios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.julen.socios.databinding.ItemSemanaHistoricaBinding
import com.julen.socios.util.CalculoComisiones
import java.util.Locale

class SemanaHistoricaAdapter(
    private val onSelectSemana: (String) -> Unit
) : ListAdapter<CalculoComisiones.SemanaHistoricaItem, SemanaHistoricaAdapter.SemanaViewHolder>(SemanaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SemanaViewHolder {
        val binding = ItemSemanaHistoricaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SemanaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SemanaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SemanaViewHolder(private val binding: ItemSemanaHistoricaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalculoComisiones.SemanaHistoricaItem) {
            val r = item.resumenSemana

            binding.tvSemanaItemTitulo.text = "Semana ${item.semanaKey}"
            binding.tvSemanaItemSociosCount.text =
                "${r.totalSociosHechos} socios hechos (+${r.bonusSemanal.toInt()}€ bonus)"

            binding.tvSemanaItemNeto.text = String.format(Locale.getDefault(), "+%.2f €", r.totalNeto)
            binding.tvSemanaItemIrpf.text = String.format(Locale.getDefault(), "IRPF: -%.2f €", r.retencionIrpf)

            binding.root.setOnClickListener {
                onSelectSemana(item.semanaKey)
            }
        }
    }

    class SemanaDiffCallback : DiffUtil.ItemCallback<CalculoComisiones.SemanaHistoricaItem>() {
        override fun areItemsTheSame(
            oldItem: CalculoComisiones.SemanaHistoricaItem,
            newItem: CalculoComisiones.SemanaHistoricaItem
        ): Boolean {
            return oldItem.semanaKey == newItem.semanaKey
        }

        override fun areContentsTheSame(
            oldItem: CalculoComisiones.SemanaHistoricaItem,
            newItem: CalculoComisiones.SemanaHistoricaItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}
