package com.chargingwatts.chargingalarm.util.battery

import AppConstants.LOG_CHARGING_ALARM
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.di.component.DaggerAppComponent
import com.chargingwatts.chargingalarm.di.module.BatteryUpdateWorkerModule
import javax.inject.Inject

class BatteryUpdateWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    @Inject
    lateinit var mBatteryProfileDao: BatteryProfileDao

    @Inject
    lateinit var mAppExecutors: AppExecutors
    override fun doWork(): Result {

        DaggerAppComponent.builder().applicationContext(applicationContext = applicationContext).build().
                newBatteryUpdateWorkerComponent(BatteryUpdateWorkerModule()).inject(this)

        val sendIntent = Intent(applicationContext, HomeActivity::class.java)
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplicationContext().startActivity(sendIntent)

        updateBatteryProfile()
//        outputData = getBatteryProfileData()
        Log.d(LOG_CHARGING_ALARM, " DO WORK CALLLED")
        return Result.SUCCESS
    }

    fun updateBatteryProfile(){
        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
        intent?.let { batteryIntent ->
            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent,applicationContext)
            batteryProfile?.let{
                mAppExecutors.diskIO().execute {
                    Log.d(LOG_CHARGING_ALARM, "BatteryProfile Updated in Worker"+ batteryProfile.toString())
                    mBatteryProfileDao.insert(it)
                }
            }
        }
    }

//    fun getBatteryProfileData(): Data  {
//        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
//            applicationContext.registerReceiver(null, ifilter)
//        }
//
//        val dataBuilder = Data.Builder()
//        intent?.let { batteryIntent ->
//            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent)
//            batteryProfile?.let {
//                dataBuilder.putAll(BatteryProfileUtils.convertBatteryProfileToMap(it))
//                Log.d(LOG_CHARGING_ALARM, " Battery Profile Data created")
//            }
//        }
//        return dataBuilder.build()
//    }

}