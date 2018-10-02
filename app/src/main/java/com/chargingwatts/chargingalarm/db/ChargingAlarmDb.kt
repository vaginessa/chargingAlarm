package com.chargingwatts.chargingalarm.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.chargingwatts.chargingalarm.vo.BatteryProfile
import com.chargingwatts.chargingalarm.vo.UserDetail

@Database(
        entities = [
            UserDetail::class,
            BatteryProfile::class],
        version = 1,
        exportSchema = false
)
abstract class ChargingAlarmDb : RoomDatabase() {
    abstract fun userDetailDao(): UserDetailDao
    abstract fun batteryProfileDao():BatteryProfileDao
}