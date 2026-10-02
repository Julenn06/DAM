package com.example.a1ebalazterketaapp.modelo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    var id: String = "",
    var titulo: String = "",
    var autor: String = "",
    var ano: Int = 0
) : Parcelable