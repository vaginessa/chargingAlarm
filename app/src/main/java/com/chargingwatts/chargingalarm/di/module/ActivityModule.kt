package com.chargingwatts.chargingalarm.di.module

import android.app.Activity
import com.chargingwatts.chargingalarm.BatteryActivity
import com.chargingwatts.chargingalarm.MainActivity
import com.chargingwatts.chargingalarm.di.component.BatteryActivitySubComponent
import com.chargingwatts.chargingalarm.di.component.MainActivitySubComponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainActivitySubComponent::class, BatteryActivitySubComponent::class])
abstract class ActivityModule {

//    @ContributesAndroidInjector(modules = [MainActivityModule::class])
//    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivitySubComponent(builder: MainActivitySubComponent.Builder): AndroidInjector.Factory<out Activity>


    @Binds
    @IntoMap
    @ActivityKey(BatteryActivity::class)
    internal abstract fun bindBatteryActivitySubComponent(builder: BatteryActivitySubComponent.Builder):AndroidInjector.Factory<out Activity>


}