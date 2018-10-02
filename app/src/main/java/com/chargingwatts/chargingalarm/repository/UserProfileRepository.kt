package com.chargingwatts.chargingalarm.repository

import android.arch.lifecycle.LiveData
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.api.AlarmApiService
import com.chargingwatts.chargingalarm.api.ApiResponse
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.db.UserDetailDao
import com.chargingwatts.chargingalarm.vo.Resource
import com.chargingwatts.chargingalarm.vo.UserDetail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val userDetailDao: UserDetailDao,
        private val alarmApiService: AlarmApiService
) {

    fun loadUserDetail(userId: String): LiveData<Resource<UserDetail>> {
        return object : NetworkBoundResource<UserDetail, UserDetail>(appExecutors) {
            override fun saveCallResult(item: UserDetail) {
                userDetailDao.insert(item)
            }

            override fun shouldFetch(data: UserDetail?) = true

            override fun loadFromDb() = userDetailDao.findByUserId(userId)

            override fun createCall(): LiveData<ApiResponse<UserDetail>> =
                    alarmApiService.getUser(userId)

        }.asLiveData()
    }
}