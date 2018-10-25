package com.chargingwatts.chargingalarm.util.battery

import AppConstants.LOG_CHARGING_ALARM
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import dagger.android.DaggerService
import javax.inject.Inject


class BatteryMonitoringService : DaggerService() {

    @Inject
    lateinit var mBatteryChangeReciever: BatteryChangeReciever

    @Inject
    lateinit var mNotificationHelper: NotificationHelper

    @Inject
    lateinit var mBatteryProfileDao: BatteryProfileDao

    @Inject
    lateinit var mAppExecutors: AppExecutors

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_CHARGING_ALARM, "onStartCommand")

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        val lBatteryProfileIntent = registerReceiver(mBatteryChangeReciever, intentFilter)
        lBatteryProfileIntent?.let { it ->
            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(it, this)
            batteryProfile?.let {
                mNotificationHelper.apply {
                    startForeground(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, mNotificationHelper.getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, batteryProfile), "").build())
                }
                mAppExecutors.diskIO().execute {
                     mBatteryProfileDao.insert(batteryProfile)
                }
            }
        }



        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // There are Bound an Unbound Services - you should read something about
        // that. This one is an Unbounded Service.
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBatteryChangeReciever)
    }

    companion object {
        @JvmStatic
        fun startInForeground(context: Context) {
            context.applicationContext?.apply{
                val serviceIntent = Intent(this, BatteryMonitoringService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)

                }
            }

        }

        @JvmStatic
        fun stopService(context: Context) {
            context.applicationContext?.apply{
                stopService(Intent(this,BatteryMonitoringService::class.java))
            }
        }

    }


}
