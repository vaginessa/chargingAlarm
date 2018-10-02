package com.chargingwatts.chargingalarm.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.chargingwatts.chargingalarm.vo.UserDetail

@Dao
interface UserDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userDetail: UserDetail)

    @Query("SELECT * FROM user_detail WHERE userId = :userId")
    fun findByUserId(userId: String): LiveData<UserDetail>

}