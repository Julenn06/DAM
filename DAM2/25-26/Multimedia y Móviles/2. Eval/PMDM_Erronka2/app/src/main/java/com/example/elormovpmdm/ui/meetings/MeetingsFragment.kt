package com.example.elormovpmdm.ui.meetings

import android.R as androidR
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.elormovpmdm.databinding.AddMeetingDialogBinding
import com.example.elormovpmdm.databinding.FragmentMeetingsBinding
import com.example.elormovpmdm.databinding.MeetingInformationBottomsheetlayoutBinding
import com.example.elormovpmdm.domain.model.Center
import com.example.elormovpmdm.domain.model.Meeting
import com.example.elormovpmdm.domain.model.User
import com.example.elormovpmdm.ui.meetings.adapter.MeetingsAdapter
import com.example.elormovpmdm.ui.meetings.addAdapter.AddDialogAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragmento para la gestión de reuniones (Listado y Creación).
 * Integra componentes de selección de fecha, hora y visualización de mapas.
 */
@AndroidEntryPoint
class MeetingsFragment : Fragment() {

    private var _binding: FragmentMeetingsBinding? = null
    private val binding get() = _binding!!
    private var sheetBinding: MeetingInformationBottomsheetlayoutBinding? = null
    private val meetingsViewModel: MeetingsViewModel by viewModels()
    private lateinit var meetingsAdapter: MeetingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initUI()
    }

    /**
     * Configura el RecyclerView principal y el botón de acción para añadir reuniones.
     */
    private fun initComponents() {
        meetingsAdapter = MeetingsAdapter(onItemSelected = { onItemSelected(it) })
        binding.rvMeetins.layoutManager = GridLayoutManager(context, 1)
        binding.rvMeetins.adapter = meetingsAdapter

        binding.btnAdd.setOnClickListener {
            initDialog()
        }
        
        sheetBinding = MeetingInformationBottomsheetlayoutBinding.inflate(layoutInflater)
    }

    /**
     * Despliega un BottomSheetDialog con el detalle de la reunión.
     * Incluye un mapa interactivo cargando las coordenadas del centro asociado.
     */
    private fun onItemSelected(meeting: Meeting) {
        sheetBinding = null

        // Crear nuevo binding cada vez
        sheetBinding = MeetingInformationBottomsheetlayoutBinding.inflate(layoutInflater) // Usa el nombre correcto de tu binding

        val dialog = BottomSheetDialog(requireContext())

        initBottomDialogUI(meeting)
        initBottomDialogComponents(meeting, dialog)

        dialog.setContentView(sheetBinding!!.root)

        // Limpiar cuando se cierra el diálogo
        dialog.setOnDismissListener {
            sheetBinding?.map?.onDetach()
            sheetBinding = null
        }

        dialog.show()
    }
    
    private fun initBottomDialogComponents(meeting: Meeting, dialog: com.google.android.material.bottomsheet.BottomSheetDialog) {
        sheetBinding!!.btnConfirm.setOnClickListener { 
            meetingsViewModel.updateMeetingStatus(meeting.idReunion, "aceptada")
            Toast.makeText(requireContext(), "Reunion aceptada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        sheetBinding!!.btnCancell.setOnClickListener { 
            meetingsViewModel.updateMeetingStatus(meeting.idReunion, "denegada")
            Toast.makeText(requireContext(), "Reunion rechazada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }
    
    private fun initBottomDialogUI(meeting: Meeting) {
        if (meeting.estado.equals("aceptada", ignoreCase = true)) {
            sheetBinding!!.clConfirmation.visibility = View.GONE
        } else if (meeting.estado.equals("pendiente", ignoreCase = true)) {
            if (meetingsViewModel.user!!.tipoId == 3) {
                sheetBinding!!.clConfirmation.visibility = View.VISIBLE
            } else {
                sheetBinding!!.clConfirmation.visibility = View.GONE
            }
        }
        
        val center: Center? = meetingsViewModel.centers.value.find {
            it.CCEN == meeting.idCentro
        }

        if (center == null) {
            Toast.makeText(requireContext(), "Centro no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Parseo de fecha ISO 8601 a componentes locales
        val instant = Instant.parse(meeting.fecha)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        sheetBinding!!.meetingDate.text = localDateTime.date.toString()
        sheetBinding!!.meetingHour.text = localDateTime.time.toString()
        sheetBinding!!.center.text = center.NOM
        sheetBinding!!.meetingClassroom.text = meeting.aula ?: "Sin aula asignada"

        // Configuración de osmdroid para mostrar la ubicación
        Configuration.getInstance().userAgentValue = "com.example.elormovpmdm"
        sheetBinding!!.map.setTileSource(TileSourceFactory.MAPNIK)
        sheetBinding!!.map.setMultiTouchControls(true)

        val startPoint = GeoPoint(center.LONGITUD.toDouble(), center.LATITUD.toDouble())
        val startMarker = Marker(sheetBinding!!.map)
        val mapController = sheetBinding!!.map.controller
        mapController?.setZoom(15.0)

        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.position = startPoint
        startMarker.title = center.NOM
        sheetBinding!!.map.overlays.add(startMarker)
        mapController.setCenter(startPoint)
    }

    /**
     * Observa el StateFlow de reuniones en el ViewModel para actualizar el adaptador.
     */
    private fun initUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                meetingsViewModel.meetings.collect {
                    meetingsAdapter.updateList(it)
                }
            }
        }
    }

    /**
     * Gestiona la lógica de creación de una nueva reunión mediante un diálogo personalizado.
     * Incluye validación de campos, selección de usuarios y formateo de fecha ISO.
     */
    private fun initDialog() {
        val dialogBinding = AddMeetingDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Añadir reunion")
            .setView(dialogBinding.root)
            .create()

        var selectedUser: User? = null

        // Adaptador para la lista de usuarios dentro del diálogo
        val addDialogAdapter = AddDialogAdapter(onMeetingUserSelected = { user -> selectedUser = user })
        dialogBinding.rvUsers.layoutManager = GridLayoutManager(context, 1)
        dialogBinding.rvUsers.adapter = addDialogAdapter
        addDialogAdapter.updateList(meetingsViewModel.users.value)

        // Configuración de Spinner para centros
        val centerNames = meetingsViewModel.centers.value.map { it.NOM }
        val centersAdapter = ArrayAdapter(requireContext(), androidR.layout.simple_spinner_item, centerNames)
        dialogBinding.autoCompleteTextView.setAdapter(centersAdapter)

        // Listeners para selección de Fecha y Hora con Material Pickers
        dialogBinding.etDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener { timestamp ->
                dialogBinding.etDate.setText(SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(timestamp)))
            }
        }

        dialogBinding.etHour.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setHour(0).setMinute(0).build()
            timePicker.show(childFragmentManager, "timePicker")
            timePicker.addOnPositiveButtonClickListener {
                dialogBinding.etHour.setText(String.format("%02d:%02d", timePicker.hour, timePicker.minute))
            }
        }

        dialogBinding.btnAdd.setOnClickListener {
            val selectedCenter = meetingsViewModel.centers.value.find {
                it.NOM == dialogBinding.autoCompleteTextView.text.toString()
            }?.CCEN ?: ""
            // Formateo ISO 8601 para compatibilidad con backend
            val dateTimeString = "${dialogBinding.etDate.text.toString().replace("/", "-")}T${dialogBinding.etHour.text}:00.00Z"
            val title: String = dialogBinding.etTitle.text.toString()
            val aula: String = dialogBinding.etClassroom.text.toString()
            // Lógica de validación
            if (selectedUser == null || selectedCenter.isEmpty() || dateTimeString.isEmpty() || title.isEmpty() || aula.isEmpty()) {
                Toast.makeText(requireContext(), "Faltan datos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val request = Meeting(
                titulo = title,
                aula = aula,
                fecha = dateTimeString,
                estado = if (meetingsViewModel.user!!.tipoId == 4) "pendiente" else "aceptada",
                idCentro = selectedCenter,
                alumnoId = selectedUser!!.id,
                profesorId = meetingsViewModel.user!!.id
            )

            lifecycleScope.launch {
                meetingsViewModel.createMeeting(request)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}