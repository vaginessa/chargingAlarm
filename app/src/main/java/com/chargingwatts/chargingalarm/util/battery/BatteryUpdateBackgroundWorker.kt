package com.chargingwatts.chargingalarm.util.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chargingwatts.chargingalarm.BatteryAlarmActivity

class BatteryUpdateBackgroundWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {

        val sendIntent = Intent(applicationContext, BatteryAlarmActivity::class.java)
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplicationContext().startActivity(sendIntent)
        outputData = getBatteryProfileData()
        Log.d("AAJ", " DO WORK CALLLED")
        return Result.SUCCESS
    }

    fun getBatteryProfileData(): Data  {
        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }

        val dataBuilder = Data.Builder()
        intent?.let { batteryIntent ->
            val batteryProfile = BatteryProfileUtils.extractBatteryProfileFromIntent(batteryIntent)
            batteryProfile?.let {
                dataBuilder.putAll(BatteryProfileUtils.convertBatteryProfileToMap(it))
                Log.d("AAJ", " Battery Profile Data created")

            }
        }
        return dataBuilder.build()
    }

}