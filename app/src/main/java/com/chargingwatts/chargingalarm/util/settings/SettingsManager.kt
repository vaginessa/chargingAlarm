package com.chargingwatts.chargingalarm.util.settings

import AppConstants.ALARM_TONE_PREF
import AppConstants.ALARM_VOLUME_PREF
import AppConstants.BATTERY_HIGH_LEVEL_PREF
import AppConstants.BATTERY_HIGH_TEMPERATURE_PREF
import AppConstants.BATTERY_LOW_LEVEL_PREF
import AppConstants.IS_SOUND_ENABLED_PREF
import AppConstants.IS_VIBRATION_ENABLED_PREF
import AppConstants.RING_ON_SILENT_MODE_PREF
import AppConstants.USER_ALARM_PREFERENCE
import AppConstants.VIBRATE_ON_SILENT_MODE_PREF
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import com.chargingwatts.chargingalarm.util.AlarmMediaManager
import com.chargingwatts.chargingalarm.util.preference.*
import javax.inject.Inject
import javax.inject.Singleton

const val DEFAULT_BATTERY_HIGH_LEVEL = 100
const val DEFAULT_BATTERY_LOW_LEVEL = 15
//keep this temperature between 30 and 45
const val DEFAULT_BATTERY_HIGH_TEMPERATURE = 42f
const val DEFAULT_USER_ALARM_PREFERENCE = false
const val DEFAULT_IS_VIBRATION_ENABLED = true
const val DEFAULT_IS_SOUND_ENABLED = true
const val DEFAULT_RING_IN_SILENT_MODE = true
const val DEFAULT_VIBRATE_IN_SILENT_MODE = true






@Singleton
class SettingsManager @Inject constructor(context : Context, val preferenceHelper: PreferenceHelper, val alarmMediaManager: AlarmMediaManager):
                            ContextWrapper(context){

     val DEFAULT_ALARM_VOLUME = alarmMediaManager.getMaxVoulume()
     val DEFAULT_ALARM_TONE_RINGTONE =  "android.resource://$packageName/raw/ultra_alarm"


    fun getBatteryHighLevelPercentPreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(BATTERY_HIGH_LEVEL_PREF, DEFAULT_BATTERY_HIGH_LEVEL)
    }

    fun getBatteryHighLevelPercentPreference(): Int {
        return preferenceHelper.getInt(BATTERY_HIGH_LEVEL_PREF, DEFAULT_BATTERY_HIGH_LEVEL)
    }

    fun setBatteryHighLevelPercentPreference(batteryHighLevelPercent: Int) {
        preferenceHelper.putInt(BATTERY_HIGH_LEVEL_PREF, batteryHighLevelPercent)
    }

    fun getBatteryLowLevelPercentPreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(BATTERY_LOW_LEVEL_PREF, DEFAULT_BATTERY_LOW_LEVEL)
    }

    fun getBatteryLowLevelPercentPreference(): Int {
        return preferenceHelper.getInt(BATTERY_LOW_LEVEL_PREF, DEFAULT_BATTERY_LOW_LEVEL)
    }

    fun setBatteryLowLevelPercentPreference(batteryLowLevelPercent: Int) {
        preferenceHelper.putInt(BATTERY_LOW_LEVEL_PREF, batteryLowLevelPercent)
    }

    fun getBatteryHighTemperaturePreferenceLiveData(): SharedPreferenceLiveData<Float> {
        return preferenceHelper.getNewSharedPreference().floatLiveData(BATTERY_HIGH_TEMPERATURE_PREF, DEFAULT_BATTERY_HIGH_TEMPERATURE)
    }

    fun getBatteryHighTemperaturePreference(): Float {
        return preferenceHelper.getFloat(BATTERY_HIGH_TEMPERATURE_PREF, DEFAULT_BATTERY_HIGH_TEMPERATURE)
    }

    fun setBatteryHighTemperaturePreference(batteryHighTemperature: Float) {
        preferenceHelper.putFloat(BATTERY_HIGH_TEMPERATURE_PREF, batteryHighTemperature)
    }

    fun setUserAlarmPreference(userAlarmPreference: Boolean) {
        preferenceHelper.putBoolean(USER_ALARM_PREFERENCE, userAlarmPreference)
    }

    fun getUserAlarmPreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(USER_ALARM_PREFERENCE, DEFAULT_USER_ALARM_PREFERENCE)
    }

    fun getUserAlarmPreference(): Boolean {
        return preferenceHelper.getBoolean(USER_ALARM_PREFERENCE, DEFAULT_USER_ALARM_PREFERENCE)
    }

    fun setVibrationPreference(vibrationPreference:Boolean){
        preferenceHelper.putBoolean(IS_VIBRATION_ENABLED_PREF, vibrationPreference)
    }

    fun getVibrationPreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(IS_VIBRATION_ENABLED_PREF, DEFAULT_IS_VIBRATION_ENABLED)
    }

    fun getVibrationPreference(): Boolean {
        return preferenceHelper.getBoolean(IS_VIBRATION_ENABLED_PREF, DEFAULT_IS_VIBRATION_ENABLED)
    }

    fun getRingOnSilentModePreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(RING_ON_SILENT_MODE_PREF, DEFAULT_RING_IN_SILENT_MODE)
    }

    fun getRingOnSilentModePreference(): Boolean {
        return preferenceHelper.getBoolean(RING_ON_SILENT_MODE_PREF, DEFAULT_RING_IN_SILENT_MODE)
    }

    fun setRingOnSilentModePreference(ringOnSilentModePreference:Boolean){
        preferenceHelper.putBoolean(RING_ON_SILENT_MODE_PREF, ringOnSilentModePreference)
    }

    fun getVibrateOnSilentModePreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(VIBRATE_ON_SILENT_MODE_PREF, DEFAULT_VIBRATE_IN_SILENT_MODE)
    }

    fun getVibrateOnSilentModePreference(): Boolean {
        return preferenceHelper.getBoolean(VIBRATE_ON_SILENT_MODE_PREF, DEFAULT_VIBRATE_IN_SILENT_MODE)
    }

    fun setVibrateOnSilentModePreference(vibrateOnSilentModePreference:Boolean){
        preferenceHelper.putBoolean(VIBRATE_ON_SILENT_MODE_PREF, vibrateOnSilentModePreference)
    }
    fun setSoundPreference(vibrationPreference:Boolean){
        preferenceHelper.putBoolean(IS_SOUND_ENABLED_PREF, vibrationPreference)
    }

    fun getSoundPreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(IS_SOUND_ENABLED_PREF, DEFAULT_IS_SOUND_ENABLED)
    }

    fun getSoundPreference(): Boolean {
        return preferenceHelper.getBoolean(IS_SOUND_ENABLED_PREF,DEFAULT_IS_SOUND_ENABLED)
    }
    fun getAlarmVolumePreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(ALARM_VOLUME_PREF, DEFAULT_ALARM_VOLUME)
    }

    fun getAlarmVolumePreference(): Int {
        return preferenceHelper.getInt(ALARM_VOLUME_PREF, DEFAULT_ALARM_VOLUME)
    }

    fun setAlarmVolumePreference(alarmVolumeLevel: Int) {
        preferenceHelper.putInt(ALARM_VOLUME_PREF, alarmVolumeLevel)
    }




    fun getAlarmTonePreferenceLiveData(): SharedPreferenceLiveData<String> {
        return preferenceHelper.getNewSharedPreference().stringLiveData(ALARM_TONE_PREF, DEFAULT_ALARM_TONE_RINGTONE)
    }

    fun getAlarmTonePreference(): String {
        return preferenceHelper.getString(ALARM_TONE_PREF, DEFAULT_ALARM_TONE_RINGTONE)
    }

    fun setAlarmTonePreference(alarmToneUri: String) {
        preferenceHelper.putString(ALARM_TONE_PREF, alarmToneUri)
    }






    fun  getSettingsProfile(): SettingsProfile {
        val batteryHighLevelPercent = getBatteryHighLevelPercentPreference()
        val batteryLowLevelPercent = getBatteryLowLevelPercentPreference()
        val batteryHighTemperature = getBatteryHighTemperaturePreference()
        val userAlarmPreference = getUserAlarmPreference()
        val isVibrationEnabled = getVibrationPreference()
        val isSoundEnabled = getSoundPreference()
        val ringOnSilentMode = getRingOnSilentModePreference()
        val vibrateOnSilentMode = getVibrateOnSilentModePreference()

        return SettingsProfile(batteryHighLevelPercent, batteryLowLevelPercent, batteryHighTemperature,
                userAlarmPreference, isVibrationEnabled, isSoundEnabled, ringOnSilentMode,
                vibrateOnSilentMode)

    }

    fun getSettingsProfileLivedata(): SettingsProfileLiveData =
            SettingsProfileLiveData(
                    getBatteryHighLevelPercentPreferenceLiveData(),
                    getBatteryLowLevelPercentPreferenceLiveData(),
                    getBatteryHighTemperaturePreferenceLiveData(),
                    getUserAlarmPreferenceLiveData(),
                    getVibrationPreferenceLiveData(),
                    getSoundPreferenceLiveData(),
                    getRingOnSilentModePreferenceLiveData(),
                    getVibrateOnSilentModePreferenceLiveData()
            )


}