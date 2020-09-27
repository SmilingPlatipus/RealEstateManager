package com.openclassrooms.realestatemanager.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.transition.TransitionManager
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    enum class Activities {
        NO_PREVIOUS_ACTIVITY,LOAN_ACTIVITY, OTHER_ACTIVITY1, OTHER_ACTIVITY2
    }

    lateinit var previousActivity : Activities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previousActivity = Activities.NO_PREVIOUS_ACTIVITY
    }

    override fun onResume() {
        super.onResume()

        when (previousActivity)
        {
            Activities.LOAN_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.main_starting_animation)
                main_activity_motionLayout.transitionToEnd()
            }

        }
        main_activity_motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (p1 == R.id.loan_click_end){
                    previousActivity = Activities.LOAN_ACTIVITY
                    startActivity(Intent(this@MainActivity, LoanActivity::class.java))
                }
            }
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
        })
    }
}