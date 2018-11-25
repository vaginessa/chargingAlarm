package com.chargingwatts.chargingalarm.repository

import androidx.lifecycle.LiveData
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDaoWrapper
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryProfileRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val batteryProfileDaoWrapper: BatteryProfileDaoWrapper,
        private val preferenceHelper: PreferenceHelper,
        private val settingsManager: SettingsManager
) {


    fun getUserAlarmPreference(): LiveData<Boolean> {
        return settingsManager.getUserAlarmPreferenceLiveData()
    }

    fun setUserAlarmPreference(pUserAlarmPreference: Boolean) {
        settingsManager.setUserAlarmPreference(pUserAlarmPreference)
    }

    fun getBatteryProfile(): LiveData<BatteryProfile> {
            return batteryProfileDaoWrapper.findRecentBatteryProfile()

    }

}