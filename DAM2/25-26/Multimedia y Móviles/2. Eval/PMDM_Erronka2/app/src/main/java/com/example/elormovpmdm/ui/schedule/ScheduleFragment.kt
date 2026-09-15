package com.example.elormovpmdm.ui.schedule

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.elormovpmdm.R
import com.example.elormovpmdm.databinding.FragmentSchedulesBinding
import com.example.elormovpmdm.domain.model.Schedule
import com.example.elormovpmdm.ui.schedule.userSchedule.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragmento encargado de visualizar el horario semanal del usuario.
 * Permite la navegación entre los días de la semana (Lunes a Viernes) y muestra
 * de forma dinámica los módulos correspondientes a cada franja horaria.
 */
@AndroidEntryPoint
class ScheduleFragment : Fragment() {
    private var _binding: FragmentSchedulesBinding? = null
    private val binding get() = _binding!!
    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private var schedules: List<Schedule> = emptyList()

    /**
     * Índice del día actual visualizado.
     * Se inicializa en 0 (Lunes).
     */
    private var currentIndex: Int = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchedulesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Lista de cadenas con los nombres de los días de la semana,
         * obtenida de forma perezosa (lazy) desde los recursos.
         */
        val daysOfWeek by lazy {
            listOf(
                ContextCompat.getString(requireContext(), R.string.monday),
                ContextCompat.getString(requireContext(), R.string.tuesday),
                ContextCompat.getString(requireContext(), R.string.wednesday),
                ContextCompat.getString(requireContext(), R.string.thursday),
                ContextCompat.getString(requireContext(), R.string.friday),
            )
        }
        initComponents(daysOfWeek)
        initUI(daysOfWeek)
    }

    /**
     * Configura los botones de navegación (atrás y adelante).
     * Implementa un sistema de carrusel infinito para recorrer los días de la semana.
     */
    private fun initComponents(daysOfWeek: List<String>) {
        binding.btnBack.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                paintSchedule(daysOfWeek, currentIndex)
            } else {
                currentIndex = daysOfWeek.size - 1
                paintSchedule(daysOfWeek, currentIndex)
            }

            binding.ivDay.text = daysOfWeek[currentIndex]
        }
        binding.btnForward.setOnClickListener {
            if (currentIndex < daysOfWeek.size - 1) {
                currentIndex++
                paintSchedule(daysOfWeek, currentIndex)
            } else {
                currentIndex = 0
                paintSchedule(daysOfWeek, currentIndex)
            }

            binding.ivDay.text = daysOfWeek[currentIndex]
        }
    }

    /**
     * Inicializa la interfaz de usuario observando el flujo de datos del ViewModel.
     * Actualiza el horario automáticamente cuando se detectan cambios en el StateFlow.
     */
    private fun initUI(daysOfWeek: List<String>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scheduleViewModel.schedules.collect { schedules ->
                    if (schedules.isNotEmpty()) {
                        updateList(schedules)
                        paintSchedule(daysOfWeek, currentIndex)
                    }
                }
            }
        }

        binding.ivDay.text = daysOfWeek[currentIndex]
    }

    /**
     * Actualiza la referencia local de la lista de horarios.
     */
    private fun updateList(listUpdated: List<Schedule>) {
        schedules = listUpdated
    }

    /**
     * Dibuja visualmente el horario en la UI para el día seleccionado.
     * Limpia los contenedores previos y asigna el nombre del módulo y su color
     * representativo a la franja horaria correspondiente (1ª a 6ª hora).
     */
    private fun paintSchedule(daysOfWeek: List<String>, index: Int) {
        val textViews = listOf(
            binding.tvFirstHour,
            binding.tvSecondHour,
            binding.tvThirdHour,
            binding.tvFourthHour,
            binding.tvFifthHour,
            binding.tvSixthHour
        )

        textViews.forEach {
            it.text = ""
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_dark))
        }

        schedules.forEach { schedule ->
            if (getDayIndex(schedule.dia) == index) {
                val targetTextView = when (schedule.hora) {
                    1 -> binding.tvFirstHour
                    2 -> binding.tvSecondHour
                    3 -> binding.tvThirdHour
                    4 -> binding.tvFourthHour
                    5 -> binding.tvFifthHour
                    6 -> binding.tvSixthHour
                    else -> null
                }

                targetTextView?.apply {
                    text = schedule.modulos.nombre
                    setBackgroundColor(getModuloColor(schedule.modulos.id))
                }

            }
        }
    }
    
    private fun getDayIndex(dayName: String): Int {
        return when (dayName.uppercase()) {
            "LUNES" -> 0
            "MARTES" -> 1
            "MIERCOLES" -> 2
            "JUEVES" -> 3
            "VIERNES" -> 4
            else -> 0   
        }
    }

    /**
     * Retorna el color específico asociado a un ID de módulo educativo.
     * @param moduloId Identificador único del módulo.
     * @return Valor entero del color resuelto desde los recursos.
     */
    private fun getModuloColor(moduloId: Int): Int {
        val colorRes = when(moduloId) {
            1 -> R.color.mod1
            2 -> R.color.mod2
            3 -> R.color.mod3
            4 -> R.color.mod4
            5 -> R.color.mod5
            6 -> R.color.mod6
            7 -> R.color.mod7
            8 -> R.color.mod8
            9 -> R.color.mod9
            10 -> R.color.mod10
            11 -> R.color.mod11
            12 -> R.color.mod12
            13 -> R.color.mod13
            14 -> R.color.mod14
            15 -> R.color.mod15
            16 -> R.color.mod16
            17 -> R.color.mod17
            18 -> R.color.mod18
            19 -> R.color.mod19
            else -> {R.color.white}
        }
        return ContextCompat.getColor(requireContext(), colorRes)
    }
}