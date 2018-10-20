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

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        BatteryProfileUtils.getPrimaryTotalCapacity(this)
        
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


        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryChangeReciever, intentFilter)

        batterProfileDao.findRecentBatteryProfile().observe(this, Observer { batteryProfile ->
            val lBatteryLevel = batteryProfile?.batteryLevel
            lBatteryLevel?.let {
                findViewById<TextView>(R.id.tv_battery_level).text = "${lBatteryLevel}%"
                findViewById<ProgressBar>(R.id.pb_battery_level).progress = lBatteryLevel
            }

        })

        Log.d("MyBackgroundWorker", "BackgroundWorker is Running")
        createNotificationChannel()
        sendNotification()
//        startVibration()
    }

    private fun createNotificationChannel() {
        val mNotificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = applicationContext.getString(R.string.app_name)
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun getNotification(): Notification {
        val text = "Background work being tested"
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentText(text)
                .setContentTitle("Background worker")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setVisibility(Notification.VISIBILITY_PUBLIC)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }

        return builder.build()
    }

    // Post a notification indicating whether a doodle was found.
    private fun sendNotification() {
        mNotificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val sendIntent = Intent(applicationContext, HomeActivity::class.java)


        //        mBuilder.setContentIntent(contentIntent);
        mNotificationManager?.notify(NOTIFICATION_ID, getNotification())
        //       startVibration()
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        applicationContext.startActivity(sendIntent)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryChangeReciever)
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.battery_activity_nav_host_fragment).navigateUp()
    }

}
