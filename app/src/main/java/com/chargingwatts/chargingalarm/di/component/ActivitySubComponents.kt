package com.chargingwatts.chargingalarm.di.component

import com.chargingwatts.chargingalarm.BatteryAlarmActivity
import com.chargingwatts.chargingalarm.MainActivity
import com.chargingwatts.chargingalarm.di.module.BatteryAlarmActivityModule
import com.chargingwatts.chargingalarm.di.module.MainActivityModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>() {
        override fun seedInstance(instance: MainActivity?) {
        }
    }

}

@Subcomponent(modules = [BatteryAlarmActivityModule::class])
interface BatteryAlarmActivitySubComponent : AndroidInjector<BatteryAlarmActivity>{
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<BatteryAlarmActivity>(){
        override fun seedInstance(instance: BatteryAlarmActivity?) {
        }
    }
}