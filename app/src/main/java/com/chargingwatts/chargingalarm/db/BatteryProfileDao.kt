package com.chargingwatts.chargingalarm.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.chargingwatts.chargingalarm.vo.BatteryProfile

@Dao
interface BatteryProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(batteryProfile: BatteryProfile)

    @Query("SELECT * FROM battery_profile WHERE time_stamp = (SELECT MAX(time_stamp) FROM battery_profile)")
    fun findRecentBatteryProfile(): LiveData<BatteryProfile>

}