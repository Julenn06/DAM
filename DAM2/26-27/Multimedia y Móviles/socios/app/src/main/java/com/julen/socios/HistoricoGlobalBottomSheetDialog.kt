package com.julen.socios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.julen.socios.databinding.BottomSheetHistoricoGlobalBinding
import com.julen.socios.util.CalculoComisiones
import java.util.Locale

class HistoricoGlobalBottomSheetDialog(
    private val resumenGlobal: CalculoComisiones.ResumenHistoricoGlobal,
    private val onSelectSemana: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetHistoricoGlobalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetHistoricoGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countSemanas = resumenGlobal.semanasHistoricas.size
        binding.tvGlobalSemanasCount.text = "Acumulado en $countSemanas ${if (countSemanas == 1) "semana registrada" else "semanas registradas"}"

        binding.tvGlobalIrpfTotal.text = String.format(Locale.getDefault(), "-%.2f €", resumenGlobal.totalIrpfRetenidoAcumulado)
        binding.tvGlobalNetoTotal.text = String.format(Locale.getDefault(), "+%.2f €", resumenGlobal.totalNetoAcumulado)
        binding.tvGlobalBonusTotal.text = String.format(Locale.getDefault(), "+%.2f €", resumenGlobal.totalBonusAcumulado)

        // Adapter para la lista de semanas
        val adapter = SemanaHistoricaAdapter { semanaKey ->
            onSelectSemana(semanaKey)
            dismiss()
        }

        binding.rvSemanasHistoricas.layoutManager = LinearLayoutManager(context)
        binding.rvSemanasHistoricas.adapter = adapter
        adapter.submitList(resumenGlobal.semanasHistoricas)

        binding.btnCerrarHistorico.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
