package com.chargingwatts.chargingalarm.util.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryChangeReciever @Inject constructor(val batteryProfileDao: BatteryProfileDao, val appExecutors: AppExecutors, val notificationHelper: NotificationHelper) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { batteryIntent ->
            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent, context)
            batteryProfile?.let {
                notificationHelper.apply {
                    notify(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, batteryProfile), ""))
                }
                appExecutors.diskIO().execute {
                    batteryProfileDao.insert(it)
                }
            }
        }
    }
}