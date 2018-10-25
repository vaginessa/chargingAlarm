package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.util.logging.EventLogger
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PowerConnectionReceiver @Inject constructor() : DaggerBroadcastReceiver() {

    @Inject
    lateinit var batteryProfileDao: BatteryProfileDao;
    @Inject
    lateinit var appExecutors: AppExecutors


    @Inject
    lateinit var mNotificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("ACTIONAA", "clled")
        if(intent == null){
            return
        }
        EventLogger.logPowerConnectionEvent()
        if(intent.action == Intent.ACTION_POWER_CONNECTED){
            context?.let{
                BatteryMonitoringService.startInForeground(it)
            }
        }
        else if(intent.action == Intent.ACTION_POWER_DISCONNECTED){
            context?.let{
                BatteryMonitoringService.stopService(it)
            }
        }

        val batteryProfile: BatteryProfile? = BatteryProfileUtils.extractBatteryProfileFromIntent(intent, context)


        val isCharging: Boolean = batteryProfile?.batteryStatusType == BatteryManager.BATTERY_STATUS_CHARGING
                || batteryProfile?.batteryStatusType == BatteryManager.BATTERY_STATUS_FULL

        context?.let {
            updateBatteryProfile(context)
        }
    }

    fun updateBatteryProfile(context: Context) {
        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        intent?.let { batteryIntent ->
            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent, context)
            batteryProfile?.let {
                mNotificationHelper.apply {
                    notify(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, batteryProfile), ""))
                }
                appExecutors.diskIO().execute { batteryProfileDao.insert(it) }
            }
        }
    }
}