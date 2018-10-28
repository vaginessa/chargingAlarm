package com.chargingwatts.chargingalarm.repository

import AppConstants.IS_CHARGING_PREFERENCE
import AppConstants.USER_ALARM_PREFERENCE
import android.arch.lifecycle.LiveData
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDaoWrapper
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.chargingwatts.chargingalarm.util.preference.booleanLiveData
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryProfileRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val batteryProfileDaoWrapper: BatteryProfileDaoWrapper,
        private val preferenceHelper: PreferenceHelper
) {


    fun getUserAlarmPreference(): LiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(USER_ALARM_PREFERENCE, false)

    }

    fun setUserAlarmPreference(pUserAlarmPreference: Boolean) {
        preferenceHelper.putBoolean(USER_ALARM_PREFERENCE, pUserAlarmPreference)
    }

    fun getIsChargingPreference():LiveData<Boolean>{
        return preferenceHelper.getNewSharedPreference().booleanLiveData(IS_CHARGING_PREFERENCE,false)
    }

    fun getBatteryProfile(): LiveData<BatteryProfile> {
            return batteryProfileDaoWrapper.findRecentBatteryProfile()

    }

}