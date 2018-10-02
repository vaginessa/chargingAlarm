package com.chargingwatts.chargingalarm.di.component

import com.chargingwatts.chargingalarm.BatteryActivity
import com.chargingwatts.chargingalarm.MainActivity
import com.chargingwatts.chargingalarm.di.module.BatteryActivityModule
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

@Subcomponent(modules = [BatteryActivityModule::class])
interface BatteryActivitySubComponent : AndroidInjector<BatteryActivity>{
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<BatteryActivity>(){
        override fun seedInstance(instance: BatteryActivity?) {
        }
    }
}