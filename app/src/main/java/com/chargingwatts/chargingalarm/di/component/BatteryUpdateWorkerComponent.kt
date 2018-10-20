package com.chargingwatts.chargingalarm.di.component

import com.chargingwatts.chargingalarm.di.module.BatteryUpdateWorkerModule
import com.chargingwatts.chargingalarm.util.battery.BatteryUpdateWorker
import dagger.Subcomponent

@Subcomponent(modules = [BatteryUpdateWorkerModule::class])
interface BatteryUpdateWorkerComponent {
    fun inject(batteryUpdateWorker: BatteryUpdateWorker)
}