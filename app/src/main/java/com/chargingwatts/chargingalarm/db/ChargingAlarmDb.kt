package com.chargingwatts.chargingalarm.db

import androidx.room.Database
import androidx.room.RoomDatabase
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