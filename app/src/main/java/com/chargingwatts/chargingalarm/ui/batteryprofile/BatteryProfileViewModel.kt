package com.chargingwatts.chargingalarm.ui.batteryprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chargingwatts.chargingalarm.repository.BatteryProfileRepository
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import javax.inject.Inject

class BatteryProfileViewModel
@Inject constructor( private val batteryProfileRepository: BatteryProfileRepository) : ViewModel() {
     val userAlarmPreferenceLiveData = batteryProfileRepository.getUserAlarmPreference()


    val batteryProfileLiveData: LiveData<BatteryProfile> =
            batteryProfileRepository.getBatteryProfile()

    fun setUserAlarmPreference(pUserAlarmPreference: Boolean) {
        batteryProfileRepository.setUserAlarmPreference(pUserAlarmPreference)
    }

}