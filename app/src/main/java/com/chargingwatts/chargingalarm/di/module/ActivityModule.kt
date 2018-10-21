package com.chargingwatts.chargingalarm.di.module

import android.app.Activity
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.MainActivity
import com.chargingwatts.chargingalarm.di.component.HomeActivitySubComponent
import com.chargingwatts.chargingalarm.di.component.MainActivitySubComponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainActivitySubComponent::class, HomeActivitySubComponent::class])
abstract class ActivityModule {

//    @ContributesAndroidInjector(modules = [MainActivityModule::class])
//    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivitySubComponent(builder: MainActivitySubComponent.Builder): AndroidInjector.Factory<out Activity>


    @Binds
    @IntoMap
    @ActivityKey(HomeActivity::class)
    internal abstract fun bindBatteryActivitySubComponent(builder: HomeActivitySubComponent.Builder):AndroidInjector.Factory<out Activity>


}