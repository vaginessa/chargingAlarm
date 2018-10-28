package com.chargingwatts.chargingalarm.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chargingwatts.chargingalarm.di.ViewModelKey
import com.chargingwatts.chargingalarm.ui.batteryprofile.BatteryProfileViewModel
import com.chargingwatts.chargingalarm.ui.profile.FEProfileViewModel
import com.chargingwatts.chargingalarm.viewmodel.ChargingAlarmViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FEProfileViewModel::class)
    abstract fun bindFEProfileViewModel(feProfileViewModel: FEProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BatteryProfileViewModel::class)
    abstract fun bindBatteryProfileViewModel(batteryProfileViewModel: BatteryProfileViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ChargingAlarmViewModelFactory): ViewModelProvider.Factory
}