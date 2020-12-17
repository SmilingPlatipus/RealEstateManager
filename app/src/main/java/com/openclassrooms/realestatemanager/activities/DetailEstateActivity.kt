package com.openclassrooms.realestatemanager.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activities.MainActivity.Companion.ID_OF_SELECTED_ESTATE
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.viewModels.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailEstateActivity : AppCompatActivity(), OnMapReadyCallback {
    private val detailViewModel by viewModel<DetailViewModel>()
    var estateSelected : Estate? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var bundle = intent.extras
        val idOfEstateSelected = bundle?.getLong(ID_OF_SELECTED_ESTATE)

        // This is the Points Of Interests, which are shown on TextView
        var poiText : String? = null

        var drawableHalfShade = ContextCompat.getDrawable(this,R.drawable.thumbnail_shade50) as GradientDrawable
        var drawableShade = ContextCompat.getDrawable(this,R.drawable.thumbnail_shade80) as GradientDrawable
        drawableHalfShade.setSize(
                resources.getDimension(R.dimen.thumbnail_width).toInt(),
                resources.getDimension(R.dimen.shade_height).toInt())
        drawableShade.setSize(
                resources.getDimension(R.dimen.thumbnail_width).toInt(),
                resources.getDimension(R.dimen.shade_height).toInt())
        lateinit var backgroundWithShade : LayerDrawable

            estateSelected = idOfEstateSelected?.let { detailViewModel.getEstateById(it) }
        if (!estateSelected?.latitude?.isNaN()!! and !estateSelected?.longitude?.isNaN()!!){
            val options :GoogleMapOptions = GoogleMapOptions().liteMode(true)
            val mapFragment = MapFragment.newInstance(options)
            val fragmentTransaction = getFragmentManager().beginTransaction()
            fragmentTransaction.add(R.id.detailMap,mapFragment)
            fragmentTransaction.commit()
            mapFragment.getMapAsync(this)
        }
            estateSelected?.photosUriWithDescriptions?.forEach {
                // This is a brand new layout that is built for each photo and added to the linearLayout "photos", who is inside a scrollView
                var newThumbnail = TextView(this)
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
                                backgroundWithShade.setLayerGravity(1,Gravity.BOTTOM)
                                newThumbnail.background = backgroundWithShade
                            }
                        })
                newThumbnail.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        if (newThumbnail.textColors.defaultColor == Color.WHITE) {
                            backgroundWithShade = LayerDrawable(arrayOf(drawable, drawableShade))
                            backgroundWithShade.setLayerGravity(1,Gravity.BOTTOM)

                            newThumbnail.setTextColor(Color.YELLOW)
                        }
                        else{
                            backgroundWithShade = LayerDrawable(arrayOf(drawable, drawableHalfShade))
                            backgroundWithShade.setLayerGravity(1,Gravity.BOTTOM)

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
                    var updateIntent = Intent(v?.context,CreateEstateActivity::class.java)
                    updateIntent.putExtra(ID_OF_SELECTED_ESTATE,idOfEstateSelected)
                    startActivity(updateIntent)
                    finish()
                }
            })
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
}