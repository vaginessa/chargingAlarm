package com.chargingwatts.chargingalarm.util.battery

import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.ui.vibrate.VibrationManager
import com.chargingwatts.chargingalarm.util.AlarmMediaManager
import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions.ALARM_TYPE.*
import com.chargingwatts.chargingalarm.util.settings.SettingsProfile
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryAlarmManager @Inject constructor(context: Context) : ContextWrapper(context) {
    private lateinit var alarmMediaManager: AlarmMediaManager
    private lateinit var vibrationManager: VibrationManager


    init {
        applicationContext.let {
            alarmMediaManager = AlarmMediaManager(it)
            vibrationManager = VibrationManager(it)

        }
    }


    fun initateAlarm(@IntegerDefinitions.ALARM_TYPE alarmType: Int) {
        when ( alarmType) {
            BATTERY_HIGH_LEVEL_ALARM -> {
                displayAlarmScreen()
                startAlarmTone()
                startVibration()
            }
            BATTERY_LOW_LEVEL_ALARM -> {
                displayAlarmScreen()
                startAlarmTone()
                startVibration()
            }
            BATTERY_HIGH_TEMPERATURE_ALARM -> {
                displayAlarmScreen()
                startAlarmTone()
                startVibration()
            }
            NONE->{

            }
        }

    }

    fun stopAlarm() {
        //TODO DECIDE WHICH SCREEN TO OPEN ON STOP ALARM AND INCLUDE IT HERE
        stopAlarmTone()
        stopVibration()
    }

    fun displayAlarmScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun startAlarmTone() {
        ///TODO uri of alarm tone selected by user to be used here
        val uri = Uri.parse("android.resource://$packageName/raw/ultra_alarm")
        alarmMediaManager.playAlarmSound(uri)

    }

    fun stopAlarmTone() {
        alarmMediaManager.stopAlarmSound()
    }

    fun startVibration() {
        vibrationManager.makePattern().beat(2000).rest(1000).playPattern(30)
    }

    fun stopVibration() {
        vibrationManager.stop()
    }

    fun checkAlarmTypeAndStartAlarm(batteryProfile: BatteryProfile, settingsProfile: SettingsProfile) {
        val alarmType = checkForAlarmType(batteryProfile, settingsProfile)
        initateAlarm(alarmType)
    }


    fun checkForAlarmType(batteryProfile: BatteryProfile, settingsProfile: SettingsProfile):  Int {

        return when {
            isHighBatteryAlarm(batteryProfile.remainingPercent, settingsProfile.batteryHighLevelPercent) ->
                BATTERY_HIGH_LEVEL_ALARM
            isLowBatteryAlarm(batteryProfile.remainingPercent, settingsProfile.batteryLowLevelPercent) ->
                BATTERY_LOW_LEVEL_ALARM
            isHighTemperatureAlarm(batteryProfile.recentBatteryTemperature, settingsProfile.batteryHighTemperature) ->
                BATTERY_HIGH_TEMPERATURE_ALARM
            else -> NONE
        }
    }

    fun isHighBatteryAlarm(remainingPercent: Int?, batteryHighLevelPercent: Int): Boolean {
        remainingPercent?.let {
            if (it >= batteryHighLevelPercent) {
                return true
            }
        }
        return false
    }

    fun isLowBatteryAlarm(remainingPercent: Int?, batteryLowLevelPercent: Int): Boolean {
        remainingPercent?.let {
            if (it <= batteryLowLevelPercent) {
                return true
            }
        }
        return false
    }

    fun isHighTemperatureAlarm(recentBatteryTemperature: Float?, batteryHighTemperature: Float): Boolean {
        recentBatteryTemperature?.let {
            if (it >= batteryHighTemperature) {
                return true
            }
        }
        return false
    }


}