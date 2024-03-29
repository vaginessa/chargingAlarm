package com.chargingwatts.chargingalarm.util.battery

import AppConstants
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDaoWrapper
import com.chargingwatts.chargingalarm.util.logging.LoggingHelper
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryChangeReciever @Inject constructor(val batteryProfileDaoWrapper: BatteryProfileDaoWrapper,
                                                val appExecutors: AppExecutors,
                                                val notificationHelper: NotificationHelper,
                                                val batteryAlarmManager: BatteryAlarmManager) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        LoggingHelper.d(AppConstants.LOG_CHARGING_ALARM, BatteryChangeReciever::class.java.simpleName + " - onReceive")

        intent?.let { batteryIntent ->
            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent, context)
            batteryProfile?.let {
                //                notificationHelper.apply {
//                    notify(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, batteryProfile), ""))
//                }
                appExecutors.diskIO().execute {
                    batteryProfileDaoWrapper.insert(it)
                }
                it.isCharging.apply {
                    when (this) {
                        true -> {
                            context?.applicationContext?.apply {
                                BatteryMonitoringService.startInForeground(this)
                            }
                        }
                        false -> {
                            context?.applicationContext?.apply {
                                BatteryMonitoringService.stopService(this)
                            }

                        }
                    }
                }

                batteryAlarmManager.checkAlarmTypeAndStartAlarm(it)
                batteryAlarmManager.checkStopAlarmTypeAndStopAlarm(it)
                Log.d("HASHOO - BCR", batteryAlarmManager.hashCode().toString())
            }
        }
    }


    fun registerReciever(context: Context) {
        context.apply {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
            registerReceiver(this@BatteryChangeReciever, intentFilter)
        }
    }

    fun unregisterReciever(context: Context) {
        context.apply {
            try{
                unregisterReceiver(this@BatteryChangeReciever)
            }
            catch (exception: Exception){

            }
        }
    }

}