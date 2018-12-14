package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import com.chargingwatts.chargingalarm.AlarmActivity
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.di.AppInjector
import com.chargingwatts.chargingalarm.ui.vibrate.VibrationManager
import com.chargingwatts.chargingalarm.util.AlarmMediaManager
import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions
import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions.ALARM_TYPE.*
import com.chargingwatts.chargingalarm.util.constants.IntegerDefinitions.STOP_ALARM_EVENT_TYPE.*

import com.chargingwatts.chargingalarm.util.logging.EventLogger
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.BATTERY_LEVEL_LOW_CHANNEL_NOTIFICATION_ID
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.BATTERY_TEMPERATURE_HIGH_CHANNEL_NOTIFICATION_ID
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.createHighBatteryAlarmNotificationBodyString
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.createHighBatteryAlarmNotificationTitleString
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.createHighTempAlarmNotificationBodyString
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.createHighTempAlarmNotificationTitleString
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.createLowBatteryAlarmNotificationBodyString
import com.chargingwatts.chargingalarm.util.notification.NotificationHelper.Companion.createLowBatteryAlarmNotificationTitleString
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import com.chargingwatts.chargingalarm.util.settings.SettingsProfile
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton
import com.chargingwatts.chargingalarm.R


@Singleton
class BatteryAlarmManager @Inject constructor(context: Context, val settingsManager: SettingsManager,
                                              val alarmMediaManager: AlarmMediaManager, val vibrationManager: VibrationManager,
                                              val notificationHelper: NotificationHelper) : ContextWrapper(context) {


    private fun initateAlarm(@IntegerDefinitions.ALARM_TYPE alarmType: Int, batteryProfile: BatteryProfile) {
        if (alarmType == BATTERY_HIGH_LEVEL_ALARM) {
            val settingsProfile = settingsManager.getSettingsProfile()
            if (settingsProfile.userAlarmPreference) {
                startHighBatteryAlarm(batteryProfile)
            }
        }
        if (alarmType == BATTERY_LOW_LEVEL_ALARM) {
            startLowBatteryAlarm(batteryProfile)
        }

        if (alarmType == BATTERY_HIGH_TEMPERATURE_ALARM) {
            startHighTemperatureAlarm(batteryProfile)
        }
    }

    private fun stopAlarm(@IntegerDefinitions.STOP_ALARM_EVENT_TYPE stopAlarmType: Int, batteryProfile: BatteryProfile) {
        if(stopAlarmType  == STOP_BATTERY_HIGH_LEVEL_ALARM){
            stopHighBatteryAlarm()
        }
        if(stopAlarmType == STOP_BATTERY_LOW_LEVEL_ALARM){
            stopLowBatteryAlarm()
        }
        if(stopAlarmType == STOP_BATTERY_HIGH_TEMPERATURE_ALARM){
            stopHighTemperatureAlarm()
        }

    }

    private fun startHighBatteryAlarm(batteryProfile: BatteryProfile) {
        val settingsProfile = settingsManager.getSettingsProfile()

        displayAlarmScreen(getString(R.string.show_full_battery_alarm_msg), getString(R.string.show_stop_alarm_msg))
        notificationHelper.apply {
            //   cancelNotification(BATTERY_LEVEL_LOW_CHANNEL_NOTIFICATION_ID)
            notify(BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID,
                    getHighBatteryNotificationBuilder(createHighBatteryAlarmNotificationTitleString(this, batteryProfile, settingsProfile),
                            createHighBatteryAlarmNotificationBodyString(this, batteryProfile, settingsProfile)))
        }
        if (shouldVibrate()) {
            startVibration()
        }
        if (shouldRunAlarmTone()) {
            startAlarmTone()
        }

        batteryProfile.let {
            EventLogger.logHighBatteryNotificationUpdatedEvent(it, settingsProfile)
        }
    }

    private fun startLowBatteryAlarm(batteryProfile: BatteryProfile) {
//        displayAlarmScreen(getString(R.string.show_low_battery_alarm_msg), getString(R.string.show_stop_alarm_msg))

        val settingsProfile = settingsManager.getSettingsProfile()
        notificationHelper.apply {
            cancelNotification(BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID)
            notify(BATTERY_LEVEL_LOW_CHANNEL_NOTIFICATION_ID,
                    getLowBatteryNotificationBuilder(createLowBatteryAlarmNotificationTitleString(this, batteryProfile, settingsProfile),
                            createLowBatteryAlarmNotificationBodyString(this, batteryProfile, settingsProfile)))
        }
//        if (shouldVibrate()) {
//            startVibration()
//        }
//        if (shouldRunAlarmTone()) {
//            startAlarmTone()
//        }

        batteryProfile.let {
            EventLogger.logLowBatteryNotificationUpdatedEvent(it, settingsProfile)
        }
    }

    private fun startHighTemperatureAlarm(batteryProfile: BatteryProfile) {
//        displayAlarmScreen(getString(R.string.show_high_temp_battery_alarm_msg), getString(R.string.show_stop_alarm_msg))

        val settingsProfile = settingsManager.getSettingsProfile()
        notificationHelper.apply {
            notify(BATTERY_TEMPERATURE_HIGH_CHANNEL_NOTIFICATION_ID,
                    getBatteryHighTempNotificationBuilder(createHighTempAlarmNotificationTitleString(this, batteryProfile, settingsProfile),
                            createHighTempAlarmNotificationBodyString(this, batteryProfile, settingsProfile)))
        }
//        if (shouldVibrate()) {
//            startVibration()
//        }
//        if (shouldRunAlarmTone()) {
//            startAlarmTone()
//        }

        batteryProfile.let {
            EventLogger.logBatteryHighTempNotificationUpdatedEvent(it, settingsProfile)
        }
    }

    private fun stopHighBatteryAlarm() {
        if (AppInjector.isAlarmScreenVisible()) {
            displayHomeScreen()
        }
        notificationHelper.apply {
            cancelNotification(BATTERY_LEVEL_HIGH_CHANNEL_NOTIFICATION_ID)
        }
        stopAlarmTone()
        stopVibration()
    }

    fun stopLowBatteryAlarm() {
        notificationHelper.apply {
            cancelNotification(BATTERY_LEVEL_LOW_CHANNEL_NOTIFICATION_ID)
        }
//        displayHomeScreen()
//        stopAlarmTone()
//        stopVibration()

    }

    fun stopHighTemperatureAlarm() {
        notificationHelper.cancelNotification(BATTERY_TEMPERATURE_HIGH_CHANNEL_NOTIFICATION_ID)
//        displayHomeScreen()
//        stopAlarmTone()
//        stopVibration()

    }

    fun stopAllAlarms() {
        stopHighBatteryAlarm()
        stopLowBatteryAlarm()
        stopHighTemperatureAlarm()

    }


    private fun displayAlarmScreen(alarmMsg: String, stopMsg: String) {
        if (!AppInjector.isAlarmScreenVisible()) {
            AlarmActivity.launch(this, alarmMsg, stopMsg)
        }
    }

    private fun displayHomeScreen() {
        if (!AppInjector.isHomeScreenVisible()) {

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
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
        if(isHighBatteryAlarm(batteryProfile, settingsProfile.batteryHighLevelPercent)){
            initateAlarm(BATTERY_HIGH_LEVEL_ALARM, batteryProfile)
        }
        if(isLowBatteryAlarm(batteryProfile, settingsProfile.batteryLowLevelPercent) ){
            initateAlarm(BATTERY_LOW_LEVEL_ALARM, batteryProfile)
        }
        if(isHighTemperatureAlarm(batteryProfile.recentBatteryTemperature, settingsProfile.batteryHighTemperature)){
            initateAlarm(BATTERY_HIGH_TEMPERATURE_ALARM, batteryProfile)
        }

    }


    private fun isHighBatteryAlarm(batteryProfile: BatteryProfile, batteryHighLevelPercent: Int): Boolean {
        batteryProfile.let { bProfile ->
            if (bProfile.isCharging != null && bProfile.isCharging && bProfile.remainingPercent != null && bProfile.remainingPercent >= batteryHighLevelPercent) {
                return true
            }
        }
        return false
    }

    private fun isLowBatteryAlarm(bProfile: BatteryProfile, batteryLowLevelPercent: Int): Boolean {
        bProfile.apply {
            if (remainingPercent != null && remainingPercent <= batteryLowLevelPercent) {
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


    fun checkStopAlarmTypeAndStopAlarm(batteryProfile: BatteryProfile) {
        val settingsProfile = settingsManager.getSettingsProfile()
        val stopAlarmType = checkForStopAlarmEvent(batteryProfile, settingsProfile)
        stopAlarm(stopAlarmType, batteryProfile)
    }

    private fun checkForStopAlarmEvent(batteryProfile: BatteryProfile, settingsProfile: SettingsProfile): Int {

        return when {
            isStopHighBatteryAlarmEvent(batteryProfile, settingsProfile.batteryHighLevelPercent) ->
                STOP_BATTERY_HIGH_LEVEL_ALARM
            isStopLowBatteryAlarmEvent(batteryProfile, settingsProfile.batteryLowLevelPercent)
            -> STOP_BATTERY_LOW_LEVEL_ALARM
            isStopHighTemperatureAlarmEvent(batteryProfile, settingsProfile.batteryHighTemperature) ->
                STOP_BATTERY_HIGH_TEMPERATURE_ALARM

            else -> IntegerDefinitions.STOP_ALARM_EVENT_TYPE.NONE
        }
    }

    private fun isStopHighBatteryAlarmEvent(batteryProfile: BatteryProfile, batteryHighLevelPercent: Int): Boolean {
        batteryProfile.let { bProfile ->
            if ((bProfile.isCharging != null && !bProfile.isCharging && bProfile.remainingPercent != null && bProfile.remainingPercent >= batteryHighLevelPercent)
                    || (bProfile.remainingPercent != null && bProfile.remainingPercent < batteryHighLevelPercent)) {
                return true
            }
        }
        return false
    }

    private fun isStopLowBatteryAlarmEvent(bProfile: BatteryProfile, batteryLowLevelPercent: Int): Boolean {
        bProfile.apply {
            if (remainingPercent != null && remainingPercent > batteryLowLevelPercent) {
                return true
            }
        }
        return false
    }

    private fun isStopHighTemperatureAlarmEvent(bProfile: BatteryProfile, batteryHighTemperature: Float): Boolean {
        bProfile.apply {
            if (remainingPercent != null && remainingPercent < batteryHighTemperature) {
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