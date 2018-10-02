package com.chargingwatts.chargingalarm.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.api.ApiResponse
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import com.chargingwatts.chargingalarm.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryProfileRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val batteryProfileDao: BatteryProfileDao) {
    fun loadBatteryProfile(): LiveData<BatteryProfile> {
        refreshBatteryProfileInDb()
        return batteryProfileDao.findRecentBatteryProfile()

    }

    fun refreshBatteryProfileInDb(){
    }
}