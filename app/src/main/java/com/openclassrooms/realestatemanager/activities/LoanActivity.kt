package com.openclassrooms.realestatemanager.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.LabelFormatter
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_loan_simulation.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.text.StringBuilder

class LoanActivity : AppCompatActivity() {
    val TAG: String = "LoanActivity"

    var slider_loan_value = 0f
    var slider_period_value = 0f
    var interest_rate_value = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_simulation)

        slider_loan.setLabelFormatter{value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        slider_period.setLabelFormatter(object : LabelFormatter{
            override fun getFormattedValue(value: Float): String {
                val temp = StringBuilder()
                temp.append(value.roundToInt())
                temp.append(" years")
                return temp.toString()
            }
        })

        slider_loan.addOnChangeListener { slider, value, fromUser ->
            slider_loan_value = value
            monthly_payment_amount.setText(calculateMonthlyPayment(amount = value.toLong(),period = slider_period_value.toInt(), interest_rate_value).toString())

        }
        slider_period.addOnChangeListener { slider, value, fromUser ->
            slider_period_value = value
            monthly_payment_amount.setText(calculateMonthlyPayment(amount = slider_loan_value.toLong(),period = value.toInt(), interest_rate_value).toString())
        }
        interest_rate_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val temp = p0.toString().replace(',', '.')

                if (!p0.isNullOrEmpty()) {
                    Log.d(TAG, "onTextChanged: ${temp}")
                    interest_rate_value = temp.toFloat()
                    monthly_payment_amount.setText(calculateMonthlyPayment(amount = slider_loan_value.toLong(), period = slider_period_value.toInt(), interest_rate_value).toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    override fun onBackPressed() {
        finish()
    }

    fun calculateMonthlyPayment(
            amount : Long,
            period : Int,
            interestRate : Float) : Double
    {
        var power = (-12*period).toDouble()
        var denominator = 1 - (Math.pow(1 + (interestRate/100/12).toDouble(),power))
        var numerator = (amount * (interestRate/100/12))
        Log.d(TAG, "calculateMonthlyPayment: amount : ${amount} period : ${period} interestRate : ${interestRate} \n")
        Log.d(TAG, "calculateMonthlyPayment: formula : ${numerator} / ${denominator}")
        return numerator / denominator
    }

}