package com.chargingwatts.chargingalarm.di.module

import com.chargingwatts.chargingalarm.ui.alarmscreen.AlarmFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ActivityCommonModule::class])
abstract class AlarmActivityModule{
    @ContributesAndroidInjector
    abstract fun contributeAlarmFragment(): AlarmFragment

}