package com.chargingwatts.chargingalarm.di.module

import android.app.Activity
import android.content.BroadcastReceiver
import com.chargingwatts.chargingalarm.BatteryAlarmActivity
import com.chargingwatts.chargingalarm.MainActivity
import com.chargingwatts.chargingalarm.di.component.BatteryAlarmActivitySubComponent
import com.chargingwatts.chargingalarm.di.component.MainActivitySubComponent
import com.chargingwatts.chargingalarm.di.component.PowerConnectionReceiverSubComponent
import com.chargingwatts.chargingalarm.util.battery.PowerConnectionReceiver
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.BroadcastReceiverKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [PowerConnectionReceiverSubComponent::class])
abstract class BroadcastRecieverModule {

//    @ContributesAndroidInjector(modules = [MainActivityModule::class])
//    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @BroadcastReceiverKey(PowerConnectionReceiver::class)
    internal abstract fun bindPowerConnectionReciever(builder: PowerConnectionReceiverSubComponent.Builder): AndroidInjector.Factory<out BroadcastReceiver>

}