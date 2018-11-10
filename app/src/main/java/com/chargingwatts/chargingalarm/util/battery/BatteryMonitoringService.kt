package com.chargingwatts.chargingalarm.util.battery

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDaoWrapper
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import dagger.android.AndroidInjection
import javax.inject.Inject


class BatteryMonitoringService : LifecycleService() {

    @Inject
    lateinit var mBatteryChangeReciever: BatteryChangeReciever

    @Inject
    lateinit var mNotificationHelper: NotificationHelper

    @Inject
    lateinit var mBatteryProfileDaoWrapper: BatteryProfileDaoWrapper

    @Inject
    lateinit var mAppExecutors: AppExecutors

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        applicationContext?.let {
            mBatteryChangeReciever.registerReciever(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
//        val lBatteryProfileIntent = registerReceiver(mBatteryChangeReciever, intentFilter)
        mBatteryProfileDaoWrapper.findRecentBatteryProfile().observe(this, Observer<BatteryProfile> { lbatteryProfile ->
            lbatteryProfile?.let {
                startForeground(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, mNotificationHelper.getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, lbatteryProfile), "").build())
                }
        })
//        lBatteryProfileIntent?.let { it ->
//            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(it, this)
//            batteryProfile?.let {
//                mNotificationHelper.apply {
//                    startForeground(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, mNotificationHelper.getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, batteryProfile), "").build())
//                }
//                mAppExecutors.diskIO().execute {
//                     mBatteryProfileDaoWrapper.insert(batteryProfile)
//                }
//            }
//        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        // There are Bound an Unbound Services - you should read something about
        // that. This one is an Unbounded Service.
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext?.let {
            mBatteryChangeReciever.unregisterReciever(it)
    }
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
