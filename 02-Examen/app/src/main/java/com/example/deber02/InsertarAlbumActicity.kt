package com.example.deber02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InsertarAlbumActivity : AppCompatActivity() {
    private lateinit var dbHelper: AlbumDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = AlbumDbHelper(this)

        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextArtista = findViewById<EditText>(R.id.editTextArtista) // Cambiado a Artista
        val editTextPrecio = findViewById<EditText>(R.id.editTextPrecio)
        val editTextCantidad = findViewById<EditText>(R.id.editTextCantidad)
        val editTextLatitud = findViewById<EditText>(R.id.editTextLatitud)
        val editTextLongitud = findViewById<EditText>(R.id.editTextLongitud)
        val buttonGuardar = findViewById<Button>(R.id.buttonGuardarAlbum)
        val buttonMostrar = findViewById<Button>(R.id.buttonMostrarAlbums)
        val buttonActualizar = findViewById<Button>(R.id.buttonActualizarAlbum)
        val buttonEliminar = findViewById<Button>(R.id.buttonEliminarAlbum)
        val textViewAlbums = findViewById<TextView>(R.id.textViewAlbums)

        buttonGuardar.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val artista = editTextArtista.text.toString()
            val precio = editTextPrecio.text.toString().toDoubleOrNull()
            val cantidad = editTextCantidad.text.toString().toIntOrNull()
            val latitud = editTextLatitud.text.toString().toDoubleOrNull()
            val longitud = editTextLongitud.text.toString().toDoubleOrNull()

            if (nombre.isNotEmpty() && artista.isNotEmpty() && precio != null && cantidad != null && latitud != null && longitud != null) {
                val album = Album(0, nombre, artista, precio, cantidad, latitud, longitud)
                val id = dbHelper.insertarAlbum(album)
                if (id != -1L) {
                    Toast.makeText(this, "Álbum insertado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al insertar álbum", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        buttonMostrar.setOnClickListener {
            val albums = dbHelper.obtenerAlbums()
            textViewAlbums.text = albums.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}, Artista: ${it.artista}, Precio: $${it.precio}, Cantidad: ${it.cantidad}, Latitd: ${it.latitud}, Logitd: ${it.longitud}"}

        }

        buttonActualizar.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            val nombre = editTextNombre.text.toString()
            val artista = editTextArtista.text.toString()
            val precio = editTextPrecio.text.toString().toDoubleOrNull()
            val cantidad = editTextCantidad.text.toString().toIntOrNull()
            val latitud = editTextLatitud.text.toString().toDoubleOrNull()
            val longitud = editTextLongitud.text.toString().toDoubleOrNull()
            if (id != null && nombre.isNotEmpty() && artista.isNotEmpty() && precio != null && cantidad != null && latitud != null && longitud != null) {
                val album = Album(id, nombre, artista, precio, cantidad, latitud, longitud)
                val rowsUpdated = dbHelper.actualizarAlbum(id, album)
                if (rowsUpdated > 0) {
                    Toast.makeText(this, "Álbum actualizado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al actualizar álbum", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        buttonEliminar.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            if (id != null) {
                val rowsDeleted = dbHelper.eliminarAlbum(id)
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Álbum eliminado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar álbum", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa un ID válido", Toast.LENGTH_SHORT).show()
            }
        }


// Guardar datos con ubicación
        buttonGuardar.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val artista = editTextArtista.text.toString()
            val precio = editTextPrecio.text.toString().toDoubleOrNull()
            val cantidad = editTextCantidad.text.toString().toIntOrNull()
            val latitud = editTextLatitud.text.toString().toDoubleOrNull()
            val longitud = editTextLongitud.text.toString().toDoubleOrNull()

            if (nombre.isNotEmpty() && artista.isNotEmpty() && precio != null && cantidad != null) {
                val album = Album(0, nombre, artista, precio, cantidad, latitud, longitud)
                val id = dbHelper.insertarAlbum(album)
                if (id != -1L) {
                    Toast.makeText(this, "Álbum insertado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al insertar álbum", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        val buttonMapa = findViewById<Button>(R.id.buttonMapa)
        buttonMapa.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            if (id != null) {
                val album = dbHelper.obtenerAlbumPorId(id)
                if (album != null && album.latitud != null && album.longitud != null) {
                    val intent = Intent(this, MapaActivity::class.java)
                    intent.putExtra("LATITUD", album.latitud)
                    intent.putExtra("LONGITUD", album.longitud)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No hay coordenadas guardadas para este álbum", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingresa un ID válido", Toast.LENGTH_SHORT).show()
            }
        }


    }

}