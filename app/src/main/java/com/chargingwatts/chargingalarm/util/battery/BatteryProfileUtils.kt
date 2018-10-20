package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.chargingwatts.chargingalarm.vo.*
import android.os.Build


object BatteryProfileUtils {

    fun extractBatteryProfileFromIntent(intent: Intent, context: Context?): BatteryProfile? {
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
        var lBatteryTotalCapacity = -1
        var lBatteryRemainingCapacity = -1
        context?.let{
            lBatteryTotalCapacity = getTotalCapacity(context)
            lBatteryRemainingCapacity = getBatteryRemainingCapacity(context)

        }

        val batteryProfile = BatteryProfile(
//                currentTimeStamp = lCurrentTimeStamp,
                batteryStatusType = lBatteryStatusType,
                batteryHealthType = lBatteryHealthType, batteryPlugType = lBatteryPlugType,
                batteryPresent = lBatteryPresent, batteryLevel = lBatteryLevel,
                batteryScale = lBatteryScale,
                recentBatteryVoltage = lRecentBatteryVoltage,
                recentBatteryTemperature = lRecentBatteryTemperature,
                batteryTechnology = lBatteryTechnology,
                totalCapacity = lBatteryTotalCapacity,
                remainingCapacity = lBatteryRemainingCapacity)

        return batteryProfile


    }

    fun getTotalCapacity(context: Context): Int{
        val primaryTotalCapacity = getPrimaryTotalCapacity(context)

        if(primaryTotalCapacity > 0){
            return  primaryTotalCapacity
        }
        else{
            return getSecondaryTotalCapacity(context)
        }

    }

    fun getSecondaryTotalCapacity(ctx: Context): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mBatteryManager = ctx.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
            val capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            return ((chargeCounter / capacity * 100f)/1000).toInt()
        }

        return -1
    }

    fun getPrimaryTotalCapacity(context: Context): Int {
        val mPowerProfile: Any
        var batteryCapacity = -1
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context::class.java)
                    .newInstance(context)

            batteryCapacity = (Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile) as Double).toInt()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        return batteryCapacity

    }
    fun getBatteryRemainingCapacity(context: Context): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mBatteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val percentRemainingBattery = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            return ((percentRemainingBattery/100f)* getTotalCapacity(context)).toInt()
        }
        return -1

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
                    CN_TECHNOLOGY to batteryProfile.batteryTechnology,
                    CN_TOTAL_CAPACITY to batteryProfile.totalCapacity,
                    CN_REMAINING_CAPACITY to batteryProfile.remainingCapacity)

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
                    batteryTechnology = map.get(CN_TECHNOLOGY) as String?,
                    totalCapacity = map.get(CN_TOTAL_CAPACITY) as Int?,
                    remainingCapacity = map.get(CN_REMAINING_CAPACITY) as Int?)

}

