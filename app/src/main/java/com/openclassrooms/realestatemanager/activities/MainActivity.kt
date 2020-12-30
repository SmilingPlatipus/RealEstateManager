package com.openclassrooms.realestatemanager.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.FragmentTransaction
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.fragments.RecyclerViewFragment
import com.openclassrooms.realestatemanager.modules.mainModule
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    enum class Activities {
        NO_PREVIOUS_ACTIVITY,LOAN_ACTIVITY, SEARCH_ACTIVITY, CREATE_ESTATE_ACTIVITY, MAP_ACTIVITY
    }

    lateinit var previousActivityWas : Activities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val fragment = RecyclerViewFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.estatesView,fragment)
        fragmentTransaction.commit()
        // Simple var allowing to know the activity from which we come from
        previousActivityWas = Activities.NO_PREVIOUS_ACTIVITY
    }

    override fun onResume() {
        super.onResume()

        when (previousActivityWas)
        {
            Activities.LOAN_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.loan_click_transition)
                main_activity_motionLayout.transitionToStart()
            }
            Activities.SEARCH_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.search_click_transition)
                main_activity_motionLayout.transitionToStart()
            }
            Activities.CREATE_ESTATE_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.create_estate_click_transition)
                main_activity_motionLayout.transitionToStart()
            }
            Activities.MAP_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.map_click_transition)
                main_activity_motionLayout.transitionToStart()
            }
        }
        // Saving the name of the activity when we came from
        main_activity_motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (p1 == R.id.loan_click_end){
                    previousActivityWas = Activities.LOAN_ACTIVITY
                    startActivity(Intent(this@MainActivity, LoanActivity::class.java))
                }
                if (p1 == R.id.search_click_end){
                    previousActivityWas = Activities.SEARCH_ACTIVITY
                    startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                }
                if (p1 == R.id.create_estate_click_end){
                    previousActivityWas = Activities.CREATE_ESTATE_ACTIVITY
                    startActivity(Intent(this@MainActivity, CreateEstateActivity::class.java))
                }
                if (p1 == R.id.map_click_end){
                    previousActivityWas = Activities.MAP_ACTIVITY
                    startActivity(Intent(this@MainActivity, MapEstateActivity::class.java))
                }
            }
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
        })
    }

    companion object {
        const val ID_OF_SELECTED_ESTATE = "ID_OF_SELECTED_ESTATE"
    }
}