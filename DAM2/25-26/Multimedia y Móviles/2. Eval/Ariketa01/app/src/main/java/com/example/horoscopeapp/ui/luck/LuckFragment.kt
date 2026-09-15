package com.example.horoscopeapp.ui.luck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.horoscopeapp.R
import com.example.horoscopeapp.databinding.FragmentHoroscopeBinding
import com.example.horoscopeapp.databinding.FragmentLuckBinding
import dagger.hilt.android.AndroidEntryPoint

//Para que se puedan inyectar clases dentro de esta clase
@AndroidEntryPoint

class LuckFragment : Fragment() {

    private var _binding: FragmentLuckBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_luck, container, false)
        _binding = FragmentLuckBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}