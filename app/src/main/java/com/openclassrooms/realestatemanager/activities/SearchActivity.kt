package com.openclassrooms.realestatemanager.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.fragments.RecyclerViewFragment
import kotlinx.android.synthetic.main.activity_search.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class SearchActivity : AppCompatActivity() {
    val TAG: String = "SearchActivity"

    var rangeSlider_price_minValue : Float? = null
    var rangeSlider_price_maxValue : Float? = null
    var rangeSlider_size_minValue : Float? = null
    var rangeSlider_size_maxValue : Float? = null
    var rangeSlider_rooms_minValue : Float? = null
    var rangeSlider_rooms_maxValue : Float? = null
    var checkboxesStatus = EnumSet.noneOf(NearbyServices::class.java)
    var radioButton_estateType : EstateTypes? = null
    var radioButton_searchStatus : SearchStatus? = null

    enum class EstateTypes{
        APARTMENT, HOUSE, DUPLEX, PENTHOUSE
    }

    enum class SearchStatus{
        ALL, FOR_SALE, SOLD
    }

    enum class NearbyServices{
        HOSPITAL,
        SHOPS,
        PARK,
        SCHOOL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val fragment = RecyclerViewFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.estatesView,fragment)
        fragmentTransaction.commit()

        rangeSlider_price.setLabelFormatter {value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }
        rangeSlider_price_minValue = resources.getStringArray(R.array.initValues_rangeSlider_price)[0].toFloat()
        rangeSlider_price_maxValue = resources.getStringArray(R.array.initValues_rangeSlider_price)[1].toFloat()

        rangeSlider_rooms.setLabelFormatter { value ->
            val temp = StringBuilder()
            temp.append(value.roundToInt())
            temp.append(" ")
            temp.append(getString(R.string.rooms))
            temp.toString()
        }
        rangeSlider_rooms_minValue = resources.getStringArray(R.array.initValues_rangeSlider_rooms)[0].toFloat()
        rangeSlider_rooms_maxValue = resources.getStringArray(R.array.initValues_rangeSlider_rooms)[1].toFloat()

        rangeSlider_size.setLabelFormatter { value ->
            val temp = StringBuilder()
            temp.append(value.roundToInt())
            temp.append(" ")
            temp.append(getString(R.string.size))
            temp.toString()
        }
        rangeSlider_size_minValue = resources.getStringArray(R.array.initValues_rangeSlider_size)[0].toFloat()
        rangeSlider_size_maxValue = resources.getStringArray(R.array.initValues_rangeSlider_size)[1].toFloat()

        rangeSlider_price.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            override fun onStopTrackingTouch(slider: RangeSlider) {
                rangeSlider_price_minValue = slider.values[0]
                rangeSlider_price_maxValue = slider.values[1]

                Log.d(TAG, "onValueChange: rangeSlider_price_minValue = " + rangeSlider_price_minValue.toString())
                Log.d(TAG, "onValueChange: rangeSlider_price_maxValue = " + rangeSlider_price_maxValue.toString())
            }

            override fun onStartTrackingTouch(slider: RangeSlider) {

            }
        })

        rangeSlider_size.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            override fun onStopTrackingTouch(slider: RangeSlider) {
                rangeSlider_size_minValue = slider.values[0]
                rangeSlider_size_maxValue = slider.values[1]

                Log.d(TAG, "onValueChange: rangeSlider_size_minValue = " + rangeSlider_size_minValue.toString())
                Log.d(TAG, "onValueChange: rangeSlider_size_maxValue = " + rangeSlider_size_maxValue.toString())
            }
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }
        })

        rangeSlider_rooms.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            override fun onStopTrackingTouch(slider: RangeSlider) {
                rangeSlider_rooms_minValue = slider.values[0]
                rangeSlider_rooms_maxValue = slider.values[1]

                Log.d(TAG, "onValueChange: rangeSlider_rooms_minValue = " + rangeSlider_rooms_minValue.toString())
                Log.d(TAG, "onValueChange: rangeSlider_rooms_maxValue = " + rangeSlider_rooms_maxValue.toString())
            }
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }
        })

        checkBox_all.setOnCheckedChangeListener { p0, p1 -> checkAllCheckboxes(p1) }

        buttonSearch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // checking hospital checkbox
                if (checkBox_hospital.isChecked())
                    checkboxesStatus.add(NearbyServices.HOSPITAL)
                else if (checkboxesStatus.contains(NearbyServices.HOSPITAL))
                    checkboxesStatus.remove(NearbyServices.HOSPITAL)

                // checking shops checkbox
                if (checkBox_shops.isChecked())
                    checkboxesStatus.add(NearbyServices.SHOPS)
                else if (checkboxesStatus.contains(NearbyServices.SHOPS))
                    checkboxesStatus.remove(NearbyServices.SHOPS)

                // checking park checkbox
                if (checkBox_park.isChecked())
                    checkboxesStatus.add(NearbyServices.PARK)
                else if (checkboxesStatus.contains(NearbyServices.PARK))
                    checkboxesStatus.remove(NearbyServices.PARK)

                // checking school checkbox
                if (checkBox_school.isChecked())
                    checkboxesStatus.add(NearbyServices.SCHOOL)
                else if (checkboxesStatus.contains(NearbyServices.SCHOOL))
                    checkboxesStatus.remove(NearbyServices.SCHOOL)

                Log.i(TAG, "onClick: checkboxes_status : $checkboxesStatus")
                Log.i(TAG, "onClick: radioButton_searchStatus : $radioButton_searchStatus")
                Log.i(TAG, "onClick: radioButton_estateType : $radioButton_estateType")
                Log.i(TAG, "onClick: rangeSlider_price_minValue : $rangeSlider_price_minValue")
                Log.i(TAG, "onClick: rangeSlider_price_maxValue : $rangeSlider_price_maxValue")
                Log.i(TAG, "onClick: rangeSlider_size_minValue : $rangeSlider_size_minValue")
                Log.i(TAG, "onClick: rangeSlider_size_maxValue : $rangeSlider_size_maxValue")
                Log.i(TAG, "onClick: rangeSlider_rooms_minValue : $rangeSlider_rooms_minValue")
                Log.i(TAG, "onClick: rangeSlider_rooms_maxValue : $rangeSlider_rooms_maxValue")

                if (radioButton_searchStatus == null) {
                    Toast.makeText(p0?.context, getString(R.string.search_status_not_selected), Toast.LENGTH_SHORT).show()
                    return
                }

                if (radioButton_estateType == null) {
                    Toast.makeText(p0?.context, getString(R.string.estate_type_not_selected), Toast.LENGTH_SHORT).show()
                    return
                }
            }
        })

        radioGroup_search_status.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when(p0?.checkedRadioButtonId)
                {
                    R.id.radioButton_status_all -> radioButton_searchStatus = SearchStatus.ALL
                    R.id.radioButton_status_forSale -> radioButton_searchStatus = SearchStatus.FOR_SALE
                    R.id.radioButton_status_sold -> radioButton_searchStatus = SearchStatus.SOLD
                }
            }
        })

        radioGroup_search_typeOfEstate.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when(p0?.checkedRadioButtonId)
                {
                    R.id.radioButton_realEstateType_appartment -> radioButton_estateType = EstateTypes.APARTMENT
                    R.id.radioButton_realEstateType_house -> radioButton_estateType = EstateTypes.HOUSE
                    R.id.radioButton_realEstateType_duplex -> radioButton_estateType = EstateTypes.DUPLEX
                    R.id.radioButton_realEstateType_penthouse -> radioButton_estateType = EstateTypes.PENTHOUSE
                }
            }
        })

        buttonReinitialize.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                rangeSlider_price_minValue = resources.getStringArray(R.array.initValues_rangeSlider_price)[0].toFloat()
                rangeSlider_price_maxValue = resources.getStringArray(R.array.initValues_rangeSlider_price)[1].toFloat()
                rangeSlider_price.values = mutableListOf(
                        resources.getStringArray(R.array.initValues_rangeSlider_price)[0].toFloat(),
                        resources.getStringArray(R.array.initValues_rangeSlider_price)[1].toFloat())

                rangeSlider_rooms_minValue = resources.getStringArray(R.array.initValues_rangeSlider_rooms)[0].toFloat()
                rangeSlider_rooms_maxValue = resources.getStringArray(R.array.initValues_rangeSlider_rooms)[1].toFloat()
                rangeSlider_rooms.values = mutableListOf(
                        resources.getStringArray(R.array.initValues_rangeSlider_rooms)[0].toFloat(),
                        resources.getStringArray(R.array.initValues_rangeSlider_rooms)[1].toFloat())

                rangeSlider_size_minValue = resources.getStringArray(R.array.initValues_rangeSlider_size)[0].toFloat()
                rangeSlider_size_maxValue = resources.getStringArray(R.array.initValues_rangeSlider_size)[1].toFloat()
                rangeSlider_size.values = mutableListOf(
                        resources.getStringArray(R.array.initValues_rangeSlider_size)[0].toFloat(),
                        resources.getStringArray(R.array.initValues_rangeSlider_size)[1].toFloat())

                radioGroup_search_status.clearCheck()
                radioButton_searchStatus = null

                radioGroup_search_typeOfEstate.clearCheck()
                radioButton_estateType = null

                checkboxesStatus.removeAll(NearbyServices.values())
                checkAllCheckboxes(false)
                checkBox_all.isChecked = false
            }
        })
    }

    override fun onBackPressed() {
        search_motionLayout.transitionToStart()
        search_motionLayout?.setTransitionListener(object : TransitionListener{
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

    private fun checkAllCheckboxes(switch : Boolean){
        checkBox_hospital.isChecked = switch
        checkBox_park.isChecked = switch
        checkBox_school.isChecked = switch
        checkBox_shops.isChecked = switch
    }

}