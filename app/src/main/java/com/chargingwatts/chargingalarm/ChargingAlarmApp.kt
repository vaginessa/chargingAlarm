package com.chargingwatts.chargingalarm

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import androidx.multidex.MultiDexApplication
import com.chargingwatts.chargingalarm.di.AppInjector
import dagger.android.*
import timber.log.Timber
import javax.inject.Inject


class ChargingAlarmApp : MultiDexApplication(), HasActivityInjector, HasBroadcastReceiverInjector, HasServiceInjector {



    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingBroadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

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

    override fun serviceInjector(): AndroidInjector<Service> =
            dispatchingServiceInjector
}