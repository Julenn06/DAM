package com.example.horoscopeapp.ui.horoscope.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopeapp.databinding.ItemHoroscopeBinding
import com.example.horoscopeapp.domain.model.HoroscopeInfo

class HoroscopeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemHoroscopeBinding.bind(view)
    fun render(horoscope: HoroscopeInfo){
        binding.tvHorscope.setText(horoscope.title)
        binding.ivHoroscope.setImageResource(horoscope.image)
    }
}