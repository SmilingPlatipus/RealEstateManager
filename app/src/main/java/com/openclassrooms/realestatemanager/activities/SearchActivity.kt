package com.openclassrooms.realestatemanager.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rangeSlider_price.setLabelFormatter {value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        rangeSlider_rooms.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                val temp = StringBuilder()
                temp.append(value.roundToInt())
                temp.append(" ")
                temp.append(getString(R.string.rooms))
                return temp.toString()
            }
        })

        rangeSlider_size.setLabelFormatter(object : LabelFormatter{
            override fun getFormattedValue(value: Float): String {
                val temp = StringBuilder()
                temp.append(value.roundToInt())
                temp.append(" ")
                temp.append(getString(R.string.size))
                return temp.toString()
            }
        })

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

}