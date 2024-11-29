package org.example
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.Serializable


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Serializable
data class Artista(
    val id: Int,
    val nombre: String,
    val fechaNacimiento: String, // Puedes usar una librería como kotlinx-datetime para representar fechas
    val paisOrigen: String,
    val activo: Boolean
)
data class Album(
    val id: Int,
    val titulo: String,
    val fechaLanzamiento: String,
    val genero: String,
    val ventasTotales: Double,
    val artista: Artista
)

fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")
    val json = Json { prettyPrint = true }
    val archivoArtistas = File("src\\main\\kotlin\\artistas.json")
    val artistas = json.decodeFromString<List<Artista>>(archivoArtistas.readText())

    artistas.forEach { artista ->
        println("Artista: ${artista.nombre}")
    }
}