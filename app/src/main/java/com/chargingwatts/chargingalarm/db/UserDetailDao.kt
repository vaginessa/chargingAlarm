package com.chargingwatts.chargingalarm.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chargingwatts.chargingalarm.vo.UserDetail

@Dao
interface UserDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userDetail: UserDetail)

    @Query("SELECT * FROM user_detail WHERE userId = :userId")
    fun findByUserId(userId: String): LiveData<UserDetail>

}