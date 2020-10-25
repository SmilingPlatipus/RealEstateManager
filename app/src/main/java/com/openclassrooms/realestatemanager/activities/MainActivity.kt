package com.openclassrooms.realestatemanager.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.modules.mainModule
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val estateViewModel by viewModel<EstateViewModel>()

    enum class Activities {
        NO_PREVIOUS_ACTIVITY,LOAN_ACTIVITY, SEARCH_ACTIVITY, OTHER_ACTIVITY2
    }

    lateinit var previousActivity : Activities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            modules(mainModule)
        }


        // Simple var allowing to know the activity from which we come from
        previousActivity = Activities.NO_PREVIOUS_ACTIVITY

        estateViewModel.allEstates.observe(this, Observer {
            // Todo : populating RecyclerView here
            it.forEach { estate ->
                Log.i("MainActivity", estate.toString())
            }
        })

    }

    override fun onResume() {
        super.onResume()

        when (previousActivity)
        {
            Activities.LOAN_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.loan_click_transition)
                main_activity_motionLayout.transitionToStart()
            }
            Activities.SEARCH_ACTIVITY -> {
                main_activity_motionLayout.setTransition(R.id.search_click_transition)
                main_activity_motionLayout.transitionToStart()
            }

        }
        main_activity_motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (p1 == R.id.loan_click_end){
                    previousActivity = Activities.LOAN_ACTIVITY
                    startActivity(Intent(this@MainActivity, LoanActivity::class.java))
                }
                if (p1 == R.id.search_click_end){
                    previousActivity = Activities.SEARCH_ACTIVITY
                    startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                }

            }
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
        })
    }
}