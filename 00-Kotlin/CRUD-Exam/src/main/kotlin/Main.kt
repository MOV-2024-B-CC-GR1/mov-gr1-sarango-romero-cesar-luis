package org.example
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Serializable
data class Artista(
    val id: Int,
    val nombre: String,
    @Serializable(with = LocalDateSerializer::class)
    val fechaNacimiento: LocalDate,
    val paisOrigen: String,
    val generoMusical: String,
    val activo: Boolean,
    val numeroPremisos: Int,
    val calificacion: Double,
    val albumesIds: List<Int> = emptyList()
)

@Serializable
data class Album(
    val id: Int,
    val titulo: String,
    @Serializable(with = LocalDateSerializer::class)
    val fechaLanzamiento: LocalDate,
    val genero: String,
    val ventasTotales: Double,
    val precioVenta: Double,
    val disponibleStreaming: Boolean,
    val numeroCanciones: Int,
    val artistaId: Int
)
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}

fun leerArtistas(): List<Artista> {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    val archivoArtistas = File("D:\\7mo semestre\\Aplicciones Mobiles\\mov-gr1-sarango-romero-cesar-luis\\00-Kotlin\\CRUD-Exam\\src\\main\\kotlin\\artistas.json")
    return if (archivoArtistas.exists()) {
        try {
            json.decodeFromString<List<Artista>>(archivoArtistas.readText())
        } catch (e: Exception) {
            println("Error al leer el archivo de artistas: ${e.message}")
            emptyList()
        }
    } else {
        println("Creando nuevo archivo de artistas...")
        archivoArtistas.writeText("[]")
        emptyList()
    }
}

fun guardarArtistas(artistas: List<Artista>) {
    val json = Json { prettyPrint = true }
    val archivoArtistas = File("D:\\7mo semestre\\Aplicciones Mobiles\\mov-gr1-sarango-romero-cesar-luis\\00-Kotlin\\CRUD-Exam\\src\\main\\kotlin\\artistas.json")
    try {
        archivoArtistas.writeText(json.encodeToString(artistas))
    } catch (e: Exception) {
        println("Error al guardar artistas: ${e.message}")
    }
}

fun leerAlbumes(): List<Album> {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    val archivoAlbumes = File("D:\\7mo semestre\\Aplicciones Mobiles\\mov-gr1-sarango-romero-cesar-luis\\00-Kotlin\\CRUD-Exam\\src\\main\\kotlin\\albumes.json")
    return if (archivoAlbumes.exists()) {
        try {
            json.decodeFromString<List<Album>>(archivoAlbumes.readText())
        } catch (e: Exception) {
            println("Error al leer el archivo de álbumes: ${e.message}")
            emptyList()
        }
    } else {
        println("Creando nuevo archivo de álbumes...")
        archivoAlbumes.writeText("[]")
        emptyList()
    }
}

fun guardarAlbumes(albumes: List<Album>) {
    val json = Json { prettyPrint = true }
    val archivoAlbumes = File("D:\\7mo semestre\\Aplicciones Mobiles\\mov-gr1-sarango-romero-cesar-luis\\00-Kotlin\\CRUD-Exam\\src\\main\\kotlin\\albumes.json")
    try {
        archivoAlbumes.writeText(json.encodeToString(albumes))
    } catch (e: Exception) {
        println("Error al guardar álbumes: ${e.message}")
    }
}


fun crearArtista(
    nombre: String,
    fechaNacimiento: LocalDate,
    paisOrigen: String,
    generoMusical: String,
    activo: Boolean,
    numeroPremisos: Int,
    calificacion: Double
): Artista {
    val artistas = leerArtistas()
    val nuevoId = (artistas.maxOfOrNull { it.id } ?: 0) + 1

    val nuevoArtista = Artista(
        id = nuevoId,
        nombre = nombre,
        fechaNacimiento = fechaNacimiento,
        paisOrigen = paisOrigen,
        generoMusical = generoMusical,
        activo = activo,
        numeroPremisos = numeroPremisos,
        calificacion = calificacion,
        albumesIds = emptyList()
    )

    guardarArtistas(artistas + nuevoArtista)
    return nuevoArtista
}

fun actualizarArtista(
    id: Int,
    nombre: String,
    fechaNacimiento: LocalDate,
    paisOrigen: String,
    generoMusical: String,
    activo: Boolean,
    numeroPremisos: Int,
    calificacion: Double
): Boolean {
    val artistas = leerArtistas()
    val artistaExistente = artistas.find { it.id == id } ?: return false

    val artistaActualizado = artistaExistente.copy(
        nombre = nombre,
        fechaNacimiento = fechaNacimiento,
        paisOrigen = paisOrigen,
        generoMusical = generoMusical,
        activo = activo,
        numeroPremisos = numeroPremisos,
        calificacion = calificacion
    )

    guardarArtistas(artistas.map { if (it.id == id) artistaActualizado else it })
    return true
}

fun eliminarArtista(id: Int): Boolean {
    val artistas = leerArtistas()
    val albumes = leerAlbumes()

    // Verificar si el artista tiene álbumes
    if (albumes.any { it.artistaId == id }) {
        throw IllegalStateException("No se puede eliminar el artista porque tiene álbumes asociados")
    }

    val nuevaLista = artistas.filter { it.id != id }
    if (nuevaLista.size == artistas.size) return false

    guardarArtistas(nuevaLista)
    return true
}

fun buscarArtistaPorId(id: Int): Artista? {
    return leerArtistas().find { it.id == id }
}

fun obtenerAlbumesPorArtista(artistaId: Int): List<Album> {
    return leerAlbumes().filter { it.artistaId == artistaId }
}

// OPERACIONES CRUD PARA ÁLBUMES

fun crearAlbum(
    titulo: String,
    fechaLanzamiento: LocalDate,
    genero: String,
    ventasTotales: Double,
    precioVenta: Double,
    disponibleStreaming: Boolean,
    numeroCanciones: Int,
    artistaId: Int
): Album {
    // Verificar que existe el artista
    val artista = buscarArtistaPorId(artistaId)
        ?: throw IllegalArgumentException("El artista con ID $artistaId no existe")

    val albumes = leerAlbumes()
    val nuevoId = (albumes.maxOfOrNull { it.id } ?: 0) + 1

    val nuevoAlbum = Album(
        id = nuevoId,
        titulo = titulo,
        fechaLanzamiento = fechaLanzamiento,
        genero = genero,
        ventasTotales = ventasTotales,
        precioVenta = precioVenta,
        disponibleStreaming = disponibleStreaming,
        numeroCanciones = numeroCanciones,
        artistaId = artistaId
    )

    // Actualizar la lista de álbumes del artista
    val artistaActualizado = artista.copy(
        albumesIds = artista.albumesIds + nuevoId
    )

    // Guardar ambas entidades
    guardarAlbumes(albumes + nuevoAlbum)
    actualizarArtista(
        artistaActualizado.id,
        artistaActualizado.nombre,
        artistaActualizado.fechaNacimiento,
        artistaActualizado.paisOrigen,
        artistaActualizado.generoMusical,
        artistaActualizado.activo,
        artistaActualizado.numeroPremisos,
        artistaActualizado.calificacion
    )

    return nuevoAlbum
}

fun actualizarAlbum(
    id: Int,
    titulo: String,
    fechaLanzamiento: LocalDate,
    genero: String,
    ventasTotales: Double,
    precioVenta: Double,
    disponibleStreaming: Boolean,
    numeroCanciones: Int,
    artistaId: Int
): Boolean {
    val albumes = leerAlbumes()
    val albumExistente = albumes.find { it.id == id } ?: return false

    // Si se está cambiando el artista, verificar que el nuevo artista existe
    if (albumExistente.artistaId != artistaId) {
        buscarArtistaPorId(artistaId) ?: throw IllegalArgumentException("El artista con ID $artistaId no existe")
    }

    val albumActualizado = albumExistente.copy(
        titulo = titulo,
        fechaLanzamiento = fechaLanzamiento,
        genero = genero,
        ventasTotales = ventasTotales,
        precioVenta = precioVenta,
        disponibleStreaming = disponibleStreaming,
        numeroCanciones = numeroCanciones,
        artistaId = artistaId
    )

    guardarAlbumes(albumes.map { if (it.id == id) albumActualizado else it })
    return true
}

fun eliminarAlbum(id: Int): Boolean {
    val albumes = leerAlbumes()
    val album = albumes.find { it.id == id } ?: return false

    // Actualizar la lista de álbumes del artista
    val artista = buscarArtistaPorId(album.artistaId)
    if (artista != null) {
        val artistaActualizado = artista.copy(
            albumesIds = artista.albumesIds - id
        )
        actualizarArtista(
            artistaActualizado.id,
            artistaActualizado.nombre,
            artistaActualizado.fechaNacimiento,
            artistaActualizado.paisOrigen,
            artistaActualizado.generoMusical,
            artistaActualizado.activo,
            artistaActualizado.numeroPremisos,
            artistaActualizado.calificacion
        )
    }

    guardarAlbumes(albumes.filter { it.id != id })
    return true
}

fun buscarAlbumPorId(id: Int): Album? {
    return leerAlbumes().find { it.id == id }
}

fun main() {
    while (true) {
        println("\n=== SISTEMA DE GESTIÓN DE ARTISTAS Y ÁLBUMES ===")
        println("1. Gestionar Artistas")
        println("2. Gestionar Álbumes")
        println("3. Salir")
        print("Seleccione una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> menuArtistas()
            2 -> menuAlbumes()
            3 -> break
            else -> println("Opción inválida")
        }
    }
}

fun menuArtistas() {
    while (true) {
        println("\n=== GESTIÓN DE ARTISTAS ===")
        println("1. Crear artista")
        println("2. Buscar artista")
        println("3. Actualizar artista")
        println("4. Eliminar artista")
        println("5. Ver álbumes de artista")
        println("6. Volver al menú principal")
        print("Seleccione una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> crearNuevoArtista()
            2 -> buscarArtista()
            3 -> actualizarArtistaExistente()
            4 -> eliminarArtistaExistente()
            5 -> verAlbumesArtista()
            6 -> break
            else -> println("Opción inválida")
        }
    }
}

fun crearNuevoArtista() {
    try {
        println("Ingrese los datos del artista:")
        print("Nombre: ")
        val nombre = readLine() ?: throw IllegalArgumentException("Nombre inválido")
        print("Fecha de nacimiento (YYYY-MM-DD): ")
        val fechaNacimiento = LocalDate.parse(readLine() ?: throw IllegalArgumentException("Fecha inválida"))
        print("País de origen: ")
        val paisOrigen = readLine() ?: throw IllegalArgumentException("País inválido")
        print("Género musical: ")
        val generoMusical = readLine() ?: throw IllegalArgumentException("Género inválido")
        print("Activo (true/false): ")
        val activo = readLine()?.toBooleanStrictOrNull() ?: throw IllegalArgumentException("Estado inválido")
        print("Número de premios: ")
        val numeroPremisos = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Número de premios inválido")
        print("Calificación: ")
        val calificacion = readLine()?.toDoubleOrNull() ?: throw IllegalArgumentException("Calificación inválida")

        val artista = crearArtista(nombre, fechaNacimiento, paisOrigen, generoMusical, activo, numeroPremisos, calificacion)
        println("Artista creado exitosamente con ID: ${artista.id}")
    } catch (e: Exception) {
        println("Error al crear artista: ${e.message}")
    }
}

fun buscarArtista() {
    print("Ingrese el ID del artista: ")
    val id = readLine()?.toIntOrNull()
    if (id == null) {
        println("ID inválido")
        return
    }

    val artista = buscarArtistaPorId(id)
    if (artista != null) {
        println("Artista encontrado:")
        println("ID: ${artista.id}")
        println("Nombre: ${artista.nombre}")
        println("Fecha de nacimiento: ${artista.fechaNacimiento}")
        println("País: ${artista.paisOrigen}")
        println("Género: ${artista.generoMusical}")
        println("Activo: ${artista.activo}")
        println("Premios: ${artista.numeroPremisos}")
        println("Calificación: ${artista.calificacion}")
    } else {
        println("Artista no encontrado")
    }
}

fun actualizarArtistaExistente() {
    try {
        print("Ingrese el ID del artista a actualizar: ")
        val id = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("ID inválido")

        val artistaExistente = buscarArtistaPorId(id) ?: throw IllegalArgumentException("Artista no encontrado")

        println("Ingrese los nuevos datos (presione Enter para mantener el valor actual):")

        print("Nombre (${artistaExistente.nombre}): ")
        val nombre = readLine()?.takeIf { it.isNotEmpty() } ?: artistaExistente.nombre

        print("Fecha de nacimiento (${artistaExistente.fechaNacimiento}): ")
        val fechaNacimiento = readLine()?.takeIf { it.isNotEmpty() }?.let { LocalDate.parse(it) } ?: artistaExistente.fechaNacimiento

        print("País de origen (${artistaExistente.paisOrigen}): ")
        val paisOrigen = readLine()?.takeIf { it.isNotEmpty() } ?: artistaExistente.paisOrigen

        print("Género musical (${artistaExistente.generoMusical}): ")
        val generoMusical = readLine()?.takeIf { it.isNotEmpty() } ?: artistaExistente.generoMusical

        print("Activo (${artistaExistente.activo}): ")
        val activo = readLine()?.takeIf { it.isNotEmpty() }?.toBoolean() ?: artistaExistente.activo

        print("Número de premios (${artistaExistente.numeroPremisos}): ")
        val numeroPremisos = readLine()?.takeIf { it.isNotEmpty() }?.toInt() ?: artistaExistente.numeroPremisos

        print("Calificación (${artistaExistente.calificacion}): ")
        val calificacion = readLine()?.takeIf { it.isNotEmpty() }?.toDouble() ?: artistaExistente.calificacion

        if (actualizarArtista(id, nombre, fechaNacimiento, paisOrigen, generoMusical, activo, numeroPremisos, calificacion)) {
            println("Artista actualizado exitosamente")
        } else {
            println("No se pudo actualizar el artista")
        }
    } catch (e: Exception) {
        println("Error al actualizar artista: ${e.message}")
    }
}

fun eliminarArtistaExistente() {
    print("Ingrese el ID del artista a eliminar: ")
    val id = readLine()?.toIntOrNull()
    if (id == null) {
        println("ID inválido")
        return
    }

    try {
        if (eliminarArtista(id)) {
            println("Artista eliminado exitosamente")
        } else {
            println("No se encontró el artista")
        }
    } catch (e: IllegalStateException) {
        println("Error: ${e.message}")
    }
}

fun verAlbumesArtista() {
    print("Ingrese el ID del artista: ")
    val id = readLine()?.toIntOrNull()
    if (id == null) {
        println("ID inválido")
        return
    }

    val artista = buscarArtistaPorId(id)
    if (artista == null) {
        println("Artista no encontrado")
        return
    }

    val albumes = obtenerAlbumesPorArtista(id)
    if (albumes.isEmpty()) {
        println("El artista no tiene álbumes registrados")
        return
    }

    println("Álbumes de ${artista.nombre}:")
    albumes.forEach { album ->
        println("\nID: ${album.id}")
        println("Título: ${album.titulo}")
        println("Fecha de lanzamiento: ${album.fechaLanzamiento}")
        println("Género: ${album.genero}")
        println("Ventas totales: ${album.ventasTotales}")
        println("Precio: ${album.precioVenta}")
        println("Streaming: ${album.disponibleStreaming}")
        println("Número de canciones: ${album.numeroCanciones}")
    }
}

fun menuAlbumes() {
    while (true) {
        println("\n=== GESTIÓN DE ÁLBUMES ===")
        println("1. Crear álbum")
        println("2. Buscar álbum")
        println("3. Actualizar álbum")
        println("4. Eliminar álbum")
        println("5. Ver álbumes por artista")
        println("6. Volver al menú principal")
        print("Seleccione una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> crearNuevoAlbum()
            2 -> buscarAlbum()
            3 -> actualizarAlbumExistente()
            4 -> eliminarAlbumExistente()
            5 -> verAlbumesArtista()
            6 -> break
            else -> println("Opción inválida")
        }
    }
}

fun crearNuevoAlbum() {
    try {
        println("Ingrese los datos del álbum:")
        print("Título: ")
        val titulo = readLine() ?: throw IllegalArgumentException("Título inválido")
        print("Fecha de lanzamiento (YYYY-MM-DD): ")
        val fechaLanzamiento = LocalDate.parse(readLine() ?: throw IllegalArgumentException("Fecha inválida"))
        print("Género: ")
        val genero = readLine() ?: throw IllegalArgumentException("Género inválido")
        print("Ventas totales: ")
        val ventasTotales = readLine()?.toDoubleOrNull() ?: throw IllegalArgumentException("Ventas inválidas")
        print("Precio de venta: ")
        val precioVenta = readLine()?.toDoubleOrNull() ?: throw IllegalArgumentException("Precio inválido")
        print("Disponible en streaming (true/false): ")
        val disponibleStreaming = readLine()?.toBooleanStrictOrNull() ?: throw IllegalArgumentException("Disponibilidad inválida")
        print("Número de canciones: ")
        val numeroCanciones = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Número de canciones inválido")
        print("ID del artista: ")
        val artistaId = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("ID de artista inválido")

        val album = crearAlbum(titulo, fechaLanzamiento, genero, ventasTotales, precioVenta,
            disponibleStreaming, numeroCanciones, artistaId)
        println("Álbum creado exitosamente con ID: ${album.id}")
    } catch (e: Exception) {
        println("Error al crear álbum: ${e.message}")
    }
}

fun buscarAlbum() {
    print("Ingrese el ID del álbum: ")
    val id = readLine()?.toIntOrNull()
    if (id == null) {
        println("ID inválido")
        return
    }

    val album = buscarAlbumPorId(id)
    if (album != null) {
        println("Álbum encontrado:")
        println("ID: ${album.id}")
        println("Título: ${album.titulo}")
        println("Fecha de lanzamiento: ${album.fechaLanzamiento}")
        println("Género: ${album.genero}")
        println("Ventas totales: ${album.ventasTotales}")
        println("Precio: ${album.precioVenta}")
        println("Streaming: ${album.disponibleStreaming}")
        println("Número de canciones: ${album.numeroCanciones}")
        println("ID del artista: ${album.artistaId}")
    } else {
        println("Álbum no encontrado")
    }
}

fun actualizarAlbumExistente() {
    try {
        print("Ingrese el ID del álbum a actualizar: ")
        val id = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("ID inválido")

        val albumExistente = buscarAlbumPorId(id) ?: throw IllegalArgumentException("Álbum no encontrado")

        println("Ingrese los nuevos datos (presione Enter para mantener el valor actual):")

        print("Título (${albumExistente.titulo}): ")
        val titulo = readLine()?.takeIf { it.isNotEmpty() } ?: albumExistente.titulo

        print("Fecha de lanzamiento (${albumExistente.fechaLanzamiento}): ")
        val fechaLanzamiento = readLine()?.takeIf { it.isNotEmpty() }?.let { LocalDate.parse(it) } ?: albumExistente.fechaLanzamiento

        print("Género (${albumExistente.genero}): ")
        val genero = readLine()?.takeIf { it.isNotEmpty() } ?: albumExistente.genero

        print("Ventas totales (${albumExistente.ventasTotales}): ")
        val ventasTotales = readLine()?.takeIf { it.isNotEmpty() }?.toDouble() ?: albumExistente.ventasTotales

        print("Precio de venta (${albumExistente.precioVenta}): ")
        val precioVenta = readLine()?.takeIf { it.isNotEmpty() }?.toDouble() ?: albumExistente.precioVenta

        print("Disponible en streaming (${albumExistente.disponibleStreaming}): ")
        val disponibleStreaming = readLine()?.takeIf { it.isNotEmpty() }?.toBoolean() ?: albumExistente.disponibleStreaming

        print("Número de canciones (${albumExistente.numeroCanciones}): ")
        val numeroCanciones = readLine()?.takeIf { it.isNotEmpty() }?.toInt() ?: albumExistente.numeroCanciones

        print("ID del artista (${albumExistente.artistaId}): ")
        val artistaId = readLine()?.takeIf { it.isNotEmpty() }?.toInt() ?: albumExistente.artistaId

        if (actualizarAlbum(id, titulo, fechaLanzamiento, genero, ventasTotales, precioVenta,
                disponibleStreaming, numeroCanciones, artistaId)) {
            println("Álbum actualizado exitosamente")
        } else {
            println("No se pudo actualizar el álbum")
        }
    } catch (e: Exception) {
        println("Error al actualizar álbum: ${e.message}")
    }
}

fun eliminarAlbumExistente() {
    print("Ingrese el ID del álbum a eliminar: ")
    val id = readLine()?.toIntOrNull()
    if (id == null) {
        println("ID inválido")
        return
    }

    if (eliminarAlbum(id)) {
        println("Álbum eliminado exitosamente")
    } else {
        println("No se encontró el álbum")
    }
}


