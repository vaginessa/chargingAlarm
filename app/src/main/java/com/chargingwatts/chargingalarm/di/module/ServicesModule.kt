package com.chargingwatts.chargingalarm.di.module

import android.app.Service
import com.chargingwatts.chargingalarm.di.component.BatteryMonitoringServiceSubComponent
import com.chargingwatts.chargingalarm.util.battery.BatteryMonitoringService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [BatteryMonitoringServiceSubComponent::class])
abstract class ServicesModule {

//    @ContributesAndroidInjector(modules = [MainActivityModule::class])
//    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ServiceKey(BatteryMonitoringService::class)
    internal abstract fun bindBatteryMonitoringService(builder: BatteryMonitoringServiceSubComponent.Builder): AndroidInjector.Factory<out Service>

}