package com.julen.socios

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.julen.socios.databinding.BottomSheetAddSocioBinding
import com.julen.socios.model.Socio
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.HapticUtils
import java.util.Locale

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

        setupListeners()

        if (socioToEdit != null) {
            binding.tvTitleBottomSheet.text = "Editar Registro de Socio"
            binding.btnGuardarSocio.text = "Actualizar Socio"
            binding.btnGuardarSocio.setIconResource(R.drawable.ic_edit)

            binding.switchSocioHechoAdd.isChecked = socioToEdit.hecho
            binding.etNombreSocio.setText(socioToEdit.nombreSocio)
            binding.etNotas.setText(socioToEdit.notas)
            selectCuota(socioToEdit.colaboracion)
            selectDia(socioToEdit.diaSemanaId)
        } else {
            binding.tvTitleBottomSheet.text = "Añadir Nuevo Socio"
            binding.btnGuardarSocio.text = "Guardar Socio"
            binding.btnGuardarSocio.setIconResource(R.drawable.ic_add)
            selectDia(defaultDiaId)
        }

        updateLivePreview()

        binding.btnCancelarAdd.setOnClickListener { viewClick ->
            HapticUtils.performClick(viewClick)
            dismiss()
        }

        binding.btnGuardarSocio.setOnClickListener { viewClick ->
            HapticUtils.performConfirm(viewClick)
            saveSocio()
        }
    }

    private fun setupListeners() {
        binding.chipGroupCuotas.setOnCheckedStateChangeListener { _, checkedIds ->
            HapticUtils.performClick(binding.chipGroupCuotas)
            if (checkedIds.contains(R.id.chipOtro)) {
                binding.tilCustomAmount.visibility = View.VISIBLE
            } else {
                binding.tilCustomAmount.visibility = View.GONE
            }
            updateLivePreview()
        }

        binding.chipGroupDias.setOnCheckedStateChangeListener { _, _ ->
            HapticUtils.performClick(binding.chipGroupDias)
        }

        binding.switchSocioHechoAdd.setOnCheckedChangeListener { viewSwitch, _ ->
            HapticUtils.performToggle(viewSwitch)
            updateLivePreview()
        }

        binding.etCustomAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateLivePreview()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateLivePreview() {
        val cuota = getSelectedCuota() ?: 0.0
        val baseX2 = CalculoComisiones.calcularBaseX2(cuota)
        val isHecho = binding.switchSocioHechoAdd.isChecked

        binding.tvPreviewBaseX2.text = String.format(
            Locale.getDefault(),
            "+%.2f € (Cuota %.0f€ x2)",
            baseX2,
            cuota
        )

        val context = requireContext()
        if (isHecho) {
            binding.tvPreviewEstadoBadge.text = "✓ Conseguido"
            binding.tvPreviewEstadoBadge.setTextColor(ContextCompat.getColor(context, R.color.primary))
            binding.tvPreviewEstadoBadge.backgroundTintList = ContextCompat.getColorStateList(context, R.color.bg_light)
        } else {
            binding.tvPreviewEstadoBadge.text = "⏳ Pendiente"
            binding.tvPreviewEstadoBadge.setTextColor(ContextCompat.getColor(context, R.color.orange_pending))
            binding.tvPreviewEstadoBadge.backgroundTintList = ContextCompat.getColorStateList(context, R.color.bg_light)
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
            Toast.makeText(context, "Por favor introduce un importe de colaboración válido", Toast.LENGTH_SHORT).show()
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
