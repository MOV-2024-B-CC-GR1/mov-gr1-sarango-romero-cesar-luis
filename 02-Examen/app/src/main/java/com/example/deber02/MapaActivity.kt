package com.example.deber02

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Obtener latitud y longitud desde el intent
        latitud = intent.getDoubleExtra("LATITUD", 0.0)
        longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        Log.d("MapaActivity", "Coordenadas recibidas: Latitud=$latitud, Longitud=$longitud")
        // Inicializar el mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Verificar si las coordenadas son válidas
        if (latitud != 0.0 && longitud != 0.0) {
            val ubicacion = LatLng(latitud, longitud)
            googleMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación del álbum"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
        } else {
            // Mostrar un mensaje de error o una ubicación por defecto
            val ubicacionPorDefecto = LatLng(-0.1807, -78.4678) // Ejemplo: Quito, Ecuador
            googleMap.addMarker(MarkerOptions().position(ubicacionPorDefecto).title("Ubicación por defecto"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionPorDefecto, 10f))
        }
    }
}