package com.chargingwatts.chargingalarm.di.component

import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.IntroActivity
import com.chargingwatts.chargingalarm.MainActivity
import com.chargingwatts.chargingalarm.di.module.HomeActivityModule
import com.chargingwatts.chargingalarm.di.module.IntroActivityModule
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

@Subcomponent(modules = [HomeActivityModule::class])
interface HomeActivitySubComponent : AndroidInjector<HomeActivity>{
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<HomeActivity>(){
        override fun seedInstance(instance: HomeActivity?) {
        }
    }
}

@Subcomponent(modules = [IntroActivityModule::class])
interface IntroActivitySubComponent : AndroidInjector<IntroActivity>{
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<IntroActivity>(){
        override fun seedInstance(instance: IntroActivity?) {
        }
    }
}