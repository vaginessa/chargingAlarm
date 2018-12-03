package com.chargingwatts.chargingalarm.di.module

import com.chargingwatts.chargingalarm.ui.batterydetail.BatteryDetailFragment
import com.chargingwatts.chargingalarm.ui.batteryprofile.BatteryProfileFragment
import com.chargingwatts.chargingalarm.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ActivityCommonModule::class])
abstract class HomeActivityModule{
    @ContributesAndroidInjector
    abstract fun contributeBatteryLevelFragment(): BatteryProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeBatteryDetailFragment(): BatteryDetailFragment


    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}