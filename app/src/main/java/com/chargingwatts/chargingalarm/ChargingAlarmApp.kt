package com.chargingwatts.chargingalarm

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import com.chargingwatts.chargingalarm.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import timber.log.Timber
import javax.inject.Inject

class ChargingAlarmApp : Application(), HasActivityInjector, HasBroadcastReceiverInjector {


    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingBroadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> =
            dispatchingActivityInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> =
            dispatchingBroadcastReceiverInjector


}