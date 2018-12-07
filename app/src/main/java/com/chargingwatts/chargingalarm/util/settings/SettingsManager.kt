package com.chargingwatts.chargingalarm.util.settings

import AppConstants.BATTERY_HIGH_LEVEL
import AppConstants.BATTERY_HIGH_TEMPERATURE
import AppConstants.BATTERY_LOW_LEVEL
import AppConstants.IS_SOUND_ENABLED
import AppConstants.IS_VIBRATION_ENABLED
import AppConstants.RING_ON_SILENT_MODE
import AppConstants.USER_ALARM_PREFERENCE
import AppConstants.VIBRATE_ON_SILENT_MODE
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
class SettingsManager @Inject constructor(val preferenceHelper: PreferenceHelper) {


    fun getBatteryHighLevelPercentPreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(BATTERY_HIGH_LEVEL, DEFAULT_BATTERY_HIGH_LEVEL)
    }

    fun getBatteryHighLevelPercentPreference(): Int {
        return preferenceHelper.getInt(BATTERY_HIGH_LEVEL, DEFAULT_BATTERY_HIGH_LEVEL)
    }

    fun setBatteryHighLevelPercentPreference(batteryHighLevelPercent: Int) {
        preferenceHelper.putInt(BATTERY_HIGH_LEVEL, batteryHighLevelPercent)
    }

    fun getBatteryLowLevelPercentPreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(BATTERY_LOW_LEVEL, DEFAULT_BATTERY_LOW_LEVEL)
    }

    fun getBatteryLowLevelPercentPreference(): Int {
        return preferenceHelper.getInt(BATTERY_LOW_LEVEL, DEFAULT_BATTERY_LOW_LEVEL)
    }

    fun setBatteryLowLevelPercentPreference(batteryLowLevelPercent: Int) {
        preferenceHelper.putInt(BATTERY_LOW_LEVEL, batteryLowLevelPercent)
    }

    fun getBatteryHighTemperaturePreferenceLiveData(): SharedPreferenceLiveData<Float> {
        return preferenceHelper.getNewSharedPreference().floatLiveData(BATTERY_HIGH_TEMPERATURE, DEFAULT_BATTERY_HIGH_TEMPERATURE)
    }

    fun getBatteryHighTemperaturePreference(): Float {
        return preferenceHelper.getFloat(BATTERY_HIGH_TEMPERATURE, DEFAULT_BATTERY_HIGH_TEMPERATURE)
    }

    fun setBatteryHighTemperaturePreference(batteryHighTemperature: Float) {
        preferenceHelper.putFloat(BATTERY_HIGH_TEMPERATURE, batteryHighTemperature)
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
        preferenceHelper.putBoolean(IS_VIBRATION_ENABLED, vibrationPreference)
    }

    fun getVibrationPreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(IS_VIBRATION_ENABLED, DEFAULT_IS_VIBRATION_ENABLED)
    }

    fun getVibrationPreference(): Boolean {
        return preferenceHelper.getBoolean(IS_VIBRATION_ENABLED, DEFAULT_IS_VIBRATION_ENABLED)
    }

    fun getRingOnSilentModePreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(RING_ON_SILENT_MODE, DEFAULT_RING_IN_SILENT_MODE)
    }

    fun getRingOnSilentModePreference(): Boolean {
        return preferenceHelper.getBoolean(RING_ON_SILENT_MODE, DEFAULT_RING_IN_SILENT_MODE)
    }

    fun setRingOnSilentModePreference(ringOnSilentModePreference:Boolean){
        preferenceHelper.putBoolean(RING_ON_SILENT_MODE, ringOnSilentModePreference)
    }

    fun getVibrateOnSilentModePreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(VIBRATE_ON_SILENT_MODE, DEFAULT_VIBRATE_IN_SILENT_MODE)
    }

    fun getVibrateOnSilentModePreference(): Boolean {
        return preferenceHelper.getBoolean(VIBRATE_ON_SILENT_MODE, DEFAULT_VIBRATE_IN_SILENT_MODE)
    }

    fun setVibrateOnSilentModePreference(vibrateOnSilentModePreference:Boolean){
        preferenceHelper.putBoolean(VIBRATE_ON_SILENT_MODE, vibrateOnSilentModePreference)
    }
    fun setSoundPreference(vibrationPreference:Boolean){
        preferenceHelper.putBoolean(IS_SOUND_ENABLED, vibrationPreference)
    }

    fun getSoundPreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(IS_SOUND_ENABLED, DEFAULT_IS_SOUND_ENABLED)
    }

    fun getSoundPreference(): Boolean {
        return preferenceHelper.getBoolean(IS_SOUND_ENABLED,DEFAULT_IS_SOUND_ENABLED)
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