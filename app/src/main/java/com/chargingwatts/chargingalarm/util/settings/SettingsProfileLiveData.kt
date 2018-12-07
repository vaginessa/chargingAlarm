package com.chargingwatts.chargingalarm.util.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chargingwatts.chargingalarm.util.preference.SharedPreferenceLiveData

class SettingsProfileLiveData(private val batteryHighLevelPercentPreferenceLiveData: SharedPreferenceLiveData<Int>,
                              private val batteryLowLevelPercentPreferenceLiveData: SharedPreferenceLiveData<Int>,
                              private val batteryHighTemperaturePreferenceLiveData: SharedPreferenceLiveData<Float>,
                              private val userAlarmPreferenceLiveData: SharedPreferenceLiveData<Boolean>,
                              private val vibrationPreferenceLiveData: SharedPreferenceLiveData<Boolean>,
                              private val soundPreferenceLiveData: SharedPreferenceLiveData<Boolean>,
                              private val ringOnSilentModePreferenceLiveData: SharedPreferenceLiveData<Boolean>) : LiveData<SettingsProfile>() {

    init {
        value = SettingsProfile(batteryHighLevelPercentPreferenceLiveData.value!!,
                batteryLowLevelPercentPreferenceLiveData.value!!,
                batteryHighTemperaturePreferenceLiveData.value!!,
                userAlarmPreferenceLiveData.value!!,
                vibrationPreferenceLiveData.value!!,
                soundPreferenceLiveData.value!!,
                ringOnSilentModePreferenceLiveData.value!!
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
    private val vibrationPreferenceObserver = Observer<Boolean> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.isVibrationPreference = it
        value = newSettingsProfileCopy
    }
    private val soundPreferenceObserver = Observer<Boolean> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.isSoundPreference = it
        value = newSettingsProfileCopy
    }

    private val ringOnSilentModePreferenceObserver = Observer<Boolean> {
        val oldSettingsProfile = value
        val newSettingsProfileCopy = oldSettingsProfile
        newSettingsProfileCopy?.ringOnSilentMode = it
        value = newSettingsProfileCopy
    }


    private fun observeSettingsProfileChanges() {
        batteryHighLevelPercentPreferenceLiveData.observeForever(batteryHighLevelPercentObserver)
        batteryLowLevelPercentPreferenceLiveData.observeForever(batteryLowLevelPercentObserver)
        batteryHighTemperaturePreferenceLiveData.observeForever(batteryHighTemperatureObserver)
        userAlarmPreferenceLiveData.observeForever(userAlarmPreferenceObserver)
        vibrationPreferenceLiveData.observeForever(vibrationPreferenceObserver)
        soundPreferenceLiveData.observeForever(soundPreferenceObserver)
        ringOnSilentModePreferenceLiveData.observeForever(ringOnSilentModePreferenceObserver)
    }

    private fun removeSettingsProfileObservers() {
        batteryHighLevelPercentPreferenceLiveData.removeObserver(batteryHighLevelPercentObserver)
        batteryLowLevelPercentPreferenceLiveData.removeObserver(batteryLowLevelPercentObserver)
        batteryHighTemperaturePreferenceLiveData.removeObserver(batteryHighTemperatureObserver)
        userAlarmPreferenceLiveData.removeObserver(userAlarmPreferenceObserver)
        vibrationPreferenceLiveData.removeObserver(vibrationPreferenceObserver)
        soundPreferenceLiveData.removeObserver(soundPreferenceObserver)
        ringOnSilentModePreferenceLiveData.removeObserver(ringOnSilentModePreferenceObserver)
    }

}
