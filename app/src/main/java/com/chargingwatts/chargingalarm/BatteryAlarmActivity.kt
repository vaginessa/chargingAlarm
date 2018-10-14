package com.chargingwatts.chargingalarm

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Dao
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.db.BatteryProfileDao
import com.chargingwatts.chargingalarm.util.battery.BatteryChangeReciever
import com.chargingwatts.chargingalarm.util.battery.PeriodicBatteryUpdater
import com.chargingwatts.chargingalarm.util.battery.PowerConnectionReceiver
import javax.inject.Inject

class BatteryAlarmActivity : BaseActivity() {
    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever
    @Inject
    lateinit var powerConnectionReceiver: PowerConnectionReceiver
    @Inject
    lateinit var periodicBatteryUpdater: PeriodicBatteryUpdater
    @Inject
    lateinit var batterProfileDao: BatteryProfileDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)

        batteryChangeReciever.apply {
            Log.d(batteryChangeReciever::class.simpleName, "app not null")
        }

        powerConnectionReceiver.apply {
            Log.d(powerConnectionReceiver::class.simpleName, "app not null")
        }
        periodicBatteryUpdater.apply {
            Log.d(periodicBatteryUpdater::class.simpleName, "app not null")
        }
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)


    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryChangeReciever, intentFilter)

        batterProfileDao.findRecentBatteryProfile().observe(this, Observer {
            findViewById<TextView>(R.id.tv_battery_level).setText(it?.batteryLevel?.toString())
        })
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryChangeReciever)
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.battery_activity_nav_host_fragment).navigateUp()
    }
}
