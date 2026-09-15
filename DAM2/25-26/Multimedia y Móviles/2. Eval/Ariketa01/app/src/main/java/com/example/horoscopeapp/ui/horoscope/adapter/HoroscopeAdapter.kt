package com.example.horoscopeapp.ui.horoscope.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopeapp.R
import com.example.horoscopeapp.domain.model.HoroscopeInfo

class HoroscopeAdapter
    (private var horoscopeList:List<HoroscopeInfo>): RecyclerView.Adapter<HoroscopeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HoroscopeViewHolder {
        return HoroscopeViewHolder(
            LayoutInflater.
            from(parent.context).
            inflate(R.layout.item_horoscope, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: HoroscopeViewHolder,
        position: Int
    ) {
        holder.render(horoscopeList[position])
    }

    override fun getItemCount(): Int {
        return horoscopeList.size
    }
}