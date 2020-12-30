package com.openclassrooms.realestatemanager.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activities.MainActivity.Companion.ID_OF_SELECTED_ESTATE
import com.openclassrooms.realestatemanager.fragments.DetailFragment
import com.openclassrooms.realestatemanager.fragments.RecyclerViewFragment
import com.openclassrooms.realestatemanager.model.Estate

class DetailEstateActivity : AppCompatActivity(), OnMapReadyCallback {
    var estateSelected : Estate? = null
    private lateinit var mMap: GoogleMap

    private var isTwoPane : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var bundle = intent.extras
        val idOfEstateSelected = bundle?.getLong(ID_OF_SELECTED_ESTATE)

        var fragmentDetail = idOfEstateSelected?.let { DetailFragment(it) }
        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.detail_container, fragmentDetail!!)
        fragmentTransaction.commit()

        checkForTabletDevice()
        if (isTwoPane) {
            val fragment = RecyclerViewFragment.newInstance()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.estatesView,fragment)
            fragmentTransaction.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        const val TAG = "DetailEstateActivity"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var markerOptions = MarkerOptions()
        val lat = estateSelected?.latitude
        val lng = estateSelected?.longitude
        markerOptions.position(LatLng(lat!!,lng!!))
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat,lng),10.0f))
    }

    private fun checkForTabletDevice() {
        var fragmentList = findViewById<FrameLayout>(R.id.estatesView)

        isTwoPane = fragmentList != null
    }

}