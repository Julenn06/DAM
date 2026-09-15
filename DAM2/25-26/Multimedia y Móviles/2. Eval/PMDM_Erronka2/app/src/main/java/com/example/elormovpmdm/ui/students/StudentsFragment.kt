package com.example.elormovpmdm.ui.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.elormovpmdm.databinding.FragmentStudentsBinding
import com.example.elormovpmdm.databinding.StudentBottomsheetlayoutBinding
import com.example.elormovpmdm.domain.model.User
import com.example.elormovpmdm.ui.students.adapter.StudentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragmento encargado de mostrar el listado de estudiantes (alumnos) para el perfil de profesor.
 * Gestiona la visualización en un RecyclerView y el despliegue de información detallada
 * mediante un componente BottomSheet.
 */
@AndroidEntryPoint
class StudentsFragment : Fragment() {
    
    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private val studentsViewModel: StudentsViewModel by viewModels()
    private lateinit var studentsAdapter: StudentsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * Libera la referencia al binding al destruir la vista para evitar fugas de memoria.
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
     * Inicializa los componentes de la interfaz, configurando el adaptador del listado
     * y estableciendo un diseño de cuadrícula (GridLayout) de una sola columna.
     */
    private fun initComponents() {
        studentsAdapter = StudentsAdapter(onItemSelected = { onItemSelected(it) })
        binding.rvStudents.layoutManager = GridLayoutManager(context, 1)
        binding.rvStudents.adapter = studentsAdapter
    }

    /**
     * Gestiona el evento de selección de un estudiante.
     * Infla un BottomSheetDialog dinámicamente para mostrar el perfil completo del alumno
     * seleccionado (ID, dirección, teléfono, email, etc.).
     * * @param user El objeto de tipo User con los datos del alumno pulsado.
     */
    private fun onItemSelected(user: User) {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val sheetBinding = StudentBottomsheetlayoutBinding.inflate(layoutInflater)
        
        val name: String = user.nombre
        val surname: String = user.apellidos
        
        sheetBinding.userName.text = "$name $surname"
        sheetBinding.tvUserName.text = user.username
        sheetBinding.userID.text = user.id.toString()
        sheetBinding.userAddress.text = user.direccion
        sheetBinding.userPhone.text = user.telefono1
        sheetBinding.userEmail.text = user.email
        
        dialog.setContentView(sheetBinding.root)
        dialog.show()
    }

    /**
     * Configura la observación del StateFlow del ViewModel dentro del ciclo de vida del fragmento.
     * Actualiza el adaptador cada vez que la lista de usuarios emite nuevos datos.
     */
    private fun initUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                studentsViewModel.users.collect {
                    studentsAdapter.updateList(it)
                }
            }
        }
    }
}