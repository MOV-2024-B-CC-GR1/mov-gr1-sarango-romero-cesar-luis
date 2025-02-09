package com.example.deber02

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlbumDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_ALBUMS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_ARTISTA TEXT,
                $COLUMN_PRECIO REAL,
                $COLUMN_CANTIDAD INTEGER
            );
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUMS")
        onCreate(db)
    }

    // Insertar un álbum en la base de datos
    fun insertarAlbum(album: Album): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, album.nombre)
            put(COLUMN_ARTISTA, album.artista)
            put(COLUMN_PRECIO, album.precio)
            put(COLUMN_CANTIDAD, album.cantidad)
        }
        return db.insert(TABLE_ALBUMS, null, values)
    }

    // Obtener todos los álbumes de la base de datos
    fun obtenerAlbums(): List<Album> {
        val albums = mutableListOf<Album>()
        val db = readableDatabase

        // Define las columnas que quieres obtener
        val columnas = arrayOf(
            COLUMN_ID,
            COLUMN_NOMBRE,
            COLUMN_ARTISTA,
            COLUMN_PRECIO,
            COLUMN_CANTIDAD
        )

        val cursor = db.query(
            TABLE_ALBUMS,
            columnas,
            null,
            null,
            null,
            null,
            null
        )

        cursor.use { // Esto asegura que el cursor se cierre automáticamente
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                    val artista = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTISTA))
                    val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRECIO))
                    val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD))

                    albums.add(Album(id, nombre, artista, precio, cantidad))
                } while (cursor.moveToNext())
            }
        }

        return albums
    }

    // Actualizar los detalles de un álbum en la base de datos
    fun actualizarAlbum(id: Int, album: Album): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, album.nombre)
            put(COLUMN_ARTISTA, album.artista)
            put(COLUMN_PRECIO, album.precio)
            put(COLUMN_CANTIDAD, album.cantidad)
        }
        return db.update(TABLE_ALBUMS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Eliminar un álbum de la base de datos
    fun eliminarAlbum(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_ALBUMS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    companion object {
        const val DATABASE_NAME = "albums.db"
        const val DATABASE_VERSION = 1
        const val TABLE_ALBUMS = "albums"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_ARTISTA = "artista"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_CANTIDAD = "cantidad"
    }
}

