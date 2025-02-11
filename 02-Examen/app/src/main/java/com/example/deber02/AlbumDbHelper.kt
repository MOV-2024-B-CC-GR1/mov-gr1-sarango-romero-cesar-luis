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
                $COLUMN_CANTIDAD INTEGER,
                $COLUMN_LATITUD REAL,
                $COLUMN_LONGITUD REAL
            );
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_ALBUMS ADD COLUMN $COLUMN_LATITUD REAL")
            db?.execSQL("ALTER TABLE $TABLE_ALBUMS ADD COLUMN $COLUMN_LONGITUD REAL")
        }
    }

    // Insertar un álbum en la base de datos
    fun insertarAlbum(album: Album): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, album.nombre)
            put(COLUMN_ARTISTA, album.artista)
            put(COLUMN_PRECIO, album.precio)
            put(COLUMN_CANTIDAD, album.cantidad)
            put(COLUMN_PRECIO, album.latitud)
            put(COLUMN_CANTIDAD, album.longitud)
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
            COLUMN_CANTIDAD,
            COLUMN_LATITUD,
            COLUMN_LONGITUD
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
                    val latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD))
                    val longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD))

                    albums.add(Album(id, nombre, artista, precio, cantidad, latitud, longitud))
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
            put(COLUMN_LATITUD, album.latitud)
            put(COLUMN_LONGITUD, album.longitud)
        }
        return db.update(TABLE_ALBUMS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Eliminar un álbum de la base de datos
    fun eliminarAlbum(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_ALBUMS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
    fun obtenerAlbumPorId(id: Int): Album? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ALBUMS WHERE id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val artista = cursor.getString(cursor.getColumnIndexOrThrow("artista"))
            val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
            val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
            val latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"))
            val longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))

            cursor.close()
            db.close()

            Album(id, nombre, artista, precio, cantidad, latitud, longitud)
        } else {
            cursor.close()
            db.close()
            null
        }
    }


    companion object {
        const val DATABASE_NAME = "albums.db"
        const val DATABASE_VERSION = 2
        const val TABLE_ALBUMS = "albums"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_ARTISTA = "artista"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_CANTIDAD = "cantidad"
        const val COLUMN_LATITUD = "latitud"
        const val COLUMN_LONGITUD = "longitud"
    }
}

