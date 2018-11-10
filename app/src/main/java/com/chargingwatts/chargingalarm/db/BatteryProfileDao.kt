package com.chargingwatts.chargingalarm.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chargingwatts.chargingalarm.vo.BatteryProfile

@Dao
interface BatteryProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(batteryProfile: BatteryProfile)

   // @Query("SELECT * FROM battery_profile WHERE time_stamp = (SELECT MAX(time_stamp) FROM battery_profile)")

   @Query("SELECT * FROM battery_profile WHERE time_stamp = 1")
   fun findRecentBatteryProfile(): LiveData<BatteryProfile>

}