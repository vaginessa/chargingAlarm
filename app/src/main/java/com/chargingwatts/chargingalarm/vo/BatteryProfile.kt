package com.chargingwatts.chargingalarm.vo

import AppConstants
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "battery_profile")
data class BatteryProfile(
        @PrimaryKey
        @ColumnInfo(name = "time_stamp")
        val currentTimeStamp: Long? = 1,

        @ColumnInfo(name = "status")
        val batteryStatusType: Int?,

        @ColumnInfo(name = "health")
        val batteryHealthType: Int?,

        @ColumnInfo(name = "plugged")
        val batteryPlugType: Int?,

        @ColumnInfo(name = "present")
        val batteryPresent: Boolean?,

        @ColumnInfo(name = "level")
        val batteryLevel: Int?,

        @ColumnInfo(name = "scale")
        val batteryScale: Int?,

        @ColumnInfo(name = "voltage")
        val recentBatteryVoltage: Int?,

        @ColumnInfo(name = "temperature")
        val recentBatteryTemperature: Int?,

        @ColumnInfo(name = "technology")
        val batteryTechnology: String?

)