package com.chargingwatts.chargingalarm

import AppConstants.LOG_CHARGING_ALARM
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.util.battery.*
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import javax.inject.Inject

class HomeActivity : BaseActivity() {
    val NOTIFICATION_ID = 1
    private var mNotificationManager: NotificationManager? = null
    private val CHANNEL_ID = "channel_01"


    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever
    @Inject
    lateinit var powerConnectionReceiver: PowerConnectionReceiver
    @Inject
    lateinit var periodicBatteryUpdater: PeriodicBatteryUpdater
    @Inject
    lateinit var batterProfileDao: BatteryProfileDao
    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        BatteryProfileUtils.getPrimaryTotalCapacity(this)

//        notificationHelper.apply {
//            notify(NotificationHelper.BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID,getHighBatteryNotification("LEVEL", "ASFASFASF",50))
//        }
//        BatteryMonitoringService.startInForeground(this)

        mPreferenceHelper?.let{
            Log.d(LOG_CHARGING_ALARM, "Pref_Helper_not_null")

        }
        batteryChangeReciever.apply {
            Log.d(batteryChangeReciever::class.simpleName, "app not null")
        }

        powerConnectionReceiver.apply {
            Log.d(powerConnectionReceiver::class.simpleName, "app not null")
        }
        periodicBatteryUpdater.apply {
            Log.d(periodicBatteryUpdater::class.simpleName, "app not null")
        }
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)


        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryChangeReciever, intentFilter)


        findViewById<Button>(R.id.btn_stop_battery_update).setOnClickListener({
            PeriodicBatteryUpdater.stopPeriodicBatteryUpdate()
        })
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


        batterProfileDao.findRecentBatteryProfile().observe(this, Observer { batteryProfile ->
            val lBatteryPercent = batteryProfile?.remainingPercent
            lBatteryPercent?.let {
                findViewById<TextView>(R.id.tv_battery_level).text = "${lBatteryPercent}%"
                findViewById<ProgressBar>(R.id.pb_battery_level).progress = lBatteryPercent
            }

        })

        Log.d("MyBackgroundWorker", "BackgroundWorker is Running")
//        startVibration()
    }



    override fun onPause() {
        super.onPause()
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.battery_activity_nav_host_fragment).navigateUp()
    }

    override fun onDestroy() {
        unregisterReceiver(batteryChangeReciever)
        super.onDestroy()

    }

}
