package com.chargingwatts.chargingalarm.vo

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_detail",
        primaryKeys = ["userId"])
data class UserDetail(

        @SerializedName("uid")
        val userId: String,

        @SerializedName("nm")
        val name: String? = null,

        @SerializedName("mno")
        val mobileNo: String? = null
)