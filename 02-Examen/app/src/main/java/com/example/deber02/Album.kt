package com.example.deber02

data class Album(
    val id: Int = 0,
    val nombre: String,
    val artista: String,
    val precio: Double,
    val cantidad: Int,
    val latitud: Double?,
    val longitud: Double?
)