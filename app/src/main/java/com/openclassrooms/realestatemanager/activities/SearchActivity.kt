package com.openclassrooms.realestatemanager.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.google.android.material.slider.LabelFormatter
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_search.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class SearchActivity : AppCompatActivity() {

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