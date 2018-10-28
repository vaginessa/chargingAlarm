package com.chargingwatts.chargingalarm.db

import android.arch.lifecycle.LiveData
import android.util.Log
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryProfileDaoWrapper @Inject constructor( val batteryProfileDao: BatteryProfileDao,
                                                    val preferenceHelper:PreferenceHelper){

    fun insert(batteryProfile: BatteryProfile){
        batteryProfileDao.insert(batteryProfile)
        batteryProfile.isCharging?.let {
            preferenceHelper.putBoolean(AppConstants.IS_CHARGING_PREFERENCE, it)
        }
    }

    fun findRecentBatteryProfile(): LiveData<BatteryProfile>{
        return batteryProfileDao.findRecentBatteryProfile()
    }

}