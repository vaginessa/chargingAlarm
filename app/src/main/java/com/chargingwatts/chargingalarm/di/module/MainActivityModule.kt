package com.chargingwatts.chargingalarm.di.module

import com.chargingwatts.chargingalarm.di.module.ActivityCommonModule
import com.chargingwatts.chargingalarm.ui.profile.FEProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ActivityCommonModule::class])
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): FEProfileFragment
}