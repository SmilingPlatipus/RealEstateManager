package com.openclassrooms.realestatemanager.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.GeocodingResponse
import com.openclassrooms.realestatemanager.utils.GeocodingService
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class MapEstateActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var userLocation: Location? = null

    val estateViewModel by viewModel<EstateViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when{
            ContextCompat
                    .checkSelfPermission(
                            this,
                            android.Manifest.permission.
                            ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        COARSE_LOCATION_PERMISSION_CODE)
            }
            ContextCompat
                    .checkSelfPermission(
                            this,
                            android.Manifest.permission.
                            ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_PERMISSION_CODE)
            }
            else -> {
                initMap()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
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
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Coarse location permission is needed by the application", Toast.LENGTH_SHORT).show()
                finish()
            }
        else
            initMap()

        if (requestCode == FINE_LOCATION_PERMISSION_CODE)
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Fine location permission is needed by the application", Toast.LENGTH_SHORT).show()
                finish()
            }
        else
            initMap()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        googleMap.isMyLocationEnabled = true

        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Moving camera in Cahors by default
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(44.447226, 1.438742), DEFAULT_ZOOM))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    userLocation = location
                    if (userLocation != null)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLocation!!.latitude, userLocation!!.longitude), DEFAULT_ZOOM))
                }

        // If there is some estates having coordinates to null and if there is an Internet connection, retrieving lat and lng
        estateViewModel.allEstates.observe(this, Observer {
            it.forEach{ estate ->
                // If the lat and lng are null
                if ((estate.latitude == null) and (estate.longitude == null)) {
                    // If internet is available
                    if (Utils.isInternetAvailable(this)) {
                        estate.address?.let { address ->
                            var latLng : LatLng?

                            val geocodingService: GeocodingService = Retrofit.Builder()
                                    .baseUrl(GeocodingService.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(GeocodingService::class.java)

                            var formattedAddress = StringBuilder()
                            formattedAddress.append(address[0] + "%20" + address[1].replace(" ", "%20"))
                            formattedAddress.append("," + address[2] + "%20" + address[3])

                            Log.i("estateViewModel", "formattedAddress value : $formattedAddress")


                            geocodingService
                                    .getLatLngFromAddress(formattedAddress.toString(), BuildConfig.ApiKey)
                                    .enqueue(object : Callback<GeocodingResponse> {
                                        override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                                            var lat : Double?
                                            var lng : Double?
                                            var responseFromRequest = response.body()
                                            if (responseFromRequest?.results?.isEmpty() == true){
                                                Toast.makeText(applicationContext,getString(R.string.geocoding_bad_address), Toast.LENGTH_LONG).show()
                                                return
                                            }
                                            else{
                                                var firstResult = responseFromRequest?.results?.get(0)

                                                lat = firstResult?.geometry?.location?.lat
                                                lng = firstResult?.geometry?.location?.lng
                                            }

                                            if (lat?.isNaN() == false and (lng?.isNaN() == false)) {
                                                latLng = lng?.let { LatLng(lat, it) }!!

                                                Log.i("estateViewModel", "onResponse: $lat , $lng")

                                                // Latitude and longitude are retrieved, updating Estate
                                                var newEstate = Estate(
                                                        type = estate.type,
                                                        price = estate.price,
                                                        size = estate.size,
                                                        rooms = estate.rooms,
                                                        description = estate.description,
                                                        photosUriWithDescriptions = estate.photosUriWithDescriptions,
                                                        address = estate.address,
                                                        poi = estate.poi,
                                                        status = estate.status,
                                                        creationDate = estate.creationDate,
                                                        saleDate = estate.saleDate,
                                                        saler = estate.saler,
                                                        latitude = latLng?.latitude,
                                                        longitude = latLng?.longitude)
                                                newEstate.id = estate.id
                                                estateViewModel.insert(newEstate)

                                                var markerOptions = MarkerOptions()
                                                markerOptions.position(latLng!!)
                                                markerOptions.title(estate.address.toString())
                                                markerOptions.snippet(estate.id.toString())
                                                mMap.addMarker(markerOptions)
                                                }
                                            }

                                        override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {

                                        }
                                    })
                        }
                    }
                }
                else{
                    var markerOptions = MarkerOptions()
                    val lat = estate.latitude
                    val lng = estate.longitude
                    markerOptions.position(LatLng(lat!!,lng!!))
                    markerOptions.title(estate.address.toString())
                    markerOptions.snippet(estate.id.toString())
                    mMap.addMarker(markerOptions)
                }
            }

        })
    }

    companion object{
        const val COARSE_LOCATION_PERMISSION_CODE = 102
        const val FINE_LOCATION_PERMISSION_CODE = 103
        const val DEFAULT_ZOOM = 13.0f
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        var dialogWindow = Dialog(this)
        dialogWindow.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogWindow.setContentView(R.layout.custom_info_window)

        val estatePicture : ImageView = dialogWindow.findViewById(R.id.estate_pic)
        val estateAddress : TextView = dialogWindow.findViewById(R.id.estate_address)
        val estateType : TextView = dialogWindow.findViewById(R.id.estate_type)
        val estateDetails : Button = dialogWindow.findViewById(R.id.estate_detail_button)

        val estate = estateViewModel.getEstateById(p0?.snippet?.toLong()!!)
        val uriPhoto = Uri.parse(estate.photosUriWithDescriptions?.get(0)?.uri)

        Glide.with(this)
                .load(uriPhoto)
                .into(estatePicture)

        estateAddress.text = estate.address?.joinToString(" ")
        estateType.text = estate.type
        estateDetails.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var detailIntent = Intent(v?.context,DetailEstateActivity::class.java)

                detailIntent.putExtra(MainActivity.ID_OF_SELECTED_ESTATE,p0?.snippet?.toLong()!!)
                startActivity(detailIntent)
            }
        })


        dialogWindow.show()

        return true
    }
}