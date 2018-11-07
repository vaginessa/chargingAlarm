package com.chargingwatts.chargingalarm.util.battery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.vo.*


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
        val lBatteryRemainingPercent = (lBatteryLevel.toFloat() / lBatteryScale.toFloat() * 100).toInt()
        val lRecentBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        val lRecentBatteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val lBatteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
        val lIsCharging = checkIfPowerConnected(lBatteryPlugType)
        var lBatteryTotalCapacity = -1
        var lBatteryRemainingCapacity = -1
        context?.let {
            lBatteryTotalCapacity = getTotalCapacity(context)
            lBatteryRemainingCapacity = getBatteryRemainingCapacity(context)

        }

        return BatteryProfile(
//                currentTimeStamp = lCurrentTimeStamp,
                batteryStatusType = lBatteryStatusType,
                batteryHealthType = lBatteryHealthType, batteryPlugType = lBatteryPlugType,
                batteryPresent = lBatteryPresent,
                recentBatteryVoltage = lRecentBatteryVoltage / 1000f,
                recentBatteryTemperature = lRecentBatteryTemperature / 10f,
                batteryTechnology = lBatteryTechnology,
                totalCapacity = lBatteryTotalCapacity,
                remainingCapacity = lBatteryRemainingCapacity,
                remainingPercent = lBatteryRemainingPercent,
                isCharging = lIsCharging)


    }

    fun getTotalCapacity(context: Context): Int {
        val primaryTotalCapacity = getPrimaryTotalCapacity(context)

        return if (primaryTotalCapacity > 0) {
            primaryTotalCapacity
        } else {
            getSecondaryTotalCapacity(context)
        }

    }


    fun getSecondaryTotalCapacity(ctx: Context): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mBatteryManager = ctx.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
            val capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            return ((chargeCounter / capacity * 100f) / 1000).toInt()
        }

        return -1
    }

    @SuppressLint("PrivateApi")
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
            return ((percentRemainingBattery / 100f) * getTotalCapacity(context)).toInt()
        }
        return -1

    }

    fun checkIfPowerConnected(batteryPlugType: Int?): Boolean? {
        var isPlugged: Boolean? = null

        batteryPlugType?.let { plugType ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                isPlugged = plugType.equals(BatteryManager.BATTERY_PLUGGED_AC) or
                        plugType.equals(BatteryManager.BATTERY_PLUGGED_USB) or
                        plugType.equals(BatteryManager.BATTERY_PLUGGED_WIRELESS)
            } else {
                isPlugged = plugType.equals(BatteryManager.BATTERY_PLUGGED_AC) or
                        plugType.equals(BatteryManager.BATTERY_PLUGGED_USB)
            }

        }

        return isPlugged
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
                    CN_VOLTAGE to batteryProfile.recentBatteryVoltage,
                    CN_TEMPERATURE to batteryProfile.recentBatteryTemperature,
                    CN_TECHNOLOGY to batteryProfile.batteryTechnology,
                    CN_TOTAL_CAPACITY to batteryProfile.totalCapacity,
                    CN_REMAINING_CAPACITY to batteryProfile.remainingCapacity,
                    CN_REMAINING_PERCENT to batteryProfile.remainingPercent,
                    CN_IS_CHARGING to batteryProfile.isCharging)

    fun convertMapToBatteryProfile(map: Map<String?, Any?>) =
            BatteryProfile(
                    currentTimeStamp = map[CN_TIME_STAMP] as Long?,
                    batteryStatusType = map[CN_STATUS] as Int?,
                    batteryHealthType = map[CN_HEALTH] as Int?,
                    batteryPlugType = map[CN_PLUGGED] as Int?,
                    batteryPresent = map[CN_PRESENT] as Boolean?,
                    recentBatteryVoltage = map[CN_VOLTAGE] as Float?,
                    recentBatteryTemperature = map[CN_TEMPERATURE] as Float?,
                    batteryTechnology = map[CN_TECHNOLOGY] as String?,
                    totalCapacity = map[CN_TOTAL_CAPACITY] as Int?,
                    remainingCapacity = map[CN_REMAINING_CAPACITY] as Int?,
                    remainingPercent = map[CN_REMAINING_PERCENT] as Int?,
                    isCharging = map[CN_IS_CHARGING]as Boolean?)

    fun getBatteryStatusString(context: Context, intStatus: Int?): String? {

        return when (intStatus) {
            BatteryManager.BATTERY_STATUS_CHARGING -> context.getString(R.string.BATTERY_STATUS_CHARGING)

            BatteryManager.BATTERY_STATUS_DISCHARGING -> context.getString(R.string.BATTERY_STATUS_DISCHARGING)

            BatteryManager.BATTERY_STATUS_FULL -> context.getString(R.string.BATTERY_STATUS_FULL)

            BatteryManager.BATTERY_STATUS_UNKNOWN -> context.getString(R.string.BATTERY_STATUS_UNKNOWN)

            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> context.getString(R.string.BATTERY_STATUS_NOT_CHARGING)
            else -> null
        }
    }

    @JvmStatic
    fun getPlugTypeString(intPlugType: Int?, context: Context): String? {

        return when (intPlugType) {
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> context.getString(R.string.BATTERY_PLUGGED_WIRELESS)

            BatteryManager.BATTERY_PLUGGED_USB -> context.getString(R.string.BATTERY_PLUGGED_USB)

            BatteryManager.BATTERY_PLUGGED_AC -> context.getString(R.string.BATTERY_PLUGGED_AC)

            else -> null
        }
    }

    fun getHealthStatusString(inthealthStatus: Int?, context: Context): String? {

        return when (inthealthStatus) {
            BatteryManager.BATTERY_HEALTH_COLD -> context.getString(R.string.BATTERY_HEALTH_COLD)

            BatteryManager.BATTERY_HEALTH_DEAD -> context.getString(R.string.BATTERY_HEALTH_DEAD)

            BatteryManager.BATTERY_HEALTH_GOOD -> context.getString(R.string.BATTERY_HEALTH_GOOD)

            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> context.getString(R.string.BATTERY_HEALTH_OVER_VOLTAGE)

            BatteryManager.BATTERY_HEALTH_OVERHEAT -> context.getString(R.string.BATTERY_HEALTH_OVERHEAT)

            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> context.getString(R.string.BATTERY_HEALTH_UNSPECIFIED_FAILURE)

            BatteryManager.BATTERY_HEALTH_UNKNOWN -> context.getString(R.string.BATTERY_HEALTH_UNKNOWN)
            else -> null
        }
    }
}

