package com.chargingwatts.chargingalarm.util.logging

import com.chargingwatts.chargingalarm.util.battery.BatteryProfileUtils
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent

object EventLogger {


    private fun getAnswersInstance() = Answers.getInstance()
    private fun getContext() = getAnswersInstance().context.applicationContext

     fun logBatteryNotificationUpdatedEvent(batteryProfile:BatteryProfile){
        getAnswersInstance().logCustom(CustomEvent("battery_notification_updated").
                putCustomAttribute("battery_level",batteryProfile.remainingPercent)
                .putCustomAttribute("battery_status", BatteryProfileUtils.getBatteryStatusString(getContext(),batteryProfile.batteryStatusType)))
    }

    fun logPowerConnectionEvent(){
        getAnswersInstance().logCustom(CustomEvent("power_connecttion_event"))

    }

}