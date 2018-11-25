package com.chargingwatts.chargingalarm.util.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chargingwatts.chargingalarm.util.preference.SharedPreferenceLiveData

class SettingsProfileLiveData(private val batteryHighLevelPercentPreferenceLiveData: SharedPreferenceLiveData<Int>,
                              private val batteryLowLevelPercentPreferenceLiveData: SharedPreferenceLiveData<Int>,
                              private val batteryHighTemperaturePreferenceLiveData: SharedPreferenceLiveData<Float>,
                              private val userAlarmPreferenceLiveData: SharedPreferenceLiveData<Boolean>) : LiveData<SettingsProfile>() {

    init {
        value = SettingsProfile(batteryHighLevelPercentPreferenceLiveData.value!!,
                batteryLowLevelPercentPreferenceLiveData.value!!,
                batteryHighTemperaturePreferenceLiveData.value!!,
                userAlarmPreferenceLiveData.value!!
        )
    }

    override fun onActive() {
        super.onActive()
        observeSettingsProfileChanges()
    }

    override fun onInactive() {
        super.onInactive()
        removeSettingsProfileObservers()
    }


    private val batteryHighLevelPercentObserver = Observer<Int> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.batteryHighLevelPercent = it
        value = newSettingsProfileCopy
    }

    private val batteryLowLevelPercentObserver = Observer<Int> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.batteryLowLevelPercent = it
        value = newSettingsProfileCopy
    }

    private val batteryHighTemperatureObserver = Observer<Float> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.batteryHighTemperature = it
        value = newSettingsProfileCopy
    }

    private val userAlarmPreferenceObserver = Observer<Boolean> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.userAlarmPreference = it
        value = newSettingsProfileCopy
    }


    private fun observeSettingsProfileChanges() {
        batteryHighLevelPercentPreferenceLiveData.observeForever(batteryHighLevelPercentObserver)
        batteryLowLevelPercentPreferenceLiveData.observeForever(batteryLowLevelPercentObserver)
        batteryHighTemperaturePreferenceLiveData.observeForever(batteryHighTemperatureObserver)
        userAlarmPreferenceLiveData.observeForever(userAlarmPreferenceObserver)
    }

    private fun removeSettingsProfileObservers() {
        batteryHighLevelPercentPreferenceLiveData.removeObserver(batteryHighLevelPercentObserver)
        batteryLowLevelPercentPreferenceLiveData.removeObserver(batteryLowLevelPercentObserver)
        batteryHighTemperaturePreferenceLiveData.removeObserver(batteryHighTemperatureObserver)
        userAlarmPreferenceLiveData.removeObserver(userAlarmPreferenceObserver)

    }

}
