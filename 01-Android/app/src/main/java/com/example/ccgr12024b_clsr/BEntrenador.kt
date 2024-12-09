package com.example.ccgr12024b_clsr

class BEntrenador(
    var id: Int,// Un entero para identificar al entrenador
    var nombre: String, // Una cadena para el nombre del entrenador
    var descripcion: String? // Una cadena opcional para una descripción
    ){
    override fun toString(): String {
        return "$nombre $descripcion" // Devuelve una cadena con el nombre y la descripción
    }
}