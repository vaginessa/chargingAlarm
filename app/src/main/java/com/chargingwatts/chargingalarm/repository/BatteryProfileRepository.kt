package com.chargingwatts.chargingalarm.repository

import AppConstants.USER_ALARM_PREFERENCE
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.util.preference.*

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


    fun getUserAlarmPreference(): LiveData<Boolean> {
        return preferenceHelper.getNewSharedPreference().booleanLiveData(USER_ALARM_PREFERENCE, false)

    }

    fun setUserAlarmPreference(pUserAlarmPreference: Boolean) {
        preferenceHelper.putBoolean(USER_ALARM_PREFERENCE, pUserAlarmPreference)
    }

    fun getBatteryProfile(): LiveData<BatteryProfile> {
        return batteryProfileDao.findRecentBatteryProfile()

    }

}