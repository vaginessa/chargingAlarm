package com.chargingwatts.chargingalarm.di.component

import com.chargingwatts.chargingalarm.di.module.PowerConnectionRecieverModule
import com.chargingwatts.chargingalarm.util.battery.PowerConnectionReceiver
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [PowerConnectionRecieverModule::class])
interface PowerConnectionReceiverSubComponent : AndroidInjector<PowerConnectionReceiver> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<PowerConnectionReceiver>() {
        override fun seedInstance(instance: PowerConnectionReceiver?) {
        }
    }
}
