package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.ui.vibrate.VibrationManager
import com.chargingwatts.chargingalarm.util.AlarmMediaManager
import com.chargingwatts.chargingalarm.util.ringtonepicker.RingTonePlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryAlarmManager @Inject constructor(val context: Context) : ContextWrapper(context) {
    lateinit var alarmMediaManager: AlarmMediaManager
    lateinit var vibrationManager: VibrationManager


    init {
        context.applicationContext.let {
            alarmMediaManager = AlarmMediaManager(it)
            vibrationManager = VibrationManager(it)

        }
    }


    fun initateAlarm() {
        displayAlarmScreen()
        startAlarmTone()
        startVibration()
    }

    fun stopAlarm() {
        //TODO DECIDE WHICH SCREEN TO OPEN ON STOP ALARM AND INCLUDE IT HERE
        stopAlarmTone()
        stopVibration()
    }

    fun displayAlarmScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun startAlarmTone() {
        ///TODO uri of alarm tone selected by user to be used here
        val uri = Uri.parse("android.resource://$packageName/raw/ultra_alarm")
        alarmMediaManager.playAlarmSound(uri)

    }

    fun stopAlarmTone() {
        alarmMediaManager.stopAlarmSound()
    }

    fun startVibration() {
        vibrationManager.makePattern().beat(2000).rest(1000).playPattern(30)
    }

    fun stopVibration() {
        vibrationManager.stop()
    }


}