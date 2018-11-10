package com.chargingwatts.chargingalarm.ui.batterydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chargingwatts.chargingalarm.BaseFragment
import com.chargingwatts.chargingalarm.databinding.FragmentBatteryDetailBinding
import com.chargingwatts.chargingalarm.ui.batteryprofile.BatteryProfileViewModel
import com.chargingwatts.chargingalarm.util.autoCleared
import com.chargingwatts.chargingalarm.util.battery.BatteryChangeReciever
import javax.inject.Inject


class BatteryDetailFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever

    private lateinit var batteryProfileViewModel: BatteryProfileViewModel

    var binding by autoCleared<FragmentBatteryDetailBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        batteryProfileViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BatteryProfileViewModel::class.java)
        binding = FragmentBatteryDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val lBatteryProfileLiveData = batteryProfileViewModel.batteryProfileLiveData
        lBatteryProfileLiveData.observe(this, Observer { lBatteryProfile ->
            binding.batteryProfile = lBatteryProfile
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            batteryChangeReciever.registerReciever(it)
        }
    }

    override fun onPause() {
        super.onPause()
        context?.let {
            batteryChangeReciever.unregisterReciever(it)
        }
    }

}
