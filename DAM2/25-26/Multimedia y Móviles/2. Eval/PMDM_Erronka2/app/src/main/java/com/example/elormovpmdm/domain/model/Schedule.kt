package com.example.elormovpmdm.domain.model

import java.net.IDN

data class Schedule(
    val id: Int,
    val dia: String,
    val hora: Int,
    val profe_id: Int,
    val modulos: Module
)