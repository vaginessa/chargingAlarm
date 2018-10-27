package com.chargingwatts.chargingalarm.util.logging

import com.chargingwatts.chargingalarm.vo.BatteryProfile
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

object EventLogger {


//    private fun getAnswersInstance() = FirebaseAnalytics.getInstance(getContext())
//    private fun getContext() = FirebaseApp.getInstance()?.applicationContext

     fun logBatteryNotificationUpdatedEvent(batteryProfile:BatteryProfile){
//        getAnswersInstance().logCustom(CustomEvent("battery_notification_updated").
//                putCustomAttribute("battery_level",batteryProfile.remainingPercent)
//                .putCustomAttribute("battery_status", BatteryProfileUtils.getBatteryStatusString(getContext(),batteryProfile.batteryStatusType)))
    }

    fun logPowerConnectionEvent(){
//        getAnswersInstance().logCustom(CustomEvent("power_connecttion_event"))

    }

}