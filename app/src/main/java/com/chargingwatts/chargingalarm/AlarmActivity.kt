package com.chargingwatts.chargingalarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.util.battery.BATTERY_WORKER_REQUEST_TAG
import com.chargingwatts.chargingalarm.util.battery.BatteryAlarmManager
import com.chargingwatts.chargingalarm.util.battery.BatteryChangeReciever
import com.chargingwatts.chargingalarm.util.battery.PeriodicBatteryUpdater
import com.chargingwatts.chargingalarm.util.settings.SettingsManager
import javax.inject.Inject


class AlarmActivity : BaseActivity() {
    @Inject
    lateinit var batteryAlarmManager: BatteryAlarmManager
    @Inject
    lateinit var settingsManager: SettingsManager
    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever
    @Inject
    lateinit var periodicBatteryUpdater: PeriodicBatteryUpdater


    private var mIvAlarmBell: ImageView? = null
    private var mIvPendulumAnimation: Animation? = null
    private var mTvStopAlarm: TextView? = null
    private var mTvAlarmMsg: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alarm)
        val argBundle = intent.extras
        val alarmMsg = argBundle?.getString(AlarmActivity.ALARM_MSG)
        val stopMsg = argBundle?.getString(AlarmActivity.STOP_MSG)

        mIvAlarmBell = findViewById(R.id.iv_alarm_bell)
        mIvPendulumAnimation = AnimationUtils.loadAnimation(this, R.anim.pendulum)
        mTvAlarmMsg = findViewById(R.id.tv_alarm_msg)
        mTvAlarmMsg?.text = alarmMsg
        mTvStopAlarm = findViewById(R.id.tv_stop_alarm);
        mTvStopAlarm?.text = stopMsg
        mTvStopAlarm?.setOnClickListener {
            batteryAlarmManager.stopAllAlarms()
            settingsManager.setUserAlarmPreference(false)
            displayHomeScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        mIvAlarmBell?.startAnimation(mIvPendulumAnimation)
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)
        try {
            batteryChangeReciever.registerReciever(this)
        } catch (exception: Exception) {

        }
    }

    override fun onPause() {
        super.onPause()
        try {
            batteryChangeReciever.unregisterReciever(this)

        } catch (exception: Exception) {

        }
    }

    private fun displayHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    companion object {

        val ALARM_MSG = "alarm_msg"
        val STOP_MSG = "stop_msg"

        fun launch(launchContext: Context, alarmMsg: String, stopMsg: String) {
            val alarmActivityIntent = Intent(launchContext, AlarmActivity::class.java)
            alarmActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val bundle = Bundle()
            bundle.putString(ALARM_MSG, alarmMsg)
            bundle.putString(STOP_MSG, stopMsg)
            alarmActivityIntent.putExtras(bundle)
            launchContext.startActivity(alarmActivityIntent)
        }
    }


}
