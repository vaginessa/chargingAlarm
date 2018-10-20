package com.chargingwatts.chargingalarm.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const  val CN_TIME_STAMP: String = "time_stamp"
const  val CN_STATUS: String = "status"
const  val CN_HEALTH: String = "health"
const  val CN_PLUGGED: String = "plugged"
const  val CN_PRESENT: String = "present"
const  val CN_LEVEL: String = "level"
const  val CN_SCALE: String = "scale"
const  val CN_VOLTAGE: String = "voltage"
const  val CN_TEMPERATURE: String = "temperature"
const  val CN_TECHNOLOGY: String = "technology"
const val CN_TOTAL_CAPACITY: String = "total_capacity"
const val CN_REMAINING_CAPACITY:String = "remaining_capacity"


@Entity(tableName = "battery_profile")
data class BatteryProfile(


        @PrimaryKey
        @ColumnInfo(name = CN_TIME_STAMP )
        val currentTimeStamp: Long? = 1,

        @ColumnInfo(name = CN_STATUS)
        val batteryStatusType: Int?,

        @ColumnInfo(name = CN_HEALTH)
        val batteryHealthType: Int?,

        @ColumnInfo(name = CN_PLUGGED)
        val batteryPlugType: Int?,

        @ColumnInfo(name = CN_PRESENT)
        val batteryPresent: Boolean?,

        @ColumnInfo(name = CN_LEVEL )
        val batteryLevel: Int?,

        @ColumnInfo(name = CN_SCALE)
        val batteryScale: Int?,

        @ColumnInfo(name = CN_VOLTAGE)
        val recentBatteryVoltage: Int?,

        @ColumnInfo(name = CN_TEMPERATURE)
        val recentBatteryTemperature: Int?,

        @ColumnInfo(name = CN_TECHNOLOGY)
        val batteryTechnology: String?,

        @ColumnInfo(name = CN_TOTAL_CAPACITY)
        val totalCapacity: Int?,

        @ColumnInfo(name = CN_REMAINING_CAPACITY)
        val remainingCapacity:Int?


)