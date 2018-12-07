package com.chargingwatts.chargingalarm.util.settings


data class SettingsProfile(
        var batteryHighLevelPercent: Int,
        var batteryLowLevelPercent: Int,
        var batteryHighTemperature: Float,
        var userAlarmPreference: Boolean,
        var isVibrationPreference:Boolean,
        var isSoundPreference:Boolean,
        var ringOnSilentMode:Boolean,
        var vibrateOnSilentMode:Boolean


        )