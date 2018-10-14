package com.chargingwatts.chargingalarm.util.battery

import android.content.Intent
import android.os.BatteryManager
import com.chargingwatts.chargingalarm.vo.BatteryProfile

object BatteryProfileIntentExtractor {

    fun extractBatteryProfileFromIntent(intent: Intent): BatteryProfile? {
        if (!isBatteryIntent(intent)) {
            return null
        }

        val lCurrentTimeStamp = System.currentTimeMillis()
        val lBatteryStatusType = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val lBatteryHealthType = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val lBatteryPlugType = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val lBatteryPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
        val lBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val lBatteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val lRecentBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        val lRecentBatteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val lBatteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

        val batteryProfile = BatteryProfile(
//                currentTimeStamp = lCurrentTimeStamp,
                batteryStatusType = lBatteryStatusType,
                batteryHealthType = lBatteryHealthType, batteryPlugType = lBatteryPlugType,
                batteryPresent = lBatteryPresent, batteryLevel = lBatteryLevel,
                batteryScale = lBatteryScale,
                recentBatteryVoltage = lRecentBatteryVoltage,
                recentBatteryTemperature = lRecentBatteryTemperature,
                batteryTechnology = lBatteryTechnology)

        return batteryProfile


    }

    fun isBatteryIntent(intent: Intent) =
            intent.action == Intent.ACTION_POWER_CONNECTED || intent.action == Intent.ACTION_POWER_DISCONNECTED
                    || intent.action == Intent.ACTION_BATTERY_CHANGED || intent.action == Intent.ACTION_BATTERY_LOW ||
                    intent.action == Intent.ACTION_BATTERY_OKAY
}