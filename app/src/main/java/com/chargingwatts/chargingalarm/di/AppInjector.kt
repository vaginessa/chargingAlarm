package com.chargingwatts.chargingalarm.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.chargingwatts.chargingalarm.AlarmActivity
import com.chargingwatts.chargingalarm.ChargingAlarmApp
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.di.component.AppComponent
import com.chargingwatts.chargingalarm.di.component.DaggerAppComponent
import com.chargingwatts.chargingalarm.di.module.BatteryUpdateWorkerModule
import com.chargingwatts.chargingalarm.util.battery.BatteryUpdateWorker
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Helper class to automatically inject fragments if they implement [Injectable].
 */
object AppInjector {
    var appComponent: AppComponent? = null
    var mIsAlarmScreenVisible = false
    var mIsHomeScreenVisible = false
    fun init(chargingAlarmApp: ChargingAlarmApp) {
        appComponent =
                DaggerAppComponent.builder().applicationContext(chargingAlarmApp)
                        .build()
        appComponent?.inject(chargingAlarmApp)
        chargingAlarmApp
                .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivityCreated")
                        handleActivity(activity)
                    }

                    override fun onActivityStarted(activity: Activity) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivityStarted")

                    }

                    override fun onActivityResumed(activity: Activity) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivityResumed")
                        if(activity is AlarmActivity){
                            mIsAlarmScreenVisible = true
                        }
                        else if(activity is HomeActivity){
                            mIsHomeScreenVisible = true
                        }


                    }

                    override fun onActivityPaused(activity: Activity) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivityPaused")
                        if(activity is AlarmActivity){
                            mIsAlarmScreenVisible = false
                        }
                        else if(activity is HomeActivity){
                            mIsHomeScreenVisible = false
                        }
                    }

                    override fun onActivityStopped(activity: Activity) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivityStopped")

                    }

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivitySaveInstanceState")

                    }

                    override fun onActivityDestroyed(activity: Activity) {
                        Log.d(AppInjector.javaClass.simpleName, activity.javaClass.simpleName + "- onActivityDestroyed")
                    }
                })
    }


    fun init(batteryUpdateWorker: BatteryUpdateWorker) {
        appComponent?.newBatteryUpdateWorkerComponent(BatteryUpdateWorkerModule())?.inject(batteryUpdateWorker)
    }

    fun isAlarmScreenVisible() = mIsAlarmScreenVisible

    fun isHomeScreenVisible() = mIsHomeScreenVisible

    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                            object : FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                                    Log.d(AppInjector.javaClass.simpleName, f.javaClass.simpleName + "- onFragmentPreAttached")

                                    if (f is Injectable) {
                                        AndroidSupportInjection.inject(f)
                                    }

                                }
                            }, true
                    )
        }
    }
}
