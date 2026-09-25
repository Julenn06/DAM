package com.julen.socios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.julen.socios.databinding.BottomSheetAddSocioBinding
import com.julen.socios.model.Socio

class AddSocioBottomSheetDialog(
    private val semanaKey: String,
    private val defaultDiaId: Int,
    private val socioToEdit: Socio? = null,
    private val onSave: (Socio) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddSocioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddSocioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCuotaChips()

        if (socioToEdit != null) {
            binding.tvTitleBottomSheet.text = "Editar Socio"
            binding.switchSocioHechoAdd.isChecked = socioToEdit.hecho
            binding.etNombreSocio.setText(socioToEdit.nombreSocio)
            binding.etNotas.setText(socioToEdit.notas)
            selectCuota(socioToEdit.colaboracion)
            selectDia(socioToEdit.diaSemanaId)
        } else {
            selectDia(defaultDiaId)
        }

        binding.btnCancelarAdd.setOnClickListener {
            dismiss()
        }

        binding.btnGuardarSocio.setOnClickListener {
            saveSocio()
        }
    }

    private fun setupCuotaChips() {
        binding.chipGroupCuotas.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.contains(R.id.chipOtro)) {
                binding.tilCustomAmount.visibility = View.VISIBLE
            } else {
                binding.tilCustomAmount.visibility = View.GONE
            }
        }
    }

    private fun selectCuota(amount: Double) {
        when (amount.toInt()) {
            10 -> binding.chip10.isChecked = true
            12 -> binding.chip12.isChecked = true
            15 -> binding.chip15.isChecked = true
            18 -> binding.chip18.isChecked = true
            20 -> binding.chip20.isChecked = true
            25 -> binding.chip25.isChecked = true
            30 -> binding.chip30.isChecked = true
            else -> {
                binding.chipOtro.isChecked = true
                binding.tilCustomAmount.visibility = View.VISIBLE
                binding.etCustomAmount.setText(amount.toString())
            }
        }
    }

    private fun selectDia(diaId: Int) {
        when (diaId) {
            1 -> binding.chipDiaLun.isChecked = true
            2 -> binding.chipDiaMar.isChecked = true
            3 -> binding.chipDiaMie.isChecked = true
            4 -> binding.chipDiaJue.isChecked = true
            5 -> binding.chipDiaVie.isChecked = true
        }
    }

    private fun getSelectedCuota(): Double? {
        val checkedId = binding.chipGroupCuotas.checkedChipId
        if (checkedId == R.id.chipOtro) {
            val text = binding.etCustomAmount.text?.toString()?.trim()
            return text?.toDoubleOrNull()
        }
        return when (checkedId) {
            R.id.chip10 -> 10.0
            R.id.chip12 -> 12.0
            R.id.chip15 -> 15.0
            R.id.chip18 -> 18.0
            R.id.chip20 -> 20.0
            R.id.chip25 -> 25.0
            R.id.chip30 -> 30.0
            else -> null
        }
    }

    private fun getSelectedDiaId(): Int {
        return when (binding.chipGroupDias.checkedChipId) {
            R.id.chipDiaLun -> 1
            R.id.chipDiaMar -> 2
            R.id.chipDiaMie -> 3
            R.id.chipDiaJue -> 4
            R.id.chipDiaVie -> 5
            else -> 1
        }
    }

    private fun saveSocio() {
        val cuota = getSelectedCuota()
        if (cuota == null || cuota <= 0) {
            Toast.makeText(context, "Por favor introduce un importe válido", Toast.LENGTH_SHORT).show()
            return
        }

        val diaId = getSelectedDiaId()
        val esHecho = binding.switchSocioHechoAdd.isChecked
        val nombre = binding.etNombreSocio.text?.toString()?.trim().orEmpty()
        val notas = binding.etNotas.text?.toString()?.trim().orEmpty()

        val socio = socioToEdit?.copy(
            colaboracion = cuota,
            hecho = esHecho,
            diaSemanaId = diaId,
            nombreSocio = nombre,
            notas = notas
        ) ?: Socio(
            colaboracion = cuota,
            hecho = esHecho,
            diaSemanaId = diaId,
            semanaKey = semanaKey,
            nombreSocio = nombre,
            notas = notas
        )

        onSave(socio)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
