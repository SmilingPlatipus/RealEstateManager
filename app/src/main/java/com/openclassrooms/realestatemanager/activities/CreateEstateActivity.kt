package com.openclassrooms.realestatemanager.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.slider.Slider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.EstatePhoto
import kotlinx.android.synthetic.main.activity_create_estate.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class CreateEstateActivity : Activity()  {
    val TAG: String = "CreateEstateActivity"
    val LOAD_IMG_REQUEST: Int = 1

    var spinner_selection : String? = null
    var slider_price_value : Float? = null
    var slider_rooms_value : Float? = null
    var slider_size_value : Float? = null
    var checkboxesStatus = EnumSet.noneOf(NearbyServices::class.java)
    var photoList: MutableList<EstatePhoto> = emptyList<EstatePhoto>().toMutableList()
    lateinit var uriPhoto : Uri

    enum class NearbyServices{
        HOSPITAL,
        SHOPS,
        PARK,
        SCHOOL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)


        val estateTypes = resources.getStringArray(R.array.estate_types)
        slider_price_value = create_estate_Slider_price.value
        slider_rooms_value = create_estate_Slider_rooms.value
        slider_size_value  = create_estate_Slider_size.value
        create_estate_spinner_typeOfEstate.adapter = ArrayAdapter(this, R.layout.create_estate_spinner_row, estateTypes)
        create_estate_spinner_typeOfEstate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                spinner_selection = estateTypes[p2]
                Log.i(TAG, "onItemSelected: " + estateTypes[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        create_estate_Slider_price.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        create_estate_Slider_rooms.setLabelFormatter { value ->
            val temp = StringBuilder()
            temp.append(value.roundToInt())
            temp.append(" ")
            temp.append(getString(R.string.rooms))
            temp.toString()
        }

        create_estate_Slider_size.setLabelFormatter { value ->
            val temp = StringBuilder()
            temp.append(value.roundToInt())
            temp.append(" ")
            temp.append(getString(R.string.size))
            temp.toString()
        }

        create_estate_Slider_price.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                slider_price_value = slider.value
                Log.i(TAG, "onStartTrackingTouch: SliderPrice =$slider_price_value")
            }

            override fun onStopTrackingTouch(slider: Slider) {

            }
        })

        create_estate_Slider_size.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                slider_size_value = slider.value
                Log.i(TAG, "onStartTrackingTouch: SliderSize =$slider_size_value")
            }

            override fun onStopTrackingTouch(slider: Slider) {

            }
        })

        create_estate_Slider_rooms.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                slider_rooms_value = slider.value
                Log.i(TAG, "onStartTrackingTouch: SliderRooms =$slider_rooms_value")
            }

            override fun onStopTrackingTouch(slider: Slider) {

            }
        })

        create_estate_checkBox_all.setOnCheckedChangeListener { p0, p1 -> checkAllCheckboxes(p1) }

        buttonCreate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // checking hospital checkbox
                if (create_estate_checkBox_hospital.isChecked())
                    checkboxesStatus.add(NearbyServices.HOSPITAL)
                else if (checkboxesStatus.contains(NearbyServices.HOSPITAL))
                    checkboxesStatus.remove(NearbyServices.HOSPITAL)

                // checking shops checkbox
                if (create_estate_checkBox_shops.isChecked())
                    checkboxesStatus.add(NearbyServices.SHOPS)
                else if (checkboxesStatus.contains(NearbyServices.SHOPS))
                    checkboxesStatus.remove(NearbyServices.SHOPS)

                // checking park checkbox
                if (create_estate_checkBox_park.isChecked())
                    checkboxesStatus.add(NearbyServices.PARK)
                else if (checkboxesStatus.contains(NearbyServices.PARK))
                    checkboxesStatus.remove(NearbyServices.PARK)

                // checking school checkbox
                if (create_estate_checkBox_school.isChecked())
                    checkboxesStatus.add(NearbyServices.SCHOOL)
                else if (checkboxesStatus.contains(NearbyServices.SCHOOL))
                    checkboxesStatus.remove(NearbyServices.SCHOOL)

                when {
                    editText_address_of_estate_number.text.isEmpty() -> Toast.makeText(p0?.context, getString(R.string.create_estate_number_not_selected), Toast.LENGTH_SHORT).show()
                    editText_address_of_estate_street.text.isEmpty() -> Toast.makeText(p0?.context, getString(R.string.create_estate_street_not_selected), Toast.LENGTH_SHORT).show()
                    editText_address_of_estate_postalCode.text.isEmpty() -> Toast.makeText(p0?.context, getString(R.string.create_estate_postalCode_not_selected), Toast.LENGTH_SHORT).show()
                    editText_address_of_estate_city.text.isEmpty() -> Toast.makeText(p0?.context, getString(R.string.create_estate_city_not_selected), Toast.LENGTH_SHORT).show()
                }

                Log.i(TAG, "onClick: checkboxesStatus :" + checkboxesStatus.toString())
                Log.i(TAG, "onClick: spinner selection :" + spinner_selection)
                Log.i(TAG, "onClick: slider price :" + slider_price_value)
                Log.i(TAG, "onClick: slider rooms :" + slider_rooms_value)
                Log.i(TAG, "onClick: slider size :" + slider_size_value)
                Log.i(TAG, "onClick: editText_address_of_estate_number :" + editText_address_of_estate_number.text)
                Log.i(TAG, "onClick: editText_address_of_estate_street :" + editText_address_of_estate_street.text)
                Log.i(TAG, "onClick: editText_address_of_estate_postalCode :" + editText_address_of_estate_postalCode.text)
                Log.i(TAG, "onClick: editText_address_of_estate_city :" + editText_address_of_estate_city.text)
                Log.i(TAG, "onClick: editText_description_of_estate :" + editText_description_of_estate.text)

            }
        })
        buttonReinit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                editText_address_of_estate_number.text.clear()
                editText_address_of_estate_street.text.clear()
                editText_address_of_estate_postalCode.text.clear()
                editText_address_of_estate_city.text.clear()
                editText_description_of_estate.text.clear()

                checkboxesStatus = EnumSet.noneOf(NearbyServices::class.java)
                create_estate_checkBox_all.isChecked = false
                checkAllCheckboxes(switch = false)

                photos.removeAllViewsInLayout()
            }
        })
        buttonPhotos_take.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (v?.context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                } else {
                    //uriPhoto = Uri.fromFile(File(Environment.getExternalStorageState() + File.separator.toString() + "images"+ File.separator.toString()+ "photo"+photoList.size+".png"))
                    var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                }
            }
        })

        buttonPhotos_gallery.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val photoPickerIntent : Intent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.setType("image/")
                startActivityForResult(photoPickerIntent,LOAD_IMG_REQUEST)
            }
        })
    }

    override fun onBackPressed() {
        create_estate_motionLayout.transitionToStart()
        create_estate_motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                finish()
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            else
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
        {
            Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
            return
        }
        lateinit var photo: Bitmap

        when (requestCode)
        {
            // This is the photo taken by the user
            CAMERA_REQUEST -> photo = data?.extras?.get("data") as Bitmap
            LOAD_IMG_REQUEST -> photo = BitmapFactory.decodeStream(data?.data?.let { contentResolver.openInputStream(it) })
        }

            // Adding photo uri to the list to be saved
            //photoList.add(EstatePhoto(uriPhoto))

            // This is a brand new layout that is built for each photo and added to the linearLayout "photos", who is inside a scrollView
            var newThumbnail : RelativeLayout = RelativeLayout(this)
            newThumbnail.background = BitmapDrawable(resources, photo)
            var closeButtonThumbnail : ImageButton = ImageButton(this)
            closeButtonThumbnail.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_24, null))
            newThumbnail.layoutParams = RelativeLayout.LayoutParams(250, 250)
            var layoutParamsForCloseButton : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(40, 40)
            layoutParamsForCloseButton.addRule(RelativeLayout.ALIGN_PARENT_END)
            layoutParamsForCloseButton.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            layoutParamsForCloseButton.setMargins(16, 16, 16, 16)
            closeButtonThumbnail.layoutParams = layoutParamsForCloseButton
            closeButtonThumbnail.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    photos.removeView(newThumbnail)
                    //photoList.remove(EstatePhoto(uriPhoto))
                }
            })
            newThumbnail.addView(closeButtonThumbnail)
            photos.addView(newThumbnail)

            // Show the picture fullscreen on user touch on it
            newThumbnail.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // Creating the layout
                    var fullscreenPicture: RelativeLayout = RelativeLayout(v?.context)
                    fullscreenPicture.background = BitmapDrawable(resources, photo)
                    fullscreenPicture.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                    var closeButtonFullscreen: ImageButton = ImageButton(v?.context)
                    closeButtonFullscreen.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_24, null))
                    closeButtonFullscreen.layoutParams = layoutParamsForCloseButton
                    var userFullscreenDescription: EditText = EditText(v?.context)
                    userFullscreenDescription.inputType = InputType.TYPE_CLASS_TEXT
                    var layoutParams2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    userFullscreenDescription.layoutParams = layoutParams2
                    fullscreenPicture.addView(userFullscreenDescription)
                    fullscreenPicture.addView(closeButtonFullscreen)

                    // Retrieving description, if there is one
                    if (newThumbnail.childCount == 2) {
                        var retrievedDescription: TextView = newThumbnail.getChildAt(1) as TextView
                        userFullscreenDescription.setText(retrievedDescription.hint)
                        newThumbnail.removeViewAt(1)
                    }

                    // Then adding it to a dialog window
                    var dialogWindow: Dialog? = v?.context?.let { Dialog(it, android.R.style.Theme_NoTitleBar_Fullscreen) }
                    dialogWindow?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogWindow?.setContentView(fullscreenPicture)
                    dialogWindow?.show()


                    closeButtonFullscreen.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            if (userFullscreenDescription.text.isNotEmpty()) {
                                var layoutParams3 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                                layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                                layoutParams3.setMargins(16, 16, 16, 16)
                                var description: TextView = TextView(v?.context)
                                description.layoutParams = layoutParams3
                                description.hint = userFullscreenDescription.text
                                description.setHintTextColor(getColor(R.color.colorPrimary))
                                newThumbnail.addView(description)
                                //photoList.set(photoList.indexOf(EstatePhoto(uriPhoto)), EstatePhoto(uriPhoto,description.hint.toString()))
                            }
                            dialogWindow?.dismiss()
                        }
                    })
                }
            })

    }

    private fun checkAllCheckboxes(switch: Boolean) {
        create_estate_checkBox_hospital.isChecked = switch
        create_estate_checkBox_park.isChecked = switch
        create_estate_checkBox_school.isChecked = switch
        create_estate_checkBox_shops.isChecked = switch
    }

    companion object {
        const val CAMERA_REQUEST = 1888
        const val CAMERA_PERMISSION_CODE = 100
    }
}