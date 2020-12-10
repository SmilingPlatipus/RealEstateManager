package com.openclassrooms.realestatemanager.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R

internal class MapEstateActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when{
            ContextCompat
                    .checkSelfPermission(
                            this,
                            android.Manifest.permission.
                            ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ->
                requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        COARSE_LOCATION_PERMISSION_CODE)
            ContextCompat
                    .checkSelfPermission(
                            this,
                            android.Manifest.permission.
                            ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ->
                requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_PERMISSION_CODE)
        }
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == COARSE_LOCATION_PERMISSION_CODE)
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Coarse location permission is needed by the application", Toast.LENGTH_SHORT).show()

        if (requestCode == FINE_LOCATION_PERMISSION_CODE)
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Fine location permission is needed by the application", Toast.LENGTH_SHORT).show()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val cahors = LatLng(44.446141, 1.439394)
        mMap.addMarker(MarkerOptions()
                .position(cahors)
                .title("Marker in Cahors"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cahors))
    }

    companion object{
        const val COARSE_LOCATION_PERMISSION_CODE = 102
        const val FINE_LOCATION_PERMISSION_CODE = 103
    }
}