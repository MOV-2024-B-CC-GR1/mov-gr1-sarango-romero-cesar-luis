package com.example.deber02

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

            if (nombre.isNotEmpty() && artista.isNotEmpty() && precio != null && cantidad != null) {
                val album = Album(0, nombre, artista, precio, cantidad)
                val id = dbHelper.insertarAlbum(album)
                if (id != -1L) {
                    Toast.makeText(this, "Álbum insertado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al insertar álbum", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        buttonMostrar.setOnClickListener {
            val albums = dbHelper.obtenerAlbums()
            textViewAlbums.text = albums.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}, Artista: ${it.artista}, Precio: $${it.precio}, Cantidad: ${it.cantidad}" }

        }

        buttonActualizar.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            val nombre = editTextNombre.text.toString()
            val artista = editTextArtista.text.toString()
            val precio = editTextPrecio.text.toString().toDoubleOrNull()
            val cantidad = editTextCantidad.text.toString().toIntOrNull()

            if (id != null && nombre.isNotEmpty() && artista.isNotEmpty() && precio != null && cantidad != null) {
                val album = Album(id, nombre, artista, precio, cantidad)
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
    }
}