package com.chargingwatts.chargingalarm.di.module

import com.chargingwatts.chargingalarm.ui.intro.IntroFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ActivityCommonModule::class])
abstract class IntroActivityModule{
    @ContributesAndroidInjector
    abstract fun contributeIntroFragment(): IntroFragment

}