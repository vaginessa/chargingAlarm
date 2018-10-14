package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import java.util.concurrent.TimeUnit
import com.chargingwatts.chargingalarm.MainActivity
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.work.WorkerParameters
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodicBatteryUpdater @Inject constructor(val batteryProfileDao: BatteryProfileDao,
                                                 val appExecutors: AppExecutors) {
    fun startPeriodicBatteryUpdate(repeatInterval: Long) {
        if (repeatInterval < PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS) {
            throw IllegalArgumentException("repeatInterval = $repeatInterval which is less than PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS} = ${PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS} is not allowed")
        }

        WorkManager.getInstance().enqueue(createPeriodicRequest(repeatInterval))
    }

    fun createPeriodicRequest(repeatInterval: Long): PeriodicWorkRequest {
        val periodicBatteryUpdateRequest = PeriodicWorkRequest.Builder(BatteryUpdateBackgroundWorker::class.java, repeatInterval
                , TimeUnit.MILLISECONDS)
        val periodicWorkRequest = periodicBatteryUpdateRequest.build()
        return periodicWorkRequest
    }


    inner class BatteryUpdateBackgroundWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val sendIntent = Intent(applicationContext, MainActivity::class.java)
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            getApplicationContext().startActivity(sendIntent)

            updateBatteryProfile()
            return Result.SUCCESS

        }

        fun updateBatteryProfile() {
            val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                applicationContext.registerReceiver(null, ifilter)
            }

            intent?.let { batteryIntent ->
                val batteryProfile = BatteryProfileIntentExtractor.extractBatteryProfileFromIntent(batteryIntent)
                batteryProfile?.let {
                    appExecutors.diskIO().execute { batteryProfileDao.insert(it) }
                }
            }
        }

    }
}

