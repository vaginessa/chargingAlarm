package com.chargingwatts.chargingalarm.di.component

import com.chargingwatts.chargingalarm.di.module.BatteryMonitoringServiceModule
import com.chargingwatts.chargingalarm.util.battery.BatteryMonitoringService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [BatteryMonitoringServiceModule::class])
interface BatteryMonitoringServiceSubComponent : AndroidInjector<BatteryMonitoringService> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BatteryMonitoringService>() {
        override fun seedInstance(instance: BatteryMonitoringService?) {
        }
    }
}
