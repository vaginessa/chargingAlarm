package com.chargingwatts.chargingalarm

import android.os.Bundle
import android.os.Vibrator
import android.view.WindowManager
import androidx.navigation.Navigation
import javax.inject.Inject

class HomeActivity : BaseActivity() {
    @Inject
    lateinit var appExecutors: AppExecutors


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)





    }

    private fun startVibration() {
        val vibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(2000, 2000, 2000, 2000, 2000)
        vibrator.vibrate(pattern, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onResume() {
        super.onResume()
    }



    override fun onPause() {
        super.onPause()
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.battery_activity_nav_host_fragment).navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
