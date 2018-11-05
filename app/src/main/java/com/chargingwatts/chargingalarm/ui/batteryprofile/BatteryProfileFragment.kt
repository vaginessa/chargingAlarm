package com.chargingwatts.chargingalarm.ui.batteryprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.BaseFragment
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.util.battery.BATTERY_WORKER_REQUEST_TAG
import com.chargingwatts.chargingalarm.util.battery.BatteryChangeReciever
import com.chargingwatts.chargingalarm.util.battery.BatteryMonitoringService
import com.chargingwatts.chargingalarm.util.battery.PeriodicBatteryUpdater
import javax.inject.Inject


class BatteryProfileFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var periodicBatteryUpdater: PeriodicBatteryUpdater
    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever
    private lateinit var batteryProfileViewModel: BatteryProfileViewModel

    private lateinit var mBtnStart: Button
    private lateinit var mBtnStop: Button
    private lateinit var mTvConnectionStatus: TextView
    private lateinit var mTvBatteryLevel: TextView
    private lateinit var mPbBatteryLevel: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_battery_profile, container, false)
        mBtnStart = view.findViewById(R.id.btn_start_battery_update)
        mBtnStop = view.findViewById(R.id.btn_stop_battery_update)
        mTvConnectionStatus = view.findViewById(R.id.tv_power_connection_status)
        mTvBatteryLevel = view.findViewById(R.id.tv_battery_level)
        mPbBatteryLevel = view.findViewById(R.id.pb_battery_level)
        val mediaPlayer = MediaPlayer.create(context,R.raw.ultra_alarm)
        mediaPlayer.isLooping = true

        mediaPlayer.setOnCompletionListener { MediaPlayer.OnCompletionListener{
            mediaPlayer.stop()
//            mediaPlayer.reset()
//            mediaPlayer.release()
        } }

        mBtnStart.setOnClickListener {
            context?.let { lContext ->
//                mediaPlayer.start()
//                VibrationManager.init(lContext)
//                VibrationManager.makePattern().beat(2000).rest(1000).playPattern(60)
                batteryProfileViewModel.setUserAlarmPreference(true)
                BatteryMonitoringService.startInForeground(lContext)
            }
        }
        mBtnStop.setOnClickListener {
            context?.let { lContext ->
//                mediaPlayer.stop()
//                mediaPlayer.prepare()
//                VibrationManager.stop()
                batteryProfileViewModel.setUserAlarmPreference(false)
                BatteryMonitoringService.stopService(lContext)
            }
        }
        return view

    }

    override fun onResume() {
        super.onResume()
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)
        context?.let{
            batteryChangeReciever.registerReciever(it)
        }
     }

    override fun onPause() {
        super.onPause()
        periodicBatteryUpdater.stopPeriodicBatteryUpdate()
        context?.let {
            batteryChangeReciever.unregisterReciever(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        batteryProfileViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BatteryProfileViewModel::class.java)

        batteryProfileViewModel.batteryProfileLiveData.observe(this, Observer { batteryProfile ->
            batteryProfile?.apply {
                remainingPercent?.let {
                    mTvBatteryLevel.text = "${it}%"
                    mPbBatteryLevel.progress = it
                }
            }
        })

        batteryProfileViewModel.isChargingPreference.observe(this, Observer {
            mTvConnectionStatus.text = it.toString()
        })
    }
}
