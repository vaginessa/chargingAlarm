package com.chargingwatts.chargingalarm

import com.chargingwatts.chargingalarm.di.AppInjector
import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.BroadcastReceiver
import androidx.work.Worker
import dagger.android.*
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