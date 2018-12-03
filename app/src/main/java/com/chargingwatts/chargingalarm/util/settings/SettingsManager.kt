package com.chargingwatts.chargingalarm.util.settings

import AppConstants.BATTERY_HIGH_LEVEL
import AppConstants.BATTERY_HIGH_TEMPERATURE
import AppConstants.BATTERY_LOW_LEVEL
import AppConstants.USER_ALARM_PREFERENCE
import com.chargingwatts.chargingalarm.util.preference.*
import javax.inject.Inject
import javax.inject.Singleton

const val DEFAULT_BATTERY_HIGH_LEVEL = 100
const val DEFAULT_BATTERY_LOW_LEVEL = 15
//keep this temperature between 30 and 45
const val DEFAULT_BATTERY_HIGH_TEMPERATURE = 42f
const val DEFAULT_USER_ALARM_PREFERENCE = false

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

    fun getSettingsProfile(): SettingsProfile {
        val batteryHighLevelPercent = getBatteryHighLevelPercentPreference()
        val batteryLowLevelPercent = getBatteryLowLevelPercentPreference()
        val batteryHighTemperature = getBatteryHighTemperaturePreference()
        val userAlarmPreference = getUserAlarmPreference()

        return SettingsProfile(batteryHighLevelPercent, batteryLowLevelPercent, batteryHighTemperature, userAlarmPreference)

    }

    fun getSettingsProfileLivedata(): SettingsProfileLiveData =
            SettingsProfileLiveData(
                    getBatteryHighLevelPercentPreferenceLiveData(),
                    getBatteryLowLevelPercentPreferenceLiveData(),
                    getBatteryHighTemperaturePreferenceLiveData(),
                    getUserAlarmPreferenceLiveData()
            )


}