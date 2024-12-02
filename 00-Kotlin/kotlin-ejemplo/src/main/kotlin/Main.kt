package org.example

import java.util.*

fun main() {
    imprimirNombre("Cesar Luis")
    println("El sueldo total es: ${calcularSueldo(10.00,15.00,20.00)}")

    calcularSueldo(10.00)
    calcularSueldo(10.00,15.00,20.00)

    calcularSueldo(10.00, bonoEspecial = 20.00)

    calcularSueldo(bonoEspecial = 20.00, sueldo=10.00, tasa=14.00)


}
fun calcularSueldo(
    sueldo: Double, // Parámetro requerido
    tasa: Double = 12.0, // Parámetro opcional con valor por defecto
    bonoEspecial: Double? = null // Parámetro opcional y nullable
): Double {
    // Calcula el sueldo teniendo en cuenta el bono especial si existe
    return if (bonoEspecial == null) {
        sueldo * 100 / tasa
    } else {
        sueldo * 100 / tasa + bonoEspecial
    }
}
fun imprimirNombre(nombre:String): Unit {
    println("Nombre: $nombre")
    fun otraFuncionAdentro(){
    }
}