package com.chargingwatts.chargingalarm.util.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryChangeReciever @Inject constructor(val batteryProfileDao: BatteryProfileDao, val appExecutors: AppExecutors) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { batteryIntent ->
            val batteryProfile = BatteryProfileIntentExtractor.extractBatteryProfileFromIntent(batteryIntent)
            appExecutors.diskIO().execute {
                batteryProfile?.let { batteryProfileDao.insert(it) }
            }
        }
    }


}