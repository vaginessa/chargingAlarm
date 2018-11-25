package com.chargingwatts.chargingalarm.util.settings

import AppConstants.BATTERY_HIGH_LEVEL
import AppConstants.BATTERY_HIGH_TEMPERATURE
import AppConstants.BATTERY_LOW_LEVEL
import AppConstants.USER_ALARM_PREFERENCE
import com.chargingwatts.chargingalarm.util.preference.*
import javax.inject.Inject
import javax.inject.Singleton

const val DEFAULT_BATTERY_HIGH_LEVEL = 100
const val DEFAULT_BATTERY_LOW_LEVEL = 35
const val DEFAULT_BATTERY_HIGH_TEMPERATURE = 42f
const val DEFAULT_USER_ALARM_PREFERENCE = false

@Singleton
class SettingsManager @Inject constructor(val preferenceHelper: PreferenceHelper) {


    fun getBatteryHighLevelPercentPreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(BATTERY_HIGH_LEVEL, DEFAULT_BATTERY_HIGH_LEVEL)
    }

    fun setBatteryHighLevelPercentPreference(batteryHighLevelPercent: Int) {
        preferenceHelper.putInt(BATTERY_HIGH_LEVEL, batteryHighLevelPercent)
    }

    fun getBatteryLowLevelPercentPreferenceLiveData(): SharedPreferenceLiveData<Int> {
        return preferenceHelper.getNewSharedPreference().intLiveData(BATTERY_LOW_LEVEL, DEFAULT_BATTERY_LOW_LEVEL)
    }

    fun setBatteryLowLevelPercentPreference(batteryLowLevelPercent: Int) {
        preferenceHelper.putInt(BATTERY_LOW_LEVEL, batteryLowLevelPercent)
    }

    fun getBatteryHighTemperaturePreferenceLiveData(): SharedPreferenceLiveData<Float> {
        return preferenceHelper.getNewSharedPreference().floatLiveData(BATTERY_HIGH_TEMPERATURE, DEFAULT_BATTERY_HIGH_TEMPERATURE)
    }

    fun setBatteryHighTemperaturePreference(batteryHighTemperature: Float) {
        preferenceHelper.putFloat(BATTERY_HIGH_TEMPERATURE, batteryHighTemperature)
    }

    fun setUserAlarmPreference(userAlarmPreference: Boolean) {
        preferenceHelper.putBoolean(USER_ALARM_PREFERENCE, userAlarmPreference)
    }

    fun getUserAlarmPreferenceLiveData(): SharedPreferenceLiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(USER_ALARM_PREFERENCE, false)
    }

    fun getSettingsProfile(): SettingsProfile {
        val batteryHighLevelPercent = getBatteryHighLevelPercentPreferenceLiveData().value
                ?: DEFAULT_BATTERY_HIGH_LEVEL
        val batteryLowLevelPercent = getBatteryLowLevelPercentPreferenceLiveData().value
                ?: DEFAULT_BATTERY_LOW_LEVEL
        val batteryHighTemperature = getBatteryHighTemperaturePreferenceLiveData().value
                ?: DEFAULT_BATTERY_HIGH_TEMPERATURE
        val userAlarmPreference = getUserAlarmPreferenceLiveData().value
                ?: DEFAULT_USER_ALARM_PREFERENCE

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