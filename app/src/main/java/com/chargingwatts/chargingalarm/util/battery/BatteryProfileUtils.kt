package com.chargingwatts.chargingalarm.util.battery

import android.content.Intent
import android.os.BatteryManager
import com.chargingwatts.chargingalarm.vo.*

object BatteryProfileUtils {

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


    fun convertBatteryProfileToMap(batteryProfile: BatteryProfile) =
            mapOf(CN_TIME_STAMP to batteryProfile.currentTimeStamp,
                    CN_STATUS to batteryProfile.batteryStatusType,
                    CN_HEALTH to batteryProfile.batteryHealthType,
                    CN_PLUGGED to batteryProfile.batteryPlugType,
                    CN_PRESENT to batteryProfile.batteryPresent,
                    CN_LEVEL to batteryProfile.batteryLevel,
                    CN_SCALE to batteryProfile.batteryScale,
                    CN_VOLTAGE to batteryProfile.recentBatteryVoltage,
                    CN_TEMPERATURE to batteryProfile.recentBatteryTemperature,
                    CN_TECHNOLOGY to batteryProfile.batteryTechnology)

    fun convertMapToBatteryProfile(map: Map<String?, Any?>) =
            BatteryProfile(
                    currentTimeStamp = map.get(CN_TIME_STAMP) as Long?,
                    batteryStatusType = map.get(CN_STATUS) as Int?,
                    batteryHealthType = map.get(CN_HEALTH) as Int?,
                    batteryPlugType = map.get(CN_PLUGGED) as Int?,
                    batteryPresent = map.get(CN_PRESENT) as Boolean?,
                    batteryLevel = map.get(CN_LEVEL) as Int?,
                    batteryScale = map.get(CN_SCALE) as Int?,
                    recentBatteryVoltage = map.get(CN_VOLTAGE) as Int?,
                    recentBatteryTemperature = map.get(CN_TEMPERATURE) as Int?,
                    batteryTechnology = map.get(CN_TECHNOLOGY) as String?)

}

