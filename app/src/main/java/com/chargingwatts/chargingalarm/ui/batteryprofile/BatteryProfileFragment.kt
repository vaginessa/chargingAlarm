package com.chargingwatts.chargingalarm.ui.batteryprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.BaseFragment
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.databinding.FragmentBatteryProfileBinding
import com.chargingwatts.chargingalarm.ui.vibrate.VibrationManager
import com.chargingwatts.chargingalarm.util.autoCleared
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

    var binding by autoCleared<FragmentBatteryProfileBinding>()

    var mediaPlayer: MediaPlayer? = null

    val callback = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.btn_start_service -> {
                    context?.let { lContext ->
                        mediaPlayer?.start()
                        VibrationManager.init(lContext)
                        VibrationManager.makePattern().beat(2000).rest(1000).playPattern(60)
                        batteryProfileViewModel.setUserAlarmPreference(true)
                        BatteryMonitoringService.startInForeground(lContext)
                    }
                }

                R.id.btn_stop_service -> {
                    context?.let { lContext ->
                        mediaPlayer?.stop()
                        mediaPlayer?.prepare()
                        VibrationManager.stop()
                        batteryProfileViewModel.setUserAlarmPreference(false)
                        BatteryMonitoringService.stopService(lContext)
                    }
                }
            }
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        batteryProfileViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BatteryProfileViewModel::class.java)
        binding = FragmentBatteryProfileBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val lBatteryProfileLiveData = batteryProfileViewModel.batteryProfileLiveData
        lBatteryProfileLiveData.observe(this, Observer { lBatteryProfile ->
            binding.batteryProfile = lBatteryProfile
        })
        binding.clickListener = callback


        mediaPlayer = MediaPlayer.create(context, R.raw.ultra_alarm)
        mediaPlayer?.isLooping = true

        mediaPlayer?.setOnCompletionListener {
            MediaPlayer.OnCompletionListener {
                mediaPlayer?.stop()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)
        context?.let {
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

}
