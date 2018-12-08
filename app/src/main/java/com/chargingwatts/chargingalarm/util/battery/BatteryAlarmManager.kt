package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.di.AppInjector
import com.chargingwatts.chargingalarm.ui.vibrate.VibrationManager
import com.chargingwatts.chargingalarm.util.AlarmMediaManager
import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions
import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions.ALARM_TYPE.*
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import com.chargingwatts.chargingalarm.util.settings.SettingsProfile
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BatteryAlarmManager @Inject constructor(context: Context, val settingsManager: SettingsManager,
                                              val alarmMediaManager: AlarmMediaManager, val vibrationManager: VibrationManager) : ContextWrapper(context) {


    private fun initateAlarm(@IntegerDefinitions.ALARM_TYPE alarmType: Int) {

        when (alarmType) {
            BATTERY_HIGH_LEVEL_ALARM -> {
                startHighBatteryAlarm()
            }
            BATTERY_LOW_LEVEL_ALARM -> {
                startLowBatteryAlarm()
            }
            BATTERY_HIGH_TEMPERATURE_ALARM -> {
                startHighTemperatureAlarm()
            }
            NONE -> {
                stopAllAlarms()

            }
        }

    }

    private fun startHighBatteryAlarm() {

        displayAlarmScreen()


        if (shouldVibrate()) {
            startVibration()
        }
        if (shouldRunAlarmTone()) {
            startAlarmTone()
        }
    }

    private fun startLowBatteryAlarm() {
        displayAlarmScreen()
        if (shouldVibrate()) {
            startVibration()
        }
        if (shouldRunAlarmTone()) {
            startAlarmTone()
        }

    }

    private fun startHighTemperatureAlarm() {
        displayAlarmScreen()
        if (shouldVibrate()) {
            startVibration()
        }
        if (shouldRunAlarmTone()) {
            startAlarmTone()
        }

    }

    private fun stopHighBatteryAlarm() {
        stopAlarm()
    }

    fun stopLowBatteryAlarm() {
        stopAlarm()

    }

    fun stopHighTemperatureAlarm() {
        stopAlarm()

    }

    fun stopAllAlarms() {
        stopAlarm()

    }

    private fun stopAlarm() {
        //TODO DECIDE WHICH SCREEN TO OPEN ON STOP ALARM AND INCLUDE IT HERE
        stopAlarmTone()
        stopVibration()
    }


    fun stopIfHighBatteryAlarm(batteryProfile: BatteryProfile) {
        val settingsProfile = settingsManager.getSettingsProfile()
        if (checkForAlarmType(batteryProfile, settingsProfile) == BATTERY_HIGH_LEVEL_ALARM) {
            stopHighBatteryAlarm()
        }
    }


    private fun displayAlarmScreen() {
        if (!AppInjector.isAlarmScreenVisible()) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


//        NotificationHelper(this).apply {
//            notify(NotificationHelper.BATTERY_LEVEL_CHANNEL_NOTIFICATION_ID, getHighBatteryNotificationBuilder(NotificationHelper.createBatteryNotificationTitleString(this, null), ""))
//        }

    }

    private fun startAlarmTone() {
        ///TODO uri of alarm tone selected by user to be used here

        val uri = Uri.parse(settingsManager.getAlarmTonePreference())
        alarmMediaManager.playAlarmSound(uri, settingsManager.getAlarmVolumePreference())

    }

    private fun stopAlarmTone() {
        alarmMediaManager.stopAlarmSound()
    }

    private fun startVibration() {
        vibrationManager.makePattern().rest(1000).beat(2000).playPattern(20)
    }

    private fun stopVibration() {
        vibrationManager.stop()
    }

    fun checkAlarmTypeAndStartAlarm(batteryProfile: BatteryProfile) {
        val settingsProfile = settingsManager.getSettingsProfile()
        if (!settingsProfile.userAlarmPreference) {
            return
        }
        val alarmType = checkForAlarmType(batteryProfile, settingsProfile)
        initateAlarm(alarmType)
    }


    private fun checkForAlarmType(batteryProfile: BatteryProfile, settingsProfile: SettingsProfile): Int {

        return when {
            isHighBatteryAlarm(batteryProfile, settingsProfile.batteryHighLevelPercent) ->
                BATTERY_HIGH_LEVEL_ALARM
            isLowBatteryAlarm(batteryProfile.remainingPercent, settingsProfile.batteryLowLevelPercent) ->
                BATTERY_LOW_LEVEL_ALARM
            isHighTemperatureAlarm(batteryProfile.recentBatteryTemperature, settingsProfile.batteryHighTemperature) ->
                BATTERY_HIGH_TEMPERATURE_ALARM
            else -> NONE
        }
    }

    private fun isHighBatteryAlarm(batteryProfile: BatteryProfile, batteryHighLevelPercent: Int): Boolean {
        batteryProfile.let { bProfile ->
            if (bProfile.isCharging != null && bProfile.remainingPercent != null && bProfile.remainingPercent >= batteryHighLevelPercent) {
                return true
            }
        }
        return false
    }

    private fun isLowBatteryAlarm(remainingPercent: Int?, batteryLowLevelPercent: Int): Boolean {
        remainingPercent?.let {
            if (it <= batteryLowLevelPercent) {
                return true
            }
        }
        return false
    }

    private fun isHighTemperatureAlarm(recentBatteryTemperature: Float?, batteryHighTemperature: Float): Boolean {
        recentBatteryTemperature?.let {
            if (it >= batteryHighTemperature) {
                return true
            }
        }
        return false
    }

    private fun shouldRunAlarmTone(): Boolean {

        return settingsManager.getSoundPreference() && (alarmMediaManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT
                || (alarmMediaManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT &&
                settingsManager.getRingOnSilentModePreference()))
    }

    private fun shouldVibrate(): Boolean {

        return settingsManager.getVibrationPreference() && (alarmMediaManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT
                || (alarmMediaManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT &&
                settingsManager.getVibrateOnSilentModePreference()))
    }

}