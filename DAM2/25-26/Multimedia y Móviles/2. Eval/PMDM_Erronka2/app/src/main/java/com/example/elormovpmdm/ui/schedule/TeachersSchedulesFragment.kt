package com.example.elormovpmdm.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.elormovpmdm.databinding.FragmentTeachersSchedulesBinding
import com.example.elormovpmdm.domain.model.User
import com.example.elormovpmdm.ui.schedule.adapter.SchedulesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

/**
 * Fragmento encargado de mostrar el listado de profesores o usuarios para consultar sus horarios.
 * Utiliza un RecyclerView para listar los perfiles y permite la navegación hacia el horario
 * específico de un usuario seleccionado.
 */
@AndroidEntryPoint
class TeachersSchedulesFragment : Fragment() {

    private var _binding: FragmentTeachersSchedulesBinding? = null
    private val binding get() = _binding!!
    private val teacherScheduleViewModel: TeachersSchedulesViewModel by viewModels()
    private var users: List<User> = emptyList()
    private lateinit var schedulesAdapter: SchedulesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeachersSchedulesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * Limpia la referencia al binding cuando la vista se destruye para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initUI()
    }

    /**
     * Inicializa los componentes de la interfaz, configurando el RecyclerView
     * con su respectivo adaptador y administrador de diseño.
     */
    private fun initComponents() {
        schedulesAdapter = SchedulesAdapter(onItemSelected = { onItemSelected(it) })
        binding.rvSchedules.layoutManager = GridLayoutManager(context, 1)
        binding.rvSchedules.adapter = schedulesAdapter
    }

    /**
     * Gestiona la navegación al seleccionar un usuario de la lista.
     * Utiliza Safe Args para pasar el ID del usuario a la siguiente pantalla (UserScheduleActivity).
     * * @param user El objeto de tipo User seleccionado en el RecyclerView.
     */
    private fun onItemSelected(user: User) {
        findNavController().navigate(
            TeachersSchedulesFragmentDirections.actionTeachersSchedulesFragmentToUserScheduleActivity(user.id)
        )
    }

    /**
     * Configura la lógica de actualización de la UI observando el flujo de datos del ViewModel.
     * La recolección se realiza de forma segura respetando el ciclo de vida del fragmento.
     */
    private fun initUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                teacherScheduleViewModel.users.collect { users ->
                    if (users.isNotEmpty()) {
                        Log.i("GVA", "Profesores encontrados: ${users.size}")
                    }
                    schedulesAdapter.updateList(users)
                }
            }
        }
    }
}