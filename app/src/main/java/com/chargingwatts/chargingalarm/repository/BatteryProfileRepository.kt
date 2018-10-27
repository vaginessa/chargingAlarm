package com.chargingwatts.chargingalarm.repository

import AppConstants.USER_ALARM_PREFERENCE
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryProfileRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val batteryProfileDao: BatteryProfileDao,
        private val preferenceHelper: PreferenceHelper
) {
    private val _userAlarmPreferenceLiveData = MutableLiveData<Boolean>()


    fun getUserAlarmPreference():LiveData<Boolean>{
        _userAlarmPreferenceLiveData.value = preferenceHelper.getBoolean(USER_ALARM_PREFERENCE)
        return _userAlarmPreferenceLiveData
    }

    fun setUserAlarmPreference(pUserAlarmPreference:Boolean){
        preferenceHelper.putBoolean(USER_ALARM_PREFERENCE,pUserAlarmPreference)
        _userAlarmPreferenceLiveData.value = pUserAlarmPreference
    }
    fun getBatteryProfile(): LiveData<BatteryProfile> {
        return batteryProfileDao.findRecentBatteryProfile()

    }

}