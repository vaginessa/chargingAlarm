package com.chargingwatts.chargingalarm.di.module

import com.chargingwatts.chargingalarm.di.module.ActivityCommonModule
import com.chargingwatts.chargingalarm.ui.batteryprofile.BatteryProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ActivityCommonModule::class])
abstract class BatteryActivityModule{
    @ContributesAndroidInjector
    abstract fun contributeBatteryLevelFragment(): BatteryProfileFragment
}