package com.openclassrooms.realestatemanager.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.slider.Slider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstatePhoto
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import kotlinx.android.synthetic.main.activity_create_estate.*
import com.openclassrooms.realestatemanager.BuildConfig
import kotlinx.android.synthetic.main.activity_create_estate.top_image
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.roundToInt

class CreateEstateActivity : AppCompatActivity()  {
    val TAG: String = "CreateEstateActivity"

    var spinner_selection : String? = null
    var slider_price_value : Float? = null
    var slider_rooms_value : Float? = null
    var slider_size_value : Float? = null
    var checkboxesStatus = EnumSet.noneOf(NearbyServices::class.java)
    val estateViewModel by viewModel<EstateViewModel>()

    lateinit var photo: Bitmap
    var photoList: MutableList<EstatePhoto> = emptyList<EstatePhoto>().toMutableList()
    lateinit var uriPhoto : Uri
    lateinit var outputFile : File

    enum class NearbyServices{
        HOSPITAL,
        SHOPS,
        PARK,
        SCHOOL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)

        var bundle = intent.extras
        val idOfEstateSelected = bundle?.getLong(MainActivity.ID_OF_SELECTED_ESTATE)
        var estateSelected : Estate? = null
        var spinnerPosition : Int? = null

        val estateTypes = resources.getStringArray(R.array.estate_types)

        estateSelected = idOfEstateSelected?.let { estateViewModel.getEstateById(it) }

        // If bundle is not null, it's an estate creation. If not, values are retrieved from database and views initialized.

        if (bundle == null) {
            top_image.text = getString(R.string.create_estate_title_create)
            slider_price_value = create_estate_Slider_price.value
            slider_rooms_value = create_estate_Slider_rooms.value
            slider_size_value = create_estate_Slider_size.value
        }
        else {
            // Changing title of the page
            top_image.text = getString(R.string.create_estate_title_update)

            // Updating Sliders values and positions
            slider_price_value = estateSelected?.price
            slider_rooms_value = estateSelected?.rooms?.toFloat()
            slider_size_value = estateSelected?.size
            create_estate_Slider_price.value = slider_price_value!!
            create_estate_Slider_rooms.value = slider_rooms_value!!
            create_estate_Slider_size.value = slider_size_value!!

            // Updating spinner selection
            estateTypes.forEach {
                if (it.compareTo(estateSelected?.type.toString()) == 0)
                    spinnerPosition = estateTypes.indexOf(it)
            }
            // Updating checkboxes

            if (estateSelected?.poi?.containsAll(listOf(
                            NearbyServices.HOSPITAL.name.toLowerCase(),
                            NearbyServices.SHOPS.name.toLowerCase(),
                            NearbyServices.SCHOOL.name.toLowerCase(),
                            NearbyServices.PARK.name.toLowerCase()
                    )) == true) {
                create_estate_checkBox_all.isChecked = true
                checkAllCheckboxes(true)
                checkboxesStatus = EnumSet.allOf(NearbyServices::class.java)
            } else
                estateSelected?.poi?.forEach {
                    if (it.compareTo(NearbyServices.HOSPITAL.name.toLowerCase()) == 0) {
                        create_estate_checkBox_hospital.isChecked = true
                        checkboxesStatus.add(NearbyServices.HOSPITAL)
                    }
                    if (it.compareTo(NearbyServices.SHOPS.name.toLowerCase()) == 0) {
                        create_estate_checkBox_shops.isChecked = true
                        checkboxesStatus.add(NearbyServices.SHOPS)
                    }
                    if (it.compareTo(NearbyServices.PARK.name.toLowerCase()) == 0) {
                        create_estate_checkBox_park.isChecked = true
                        checkboxesStatus.add(NearbyServices.PARK)
                    }
                    if (it.compareTo(NearbyServices.SCHOOL.name.toLowerCase()) == 0) {
                        create_estate_checkBox_school.isChecked = true
                        checkboxesStatus.add(NearbyServices.SCHOOL)
                    }
                }

            // Updating EditTexts with data from estate
            editText_address_of_estate_number.setText(estateSelected?.address?.get(0))
            editText_address_of_estate_street.setText(estateSelected?.address?.get(1))
            editText_address_of_estate_postalCode.setText(estateSelected?.address?.get(2))
            editText_address_of_estate_city.setText(estateSelected?.address?.get(3))
            editText_description_of_estate.setText(estateSelected?.description)

            // Changing button Create to Update
            buttonCreate.text = getString(R.string.create_estate_button_update)

            var drawableHalfShade = ContextCompat.getDrawable(this, R.drawable.thumbnail_shade50) as GradientDrawable
            var drawableShade = ContextCompat.getDrawable(this, R.drawable.thumbnail_shade80) as GradientDrawable
            drawableHalfShade.setSize(150, 150)
            drawableShade.setSize(150, 150)
            lateinit var backgroundWithShade: LayerDrawable

            estateSelected?.photosUriWithDescriptions?.forEach {
                // This is a brand new layout that is built for each photo and added to the linearLayout "photos", who is inside a scrollView
                var newThumbnail = TextView(this)
                newThumbnail.layoutParams = RelativeLayout.LayoutParams(400, 400)
                newThumbnail.gravity = Gravity.BOTTOM
                var uriPhoto = Uri.parse(it?.uri)
                lateinit var drawable: Drawable
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
                newThumbnail.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        backgroundWithShade = LayerDrawable(arrayOf(drawable, drawableShade))
                        backgroundWithShade.setLayerGravity(1, Gravity.BOTTOM)
                        newThumbnail.background = backgroundWithShade
                        newThumbnail.setTextColor(Color.YELLOW)
                    }
                })

                photos.addView(newThumbnail)
                photoList.add(it)
            }
        }

        create_estate_spinner_typeOfEstate.adapter = ArrayAdapter(this, R.layout.create_estate_spinner_row, estateTypes)

        if (spinnerPosition != null)
            create_estate_spinner_typeOfEstate.setSelection(spinnerPosition!!)

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

            }

            override fun onStopTrackingTouch(slider: Slider) {
                slider_price_value = slider.value
                Log.i(TAG, "onStopTrackingTouch: SliderPrice =$slider_price_value")
            }
        })

        create_estate_Slider_size.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                slider_size_value = slider.value
                Log.i(TAG, "onStopTrackingTouch: SliderSize =$slider_size_value")
            }
        })

        create_estate_Slider_rooms.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                slider_rooms_value = slider.value
                Log.i(TAG, "onStopTrackingTouch: SliderRooms =$slider_rooms_value")
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

                // Adress content is needed, so if something is missing, return
                when {
                    editText_address_of_estate_number.text.isEmpty() -> {
                        Toast.makeText(p0?.context, getString(R.string.create_estate_number_not_selected), Toast.LENGTH_SHORT).show()
                        return
                    }
                    editText_address_of_estate_street.text.isEmpty() -> {
                        Toast.makeText(p0?.context, getString(R.string.create_estate_street_not_selected), Toast.LENGTH_SHORT).show()
                        return
                    }
                    editText_address_of_estate_postalCode.text.isEmpty() -> {
                        Toast.makeText(p0?.context, getString(R.string.create_estate_postalCode_not_selected), Toast.LENGTH_SHORT).show()
                        return
                    }
                    editText_address_of_estate_city.text.isEmpty() -> {
                        Toast.makeText(p0?.context, getString(R.string.create_estate_city_not_selected), Toast.LENGTH_SHORT).show()
                        return
                    }
                    photoList.isEmpty() -> {
                        Toast.makeText(p0?.context, getString(R.string.create_estate_photo_not_selected), Toast.LENGTH_SHORT).show()
                        return
                    }
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

                photoList.forEach {
                    Log.i(TAG, "onClick: photoList : ${it}")
                }
                var poiList : MutableList<String> = emptyList<String>().toMutableList()
                checkboxesStatus.forEach {
                    poiList.add(it.name.toLowerCase())
                }

                var newEstate = Estate(
                        spinner_selection,
                        slider_price_value,
                        slider_size_value,
                        slider_rooms_value?.roundToInt(),
                        editText_description_of_estate.text.toString(),
                        photoList,
                        listOf(
                                editText_address_of_estate_number.text.toString(),
                                editText_address_of_estate_street.text.toString(),
                                editText_address_of_estate_postalCode.text.toString(),
                                editText_address_of_estate_city.text.toString()),
                        poiList,
                        SearchActivity.SearchStatus.FORSALE.name.toLowerCase(),
                        OffsetDateTime.now(),
                        null,
                        "Jean Michel",
                        null,
                        null)

                if (idOfEstateSelected == null) {
                    estateViewModel.insert(newEstate)
                    Toast.makeText(p0?.context, getString(R.string.create_estate_creation), Toast.LENGTH_LONG).show()
                }
                else{
                    newEstate.id = estateSelected?.id!!
                    estateViewModel.update(newEstate)
                    Toast.makeText(p0?.context, getString(R.string.create_estate_update), Toast.LENGTH_LONG).show()
                }
                finish()
            }
        })

        buttonReinit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val alertDialog = AlertDialog.Builder(p0?.context)
                alertDialog.setTitle(p0?.context?.getString(R.string.create_estate_alert_dialog_title))
                alertDialog.setMessage(p0?.context?.getString(R.string.create_estate_alert_dialog_message))
                alertDialog.setPositiveButton(p0?.context?.getString(R.string.create_estate_alert_dialog_yes),object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        editText_address_of_estate_number.text.clear()
                        editText_address_of_estate_street.text.clear()
                        editText_address_of_estate_postalCode.text.clear()
                        editText_address_of_estate_city.text.clear()
                        editText_description_of_estate.text.clear()

                        checkboxesStatus = EnumSet.noneOf(NearbyServices::class.java)
                        create_estate_checkBox_all.isChecked = false
                        checkAllCheckboxes(switch = false)

                        photos.removeAllViewsInLayout()
                        photoList.clear()
                    }
                })
                alertDialog.setNegativeButton(p0?.context?.getString(R.string.create_estate_alert_dialog_no),object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                    }
                })
                alertDialog.show()
            }
        })

        buttonPhotos_take.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                when {
                    v?.context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED ->
                        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                    v?.context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED ->
                        requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_PERMISSION_CODE)
                    else -> {
                        var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

                        outputFile = createNewJpegFile()
                        uriPhoto = FileProvider.getUriForFile(v.context, BuildConfig.APPLICATION_ID + ".fileprovider", outputFile)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)
                    }
                }
            }
        })

        buttonPhotos_gallery.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.setType("image/")
                startActivityForResult(photoPickerIntent, LOAD_IMG_REQUEST)
            }
        })
    }

    private fun createNewJpegFile(): File {
        val timestamp = SimpleDateFormat("ddMMyyyy_").format(Date())
        val filename = StringBuilder("pic${timestamp}")
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return createTempFile(filename.toString(),
                ".jpg",
                storageDir)
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
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Camera permission is needed by the application", Toast.LENGTH_SHORT).show()

        if (requestCode == WRITE_EXTERNAL_PERMISSION_CODE)
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Write external storage permission is needed by the application", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            return
        }
        val uri: Uri? = data?.data
        var inputStream = uri?.let { contentResolver.openInputStream(it) }
        when (requestCode)
        {
            // This is the photo taken by the user (saved in the gallery for testing purposes)
            CAMERA_REQUEST -> {
                // photo taken by user, as a bitmap
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    photo = BitmapFactory.decodeFile(outputFile.absolutePath)
                }
            }
            // This is the photo taken from gallery, rotated in the right side
            LOAD_IMG_REQUEST -> {
                photo = BitmapFactory.decodeStream(data?.data?.let { contentResolver.openInputStream(it) })
                outputFile = createNewJpegFile()
            }
        }
        var matrix = turnPhotoTheRightWay(inputStream)
        photo = Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true)
        // Saving selected image into the external directory of the app
        uriPhoto = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", outputFile)
        savePhotoToTelephone(this, photo, uriPhoto)
        // Adding photo uri to the list to be saved
        photoList.add(EstatePhoto(uriPhoto.toString()))

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
                photoList.removeAt(photos.indexOfChild(newThumbnail))
                photos.removeView(newThumbnail)

                photoList.forEach {
                    Log.i(TAG, "onActivityResult: photoList : ${it}")
                }
            }
        })
        newThumbnail.addView(closeButtonThumbnail)
        photos.addView(newThumbnail)

        photoList.forEach {
            Log.i(TAG, "onActivityResult: photoList : ${it}")
        }
        // Show the picture fullscreen on user touch
        newThumbnail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Getting the right thumbnail to draw fullscreen
                val indexOfThumbnailInPhotoList = photos.indexOfChild(v)
                inputStream = v?.context?.contentResolver?.openInputStream(Uri.parse(photoList[indexOfThumbnailInPhotoList].uri))

                // Creating the layout
                var fullscreenPicture: RelativeLayout = RelativeLayout(v?.context)
                fullscreenPicture.background = BitmapDrawable(resources, inputStream)
                fullscreenPicture.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                // Creating the fullscreen close button
                var closeButtonFullscreen: ImageButton = ImageButton(v?.context)
                var layoutParamsForCloseButton2 : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(80, 80)
                layoutParamsForCloseButton2.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParamsForCloseButton2.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                layoutParamsForCloseButton2.setMargins(16, 16, 16, 16)
                closeButtonFullscreen.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_24, null))
                closeButtonFullscreen.layoutParams = layoutParamsForCloseButton2
                // Creating the editText, for receiving user content
                var userFullscreenDescription: EditText = EditText(v?.context)
                userFullscreenDescription.inputType = InputType.TYPE_CLASS_TEXT
                var layoutParamsForFullscreenDescription = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                layoutParamsForFullscreenDescription.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                userFullscreenDescription.layoutParams = layoutParamsForFullscreenDescription
                userFullscreenDescription.setTextColor(Color.WHITE)
                // Adding childs to the new fullscreen layout
                fullscreenPicture.addView(userFullscreenDescription)
                fullscreenPicture.addView(closeButtonFullscreen)
                // Retrieving description, if there is one
                if (newThumbnail.childCount >= 2) {
                    var retrievedDescription: TextView = newThumbnail.getChildAt(1) as TextView
                    userFullscreenDescription.setText(retrievedDescription.text)
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
                            // Setting params for description
                            var layoutParamsForThumbnailDescription =
                                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                            layoutParamsForThumbnailDescription.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            layoutParamsForThumbnailDescription.addRule(RelativeLayout.CENTER_HORIZONTAL)
                            layoutParamsForThumbnailDescription.setMargins(16, 16, 16, 16)
                            var description: TextView = TextView(v?.context)
                            description.layoutParams = layoutParamsForThumbnailDescription
                            description.text = userFullscreenDescription.text
                            var shade = ResourcesCompat.getDrawable(resources,R.drawable.thumbnail_shade50, null)
                            shade?.alpha = 100
                            description.background = shade
                            description.setTextColor(Color.WHITE)
                            // Adding to the corresponding thumbnail
                            newThumbnail.addView(description)
                            // Updating the photoList accordingly
                            photoList.set(
                                    indexOfThumbnailInPhotoList,
                                    EstatePhoto(photoList[indexOfThumbnailInPhotoList].uri,
                                            description.text.toString()))

                            photoList.forEach {
                                Log.i(TAG, "onActivityResult: photoList : ${it}")
                            }
                        }
                        dialogWindow?.dismiss()
                    }
                })
            }
        })
    }

    private fun turnPhotoTheRightWay(inputStream: InputStream?): Matrix {
        val matrix: Matrix = Matrix()
        if (inputStream != null) {
            val exif: ExifInterface = ExifInterface(inputStream)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) {
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }
        }
        return matrix
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun savePhotoToTelephone(context: Context, photoToSave: Bitmap, uriPhoto: Uri) {

        var values : ContentValues = ContentValues()
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        values.put(MediaStore.MediaColumns.IS_PENDING, true)

        val uri : Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null)
        {
            var outputStream = ByteArrayOutputStream()
            photoToSave.compress(Bitmap.CompressFormat.JPEG, 100,outputStream)
            var photoData : ByteArray = outputStream.toByteArray()

            var fileOutputStream = FileOutputStream(outputFile)
            fileOutputStream.write(photoData)
            fileOutputStream.flush()
            fileOutputStream.close()
            Toast.makeText(this, "File saved to : ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAllCheckboxes(switch: Boolean) {
        create_estate_checkBox_hospital.isChecked = switch
        create_estate_checkBox_park.isChecked = switch
        create_estate_checkBox_school.isChecked = switch
        create_estate_checkBox_shops.isChecked = switch
    }


    companion object {
        const val CAMERA_REQUEST = 1888
        const val LOAD_IMG_REQUEST = 1889
        const val CAMERA_PERMISSION_CODE = 100
        const val WRITE_EXTERNAL_PERMISSION_CODE = 101
    }
}