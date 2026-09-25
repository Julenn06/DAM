package com.julen.socios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.julen.socios.databinding.BottomSheetIrpfBreakdownBinding
import com.julen.socios.util.CalculoComisiones
import java.util.Locale

class IrpfBreakdownBottomSheetDialog(
    private val rangoSemanaTexto: String,
    private val resumen: CalculoComisiones.ResumenCalculo
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetIrpfBreakdownBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetIrpfBreakdownBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvBreakdownRangoSemana.text = rangoSemanaTexto

        // Formatos de moneda
        binding.tvBreakdownNetoBig.text = String.format(Locale.getDefault(), "%.2f €", resumen.totalNeto)
        binding.tvBreakdownNetoSmall.text = String.format(Locale.getDefault(), "%.2f €", resumen.totalNeto)

        binding.tvBreakdownSociosCount.text =
            "Basado en ${resumen.totalSociosHechos} socios hechos (${resumen.totalSociosRegistrados} registrados)"

        binding.tvBreakdownSumaCuotas.text =
            String.format(Locale.getDefault(), "%.2f €", resumen.sumaColaboracionesBase)

        binding.tvBreakdownBaseX2.text =
            String.format(Locale.getDefault(), "+%.2f €", resumen.gananciasBaseX2)

        binding.tvBreakdownBonusValue.text =
            String.format(Locale.getDefault(), "+%.2f €", resumen.bonusSemanal)

        binding.tvBreakdownBonusLabel.text =
            "3. Bonus semanal por socios (${resumen.totalSociosHechos} hechos)"

        binding.tvBreakdownBruto.text =
            String.format(Locale.getDefault(), "%.2f €", resumen.totalBruto)

        binding.tvBreakdownIrpf.text =
            String.format(Locale.getDefault(), "-%.2f €", resumen.retencionIrpf)

        binding.btnCerrarBreakdown.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
