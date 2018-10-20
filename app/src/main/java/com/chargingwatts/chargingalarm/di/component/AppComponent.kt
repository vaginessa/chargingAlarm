package com.chargingwatts.chargingalarm.di.component

import android.app.Application
import android.content.Context
import com.chargingwatts.chargingalarm.ChargingAlarmApp
import com.chargingwatts.chargingalarm.di.module.ActivityModule
import com.chargingwatts.chargingalarm.di.module.AppModule
import com.chargingwatts.chargingalarm.di.module.BatteryUpdateWorkerModule
import com.chargingwatts.chargingalarm.di.module.BroadcastRecieverModule
import com.chargingwatts.chargingalarm.util.battery.BatteryUpdateWorker
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            ActivityModule::class,
            BroadcastRecieverModule::class
        ]
)

interface AppComponent {

    @Component.Builder
    interface Builder {

        // this method was necessary as application could not be provided from inside the Worker
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

//        @BindsInstance
//        fun application(application: Application):Builder

        fun build(): AppComponent
    }

    fun inject(chargingAlarmApp: ChargingAlarmApp)

    fun newBatteryUpdateWorkerComponent(batteryUpdateWorkerModule: BatteryUpdateWorkerModule): BatteryUpdateWorkerComponent

}