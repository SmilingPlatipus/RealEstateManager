package com.openclassrooms.realestatemanager.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activities.CreateEstateActivity
import com.openclassrooms.realestatemanager.activities.MainActivity
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment(val idOfEstate: Long) : Fragment(), OnMapReadyCallback {
    private val detailViewModel by viewModel<DetailViewModel>()
    lateinit var estateSelected : Estate
    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_detail,container,false)
        estateSelected = detailViewModel.getEstateById(idOfEstate)
        val detailEstate_scrollview_layout = view.findViewById<LinearLayout>(R.id.detailEstate_scrollview_layout)
        val detailEstate_description = view.findViewById<TextView>(R.id.detailEstate_description)
        val detailEstate_size = view.findViewById<TextView>(R.id.detailEstate_size)
        val detailEstate_rooms = view.findViewById<TextView>(R.id.detailEstate_rooms)
        val detailEstate_nearbyServices = view.findViewById<TextView>(R.id.detailEstate_nearbyServices)
        val detailEstate_status = view.findViewById<TextView>(R.id.detailEstate_status)
        val detailEstate_numberStreet = view.findViewById<TextView>(R.id.detailEstate_numberStreet)
        val detailEstate_street = view.findViewById<TextView>(R.id.detailEstate_street)
        val detailEstate_postalCode = view.findViewById<TextView>(R.id.detailEstate_postalCode)
        val detailEstate_city = view.findViewById<TextView>(R.id.detailEstate_city)
        val detailEstate_price = view.findViewById<TextView>(R.id.detailEstate_price)
        val buttonEdit = view.findViewById<Button>(R.id.buttonEdit)
        // This is the Points Of Interests, which are shown on TextView
        var poiText : String? = null

        var drawableHalfShade = ContextCompat.getDrawable(view.context, R.drawable.thumbnail_shade50) as GradientDrawable
        var drawableShade = ContextCompat.getDrawable(view.context, R.drawable.thumbnail_shade80) as GradientDrawable
        drawableHalfShade.setSize(
                resources.getDimension(R.dimen.thumbnail_width).toInt(),
                resources.getDimension(R.dimen.shade_height).toInt())
        drawableShade.setSize(
                resources.getDimension(R.dimen.thumbnail_width).toInt(),
                resources.getDimension(R.dimen.shade_height).toInt())
        lateinit var backgroundWithShade : LayerDrawable


        if (!estateSelected?.latitude?.isNaN()!! and !estateSelected?.longitude?.isNaN()!!){
            val options : GoogleMapOptions = GoogleMapOptions().liteMode(true)
            val mapFragment = SupportMapFragment.newInstance(options)

            val fragmentTransaction = getFragmentManager()?.beginTransaction()
            fragmentTransaction?.add(R.id.detailMap,mapFragment)
            fragmentTransaction?.commit()
            mapFragment.getMapAsync(this)


        }
        estateSelected?.photosUriWithDescriptions?.forEach {
            // This is a brand new layout that is built for each photo and added to the linearLayout "photos", who is inside a scrollView
            var newThumbnail = TextView(view.context)
            newThumbnail.layoutParams = RelativeLayout.LayoutParams(
                    resources.getDimension(R.dimen.thumbnail_width).toInt(),
                    resources.getDimension(R.dimen.thumbnail_height).toInt())
            newThumbnail.gravity = Gravity.BOTTOM
            newThumbnail.setTextColor(Color.WHITE)
            var uriPhoto = Uri.parse(it?.uri)
            lateinit var drawable : Drawable
            Glide.with(this)
                    .asDrawable()
                    .load(uriPhoto)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            drawable = resource
                            newThumbnail.text = it?.description
                            backgroundWithShade = LayerDrawable(arrayOf(drawable, drawableHalfShade))
                            backgroundWithShade.setLayerGravity(1, Gravity.BOTTOM)
                            newThumbnail.background = backgroundWithShade
                        }
                    })
            newThumbnail.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (newThumbnail.textColors.defaultColor == Color.WHITE) {
                        backgroundWithShade = LayerDrawable(arrayOf(drawable, drawableShade))
                        backgroundWithShade.setLayerGravity(1, Gravity.BOTTOM)

                        newThumbnail.setTextColor(Color.YELLOW)
                    }
                    else{
                        backgroundWithShade = LayerDrawable(arrayOf(drawable, drawableHalfShade))
                        backgroundWithShade.setLayerGravity(1, Gravity.BOTTOM)

                        newThumbnail.setTextColor(Color.WHITE)
                    }
                    newThumbnail.background = backgroundWithShade
                }
            })

            detailEstate_scrollview_layout.addView(newThumbnail)

        }
        detailEstate_description.text = estateSelected?.description
        detailEstate_size.text = estateSelected?.size.toString()
        detailEstate_rooms.text = estateSelected?.rooms.toString()
        estateSelected?.poi?.forEach {
            if (poiText == null)
                poiText = it.toLowerCase() + "\n"
            else
                poiText += it.toLowerCase() + "\n"
        }
        detailEstate_nearbyServices.text = poiText
        detailEstate_status.text = estateSelected?.status
        detailEstate_numberStreet.text = estateSelected?.address?.get(0)
        detailEstate_street.text = estateSelected?.address?.get(1)
        detailEstate_postalCode.text = estateSelected?.address?.get(2)
        detailEstate_city.text = estateSelected?.address?.get(3)
        detailEstate_price.text = estateSelected?.price.toString()

        buttonEdit.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var updateIntent = Intent(v?.context, CreateEstateActivity::class.java)
                updateIntent.putExtra(MainActivity.ID_OF_SELECTED_ESTATE, estateSelected!!.id)
                startActivity(updateIntent)
            }
        })

        return view
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
}