package com.chargingwatts.chargingalarm.util.battery

import AppConstants.LOG_CHARGING_ALARM
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDaoWrapper
import com.chargingwatts.chargingalarm.di.component.DaggerAppComponent
import com.chargingwatts.chargingalarm.di.module.BatteryUpdateWorkerModule
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject

class BatteryUpdateWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    @Inject
    lateinit var mBatteryProfileDaoWrapper: BatteryProfileDaoWrapper

    @Inject
    lateinit var mAppExecutors: AppExecutors

    @Inject
    lateinit var mNotificationHelper: NotificationHelper

    override fun doWork(): Result {

        DaggerAppComponent.builder().applicationContext(applicationContext = applicationContext).build().newBatteryUpdateWorkerComponent(BatteryUpdateWorkerModule()).inject(this)

//        val sendIntent = Intent(applicationContext, HomeActivity::class.java)
//        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        getApplicationContext().startActivity(sendIntent)


        val lBatteryProfile = updateBatteryProfile()
        lBatteryProfile?.isCharging?.apply {
            when (this) {
                true -> {
        BatteryMonitoringService.startInForeground(applicationContext)
                }
                false ->{
                    BatteryMonitoringService.stopService(applicationContext)
                }
            }
        }

//        outputData = getBatteryProfileData()
        return Result.SUCCESS
    }

    fun updateBatteryProfile(): BatteryProfile? {
        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
        var lBatteryProfile: BatteryProfile? = null
        intent?.let { batteryIntent ->
            lBatteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent, applicationContext)

            lBatteryProfile?.let {
                mNotificationHelper.apply {
                    notify(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, it), ""))
                }

                mAppExecutors.diskIO().execute {
                    mBatteryProfileDaoWrapper.insert(it)
                }
            }
        }
        return lBatteryProfile
    }

//    fun getBatteryProfileData(): Data  {
//        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
//            applicationContext.registerReceiver(null, ifilter)
//        }
//
//        val dataBuilder = Data.Builder()
//        intent?.let { batteryIntent ->
//            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent)
//            batteryProfile?.let {
//                dataBuilder.putAll(BatteryProfileUtils.convertBatteryProfileToMap(it))
//            }
//        }
//        return dataBuilder.build()
//    }

}