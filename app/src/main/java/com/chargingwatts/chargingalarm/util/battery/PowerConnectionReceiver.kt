package com.chargingwatts.chargingalarm.util.battery

import AppConstants
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDaoWrapper
import com.chargingwatts.chargingalarm.util.logging.EventLogger
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject
import javax.inject.Singleton

/*
As part of the Android 8.0 (API level 26) Background Execution Limits, apps that target the API
level 26 or higher can no longer register broadcast receivers for implicit broadcasts in their
manifest. However, several broadcasts are currently exempted from these limitations. PowerConnection
Reciever is not in these limitations
 */
@Singleton
class PowerConnectionReceiver @Inject constructor() : DaggerBroadcastReceiver() {

    @Inject
    lateinit var batteryProfileDaoWrapper: BatteryProfileDaoWrapper;
    @Inject
    lateinit var appExecutors: AppExecutors
    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    @Inject
    lateinit var mNotificationHelper: NotificationHelper
    @Inject
    lateinit var batteryAlarmManager: BatteryAlarmManager
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)


        if (intent == null) {
            return
        }
        context?.let {
            updateBatteryProfile(context)
        }
        EventLogger.logPowerConnectionEvent()
        if (intent.action == Intent.ACTION_POWER_CONNECTED) {
            context?.let {
                BatteryMonitoringService.startInForeground(it)
            }
            preferenceHelper.putBoolean(AppConstants.IS_CHARGING_PREFERENCE, true)
        } else if (intent.action == Intent.ACTION_POWER_DISCONNECTED) {
            context?.let {
                getBatteryProfile(context)?.let {
                    batteryAlarmManager.stopIfHighBatteryAlarm(it, settingsManager.getSettingsProfile())
                }
            }
            preferenceHelper.putBoolean(AppConstants.IS_CHARGING_PREFERENCE, false)
        }
    }

    private fun getBatteryProfile(context: Context): BatteryProfile? {
        var batteryProfile: BatteryProfile? = null
        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        intent?.let { batteryIntent ->
            batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent, context)
        }
        return batteryProfile
    }


    private fun updateBatteryProfile(context: Context) {
        getBatteryProfile(context)?.let {
            mNotificationHelper.apply {
                notify(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, getBatteryLevelNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, it), ""))
            }
            appExecutors.diskIO().execute { batteryProfileDaoWrapper.insert(it) }
            batteryAlarmManager.checkAlarmTypeAndStartAlarm(it, settingsManager.getSettingsProfile())
            Log.d("HASHOO - PCR", batteryAlarmManager.hashCode().toString())

        }
    }
}