package com.chargingwatts.chargingalarm.di.module

import android.content.BroadcastReceiver
import com.chargingwatts.chargingalarm.di.component.PowerConnectionReceiverSubComponent
import com.chargingwatts.chargingalarm.util.battery.PowerConnectionReceiver
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.BroadcastReceiverKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [PowerConnectionReceiverSubComponent::class])
abstract class BroadcastRecieversModule {

//    @ContributesAndroidInjector(modules = [MainActivityModule::class])
//    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @BroadcastReceiverKey(PowerConnectionReceiver::class)
    internal abstract fun bindPowerConnectionReciever(builder: PowerConnectionReceiverSubComponent.Builder): AndroidInjector.Factory<out BroadcastReceiver>

}