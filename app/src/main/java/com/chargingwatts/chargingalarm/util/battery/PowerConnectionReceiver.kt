package com.chargingwatts.chargingalarm.util.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PowerConnectionReceiver @Inject constructor() : BroadcastReceiver() {

    @Inject
    lateinit var batteryProfileDao: BatteryProfileDao;
    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onReceive(context: Context?, intent: Intent?) {
         AndroidInjection.inject(this, context)
        if (intent?.action != Intent.ACTION_POWER_CONNECTED && intent?.action != Intent.ACTION_POWER_DISCONNECTED) {
            return
        }
        val batteryProfile: BatteryProfile? = BatteryProfileUtils.extractBatteryProfileFromIntent(intent,context)


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
                appExecutors.diskIO().execute { batteryProfileDao.insert(it) }
            }
        }
    }
}