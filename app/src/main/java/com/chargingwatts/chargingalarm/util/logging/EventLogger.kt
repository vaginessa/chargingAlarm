package com.chargingwatts.chargingalarm.util.logging

import android.content.Context
import android.os.Bundle
import com.chargingwatts.chargingalarm.util.BATTERY_NOTIFICATION_UPDATED
import com.chargingwatts.chargingalarm.util.BATTERY_PERCENT
import com.chargingwatts.chargingalarm.util.BATTERY_STATUS
import com.chargingwatts.chargingalarm.util.POWER_CONNECTED
import com.chargingwatts.chargingalarm.util.battery.BatteryProfileUtils
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

object EventLogger {


    private fun getFirebaseAnalyticsInstance(): FirebaseAnalytics? =
            getContext()?.let {

                FirebaseAnalytics.getInstance(it)
            }

    private fun getContext(): Context? = FirebaseApp.getInstance()?.applicationContext

    private fun logEvent(eventName: String, params: Bundle?) {
        getFirebaseAnalyticsInstance()?.logEvent(eventName, params)

    }

    fun logBatteryNotificationUpdatedEvent(batteryProfile: BatteryProfile) {
        val params = Bundle()
        batteryProfile.remainingPercent?.let {
            params.putInt(BATTERY_PERCENT, it)
        }
        getContext()?.let { context ->
            BatteryProfileUtils.getBatteryStatusString( batteryProfile.batteryStatusType, context)?.let { batteryStatusString ->
                params.putString(BATTERY_STATUS, batteryStatusString)
            }
        }
        logEvent(BATTERY_NOTIFICATION_UPDATED, params)
    }

    fun logPowerConnectionEvent() {
        logEvent(POWER_CONNECTED, null)

    }
}

