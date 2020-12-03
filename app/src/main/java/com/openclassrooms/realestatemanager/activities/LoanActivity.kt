package com.openclassrooms.realestatemanager.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.slider.LabelFormatter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.fragments.RecyclerViewFragment
import kotlinx.android.synthetic.main.activity_loan_simulation.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.text.StringBuilder

class LoanActivity : AppCompatActivity() {
    val TAG: String = "LoanActivity"

    var slider_amount_value = 0f
    var slider_period_value = 0f
    var interest_rate_value : Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_simulation)
        val fragment = RecyclerViewFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.estatesView,fragment)
        fragmentTransaction.commit()

        slider_amount.setLabelFormatter{ value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        slider_period.setLabelFormatter(object : LabelFormatter{
            override fun getFormattedValue(value: Float): String {
                val temp = StringBuilder()
                temp.append(value.roundToInt())
                temp.append(" ")
                temp.append(getString(R.string.years))
                return temp.toString()
            }
        })

        slider_amount.addOnChangeListener { slider, value, fromUser ->
            slider_amount_value = value
        }
        slider_period.addOnChangeListener { slider, value, fromUser ->
            slider_period_value = value
        }
        interest_rate_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val temp = p0.toString().replace(',', '.')

                if (!p0.isNullOrEmpty()) {
                    Log.d(TAG, "onTextChanged: ${temp}")
                    interest_rate_value = temp.toFloat()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        buttonCalculate.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (interest_rate_value != null)
                    monthly_payment_amount.text = calculateMonthlyPayment(slider_amount_value.toLong(),slider_period_value.toInt(), interest_rate_value!!) +
                            getString(R.string.currency_per_month)
                else
                    Toast.makeText(v?.context,"You need to enter an interest rate to be able to calculate",Toast.LENGTH_LONG).show()
            }
        })
        buttonReset.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                slider_amount.value = 0F
                slider_period.value = 3F
                monthly_payment_amount.clearComposingText()
                interest_rate_value = null
                interest_rate_editText.text = null
            }
        })
    }

    override fun onBackPressed() {
        loan_simulation_motionLayout.transitionToStart()
        loan_simulation_motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener{
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

    fun calculateMonthlyPayment(
            amount : Long,
            period : Int,
            interestRate : Float) : String
    {
        val df = DecimalFormat()
        df.maximumFractionDigits = 2
        df.roundingMode = RoundingMode.HALF_UP
        var power = (-12*period).toDouble()
        var denominator = 1 - (Math.pow(1 + (interestRate/100/12).toDouble(),power))
        var numerator = (amount * (interestRate/100/12))
        Log.i(TAG, "calculateMonthlyPayment: amount : ${amount} period : ${period} interestRate : ${interestRate} \n")
        Log.i(TAG, "calculateMonthlyPayment: formula : ${numerator} / ${denominator}")

        return df.format(numerator / denominator)
    }

}